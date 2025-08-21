package compiler.phase.finalsol;

import java.util.*;
import compiler.phase.asmgen.Instr;
import compiler.phase.imclin.LIN;
import compiler.phase.livean.LivenessAnalyzer;
import compiler.phase.memory.MEM;
import compiler.phase.regall.SpillRegulator;
import compiler.phase.imclin.ImcLin;
public class CompF {
    private final List<String> finalInstructions = new ArrayList<>();

    public CompF(Map<LIN.CodeChunk, List<Instr>> codeChunkToInstructions, List<LIN.DataChunk> dataChunks, SpillRegulator spillRegulator, Map<LIN.CodeChunk, Long> max_spills) {
        // Generate the .data section
        initFunction();

        generateDataSection(dataChunks);

        // Generate the prologue, body, and epilogue for each function
        for (Map.Entry<LIN.CodeChunk, List<Instr>> entry : codeChunkToInstructions.entrySet()) {
            LIN.CodeChunk codeChunk = entry.getKey();
            List<Instr> instructions = entry.getValue();

            generateFunction(codeChunk, instructions,spillRegulator,max_spills);
        }
        printHelpers();
    }

    private void generateDataSection(List<LIN.DataChunk> dataChunks) {
        finalInstructions.add(".data");
        for (LIN.DataChunk dataChunk : dataChunks) {
            String label = dataChunk.label.name;
            String directive = ".dword"; // Example: `.word` for integers
            String value; 
            if (dataChunk.init != null && !dataChunk.init.isEmpty()) {
                directive = ".asciz"; // Use `.ascii` for strings
                value = dataChunk.init; // Use the initial value if provided

                // add quotes around the string value
                value = "\"" + value + "\""; // Add quotes for string literals
            } else {
                value = "0"; // Default value if no initial value is specified
            }
           
            finalInstructions.add(label + ": " + directive + " " + value);
        }
        finalInstructions.add(""); // Add a blank line for separation
    }
    private boolean includeInstr(Instr instr) {
        boolean isValid = true;
        // Check if the instruction is valid for final output
        // split instruction
      
        String line = instr.toString();              // convert Instr to String
        line = line.replace(",", "");                // remove commas
        String[] parts = line.trim().split("\\s+");
        
        if (parts[0].equals("mv")){
            
            if(parts[1].equals(parts[2])){
                    isValid = false; // Skip move instructions that do not change state
                
            }
        }
        
        
        return isValid;
    }

    private void generateFunction(LIN.CodeChunk codeChunk, List<Instr> instructions, SpillRegulator spillRegulator,Map<LIN.CodeChunk, Long> max_spills) {
        String functionName = codeChunk.frame.label.name;
        String returnAddress = codeChunk.frame.RV.toString();
        //System.out.println("return address is " + returnAddress);

        // Add function label
        finalInstructions.add(".text");
       
        finalInstructions.add(functionName + ":");

        // Generate prologue
        Map<String, String> tempToReg = spillRegulator.getTempToRegMappingForChunk(codeChunk);
        //System.out.println("Temp to register mapping for " + functionName + ": " + tempToReg);
        List<String> usedRegs = new ArrayList<>(tempToReg.values());
        // get unique registers
        usedRegs = new ArrayList<>(new HashSet<>(usedRegs)); // Remove duplicates
        Collections.sort(usedRegs); // Sort for consistent output
        //add to list
        

        //System.out.println("Used registers for " + functionName + ": " + usedRegs);
        long max_spill = max_spills.getOrDefault(codeChunk, 0L);
        generatePrologue(codeChunk,usedRegs,max_spill);

        // Add function body
        String fp = codeChunk.frame.FP.toString();
        String true_Fp = tempToReg.getOrDefault(fp, fp); // Get the register for FP, or use the temp name if not mapped
        //System.out.println("FP register for " + functionName + ": " + true_Fp);
        // save SP value into true_Fp
        long frameSize = codeChunk.frame.size;
        // Save SP to FP
      
        for (Instr instr : instructions) {
            if (includeInstr(instr)) {
                finalInstructions.add("    " + instr.toString());
            }
        }
        // check RV register to see if it is used in the function using livenessAnalyzer
        // Get the temp-to-register mapping (assume this is passed or accessible)
        
        // Get the return value temp
        MEM.Temp returnValue = codeChunk.frame.RV;
        String returnTemp = returnValue.toString();

        // Find the register corresponding to the return value temp
        String returnRegister = tempToReg.get(returnTemp);

        // Generate epilogue
        generateEpilogue(codeChunk,usedRegs,spillRegulator,max_spill);
        finalInstructions.add("");
    }

    private void generatePrologue(LIN.CodeChunk codeChunk, List<String> usedRegs, long max_spill) {
        String SP_check = "sp";
        String s0 = "s0";
        // exclude sp and s0 from usedRegs
        //System.out.println("Before is: " + usedRegs.size());
        
        usedRegs.removeIf(reg -> reg.equals(SP_check) || reg.equals(s0));
        //System.out.println("after is: " + usedRegs.size());
        //System.out.println("codeChunk.frame.size" +codeChunk.frame.size);
        //System.out.println("usedRegs.size() "+ usedRegs.size());
        //System.out.println("max_spill" + max_spill);
        long frameSize = max_spill + usedRegs.size() * 8 + codeChunk.frame.size; // 16 for ra and s0, plus space for used registers
        //System.out.println("frameSize is " + frameSize);
        long new_sp = frameSize;
        if (new_sp % 8 != 0) {
            new_sp -= 4; // Ensure stack pointer is aligned to 8 bytes
        }
        // Allocate stack space
        finalInstructions.add("    addi sp, sp, -" + new_sp);

        // Save return address (typically at highest address in the frame)
        finalInstructions.add("    sd ra, " + (frameSize - 8) + "(sp)");

        // Save frame pointer (s0)
        finalInstructions.add("    sd s0, " + (frameSize - 16) + "(sp)");

        // Save other callee-saved registers (starting below saved s0)
        for (int i = 0; i < usedRegs.size(); i++) {
            String reg = usedRegs.get(i);
            int offset = (int)(frameSize - 24 - i * 8);  // adjust offsets downward
            finalInstructions.add("    sd " + reg + ", " + offset + "(sp)");
        }
        //System.out.println("Frame size for " + codeChunk.frame.label.name + ": " + frameSize);
        //System.out.println("    addi s0, sp, " + frameSize);
        
        // Set frame pointer to point to the old sp (top of frame)
       
        finalInstructions.add("    addi s0, sp, " + new_sp);
    }

    private void generateEpilogue(LIN.CodeChunk codeChunk, List<String> usedRegs, SpillRegulator spillRegulator,long max_spill) {
        String SP_check = "sp";
        String s0 = "s0";
        // exclude sp and s0 from usedRegs
        usedRegs.removeIf(reg -> reg.equals(SP_check) || reg.equals(s0));

        long frameSize = max_spill + usedRegs.size() * 8 + codeChunk.frame.size; // 16 for ra and s0, plus space for used registers
        //System.out.println("frameSize is " + frameSize);
        String returnValue = codeChunk.frame.RV.toString();
        String true_RV = spillRegulator.getTempToRegMappingForChunk(codeChunk).getOrDefault(returnValue, returnValue); // Get the register for RV, or use the temp name if not mapped
        //System.out.println("Return value register for " + codeChunk.frame.label.name + ": " + true_RV);
        

        // Restore callee-saved registers
        for (int i = usedRegs.size() - 1; i >= 0; i--) {
            String reg = usedRegs.get(i);
            int offset = (int)(frameSize - 24 - i * 8); // Adjust offsets downward
            finalInstructions.add("    ld " + reg + ", " + offset + "(sp)");
        }

        // Restore frame pointer (s0)
        finalInstructions.add("    ld s0, " + (frameSize - 16) + "(sp)");

        // Restore return address (ra)
        finalInstructions.add("    ld ra, " + (frameSize - 8) + "(sp)");

        // Deallocate stack space
        long old_sp = frameSize;
        if (old_sp % 8 != 0) {
            old_sp -= 4; // Ensure stack pointer is aligned to 8 bytes
        }
        finalInstructions.add("    addi sp, sp, " + old_sp);

        // Return to caller
        if (codeChunk.frame.label.name.equals("_main")) {
            finalInstructions.add("    li a7, 10"); // Exit syscall
            finalInstructions.add("    ecall"); // Perform syscall
        } else {
            finalInstructions.add("    jr ra");
        }
    }
    public List<String> getFinalInstructions() {
        return finalInstructions;
    }
    private void initFunction() {
        finalInstructions.add(".globl _start");
        finalInstructions.add("_start:");
        finalInstructions.add("    li sp, 0x7ffffff0");
        finalInstructions.add("    li a0, 0");
        finalInstructions.add("    sd a0, 0(sp)");
        finalInstructions.add("    call _main");
        finalInstructions.add("    ld a0, 0(sp)");
        finalInstructions.add("    li a7, 93");
        finalInstructions.add("    ecall");
        finalInstructions.add(""); // Add a blank line for separation
    }

    private void printHelpers() {
        boolean userDefinedPutInt = ImcLin.codeChunks().stream()
            .anyMatch(chunk -> chunk.frame.label.name.equals("_putint"));
        if (!userDefinedPutInt) addPutInt();

        boolean userDefinedPutChar = ImcLin.codeChunks().stream()
            .anyMatch(chunk -> chunk.frame.label.name.equals("_putchar"));
        if (!userDefinedPutChar) addPutChar();

        boolean userDefinedPutString = ImcLin.codeChunks().stream()
            .anyMatch(chunk -> chunk.frame.label.name.equals("_puts"));
        if (!userDefinedPutString) addPutString();

        // Add native_malloc function
        addNativeMalloc();
    }

    private void addPutInt() {
        finalInstructions.add("");
        finalInstructions.add("# Print Integer Helper Function");
        finalInstructions.add("_putint:");
        finalInstructions.add("    sd a0, -8(sp)");
        finalInstructions.add("    sd a7, -16(sp)");
        finalInstructions.add("    ld a0, 8(sp)");
        finalInstructions.add("    li a7, 1");
        finalInstructions.add("    ecall");
        finalInstructions.add("    ld a0, -8(sp)");
        finalInstructions.add("    ld a7, -16(sp)");
        finalInstructions.add("    ret");
    }

    private void addPutChar() {
        finalInstructions.add("");
        finalInstructions.add("# Print Character Helper Function");
        finalInstructions.add("_putchar:");
        finalInstructions.add("    sd a0, -8(sp)");
        finalInstructions.add("    sd a7, -16(sp)");
        finalInstructions.add("    ld a0, 8(sp)");
        finalInstructions.add("    li a7, 11");
        finalInstructions.add("    ecall");
        finalInstructions.add("    ld a0, -8(sp)");
        finalInstructions.add("    ld a7, -16(sp)");
        finalInstructions.add("    ret");
    }

    private void addPutString() {
        finalInstructions.add("");
        finalInstructions.add("# Print String Helper Function");
        finalInstructions.add("_puts:");
        finalInstructions.add("    sd a0, -8(sp)");
        finalInstructions.add("    sd a7, -16(sp)");
        finalInstructions.add("    ld a0, 8(sp)");
        finalInstructions.add("    li a7, 4");
        finalInstructions.add("    ecall");
        finalInstructions.add("    ld a0, -8(sp)");
        finalInstructions.add("    ld a7, -16(sp)");
        finalInstructions.add("    ret");
    }
    private void addNativeMalloc() {
        finalInstructions.add("");
        finalInstructions.add("# Native Malloc Function");
        finalInstructions.add("_malloc:");
        finalInstructions.add("    sd a0, -8(sp)"); // Save size argument (a0) on the stack
        finalInstructions.add("    sd tp, 0(sp)"); // Save the current heap pointer (tp) on the stack
        finalInstructions.add("    ld a0, 8(sp)"); // Load size argument from the stack
        finalInstructions.add("    add tp, tp, a0"); // Increment heap pointer by size
        finalInstructions.add("    andi tp, tp, -8"); // Align heap pointer to 8 bytes
        finalInstructions.add("    addi tp, tp, 8"); // Add 8 bytes for alignment
        finalInstructions.add("    ld a0, -8(sp)"); // Restore size argument (a0)
        finalInstructions.add("    ret"); // Return to caller
    }

    

}
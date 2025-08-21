package compiler.phase.regall;

import java.util.*;
import compiler.phase.asmgen.Instr;
import compiler.phase.livean.LivenessAnalyzer;
import compiler.phase.livean.InstrLiveness;
import compiler.phase.imclin.LIN;
import compiler.phase.memory.MEM;


public class SpillRegulator {
    private final LivenessAnalyzer livenessAnalyzer;
    private final List<String> registers;
    private final Map<LIN.CodeChunk, List<Instr>> chunkToInstructions;
    private final Map<LIN.CodeChunk, Map<String, String>> chunkToTempToReg = new HashMap<>();
    private final Map<String, Integer> instrToSpillOffset = new HashMap<>();
    private final Map<LIN.CodeChunk, Long> spilloffsets_max = new HashMap<>();
    public SpillRegulator(LivenessAnalyzer livenessAnalyzer, List<String> registers) {
        this.livenessAnalyzer = livenessAnalyzer;
        this.registers = registers;
        this.chunkToInstructions = livenessAnalyzer.getCodeChunkToInstructions();  
    }

    public void processChunks() {
        for (LIN.CodeChunk codeChunk : chunkToInstructions.keySet()) {
            //System.out.println("Processing chunk: " + codeChunk.frame.label.name);

            // Get the interference graph and instructions for this chunk
            Map<String, Set<String>> interferenceGraph = livenessAnalyzer.getInterferenceGraphForChunk(codeChunk);
            List<Instr> instructions = chunkToInstructions.get(codeChunk);

            // Perform register allocation and handle spills
            processChunk(codeChunk, interferenceGraph, instructions);
        }
    }
    public Map<LIN.CodeChunk, List<Instr>> getUpdatedInstructions() {
        return chunkToInstructions;
    }

    private void processChunk(LIN.CodeChunk codeChunk, Map<String, Set<String>> interferenceGraph, List<Instr> instructions) {
        boolean hasSpills = true;
        int spillIterationLimit = 20;
        long spillOffset_start = codeChunk.frame.argsSize + 8;
        while (hasSpills && spillIterationLimit > 0) {
            Regall regall = new Regall(interferenceGraph, registers, instructions);
            regall.allocateRegisters(livenessAnalyzer, codeChunk);

            Set<String> spilled = regall.getSpilled();

            if (!spilled.isEmpty()) {
                //System.out.println("_______________________");
                for (Instr instr : instructions) {
                    //System.out.println(instr);
                }
                //System.out.println("_______________________");
                System.out.println("Spills detected in chunk " + codeChunk.frame.label.name + ": " + spilled);
                // update instrToSpillOffset
               
                for (String temp_spilled : spilled) {
                    if (!instrToSpillOffset.containsKey(temp_spilled)) {
                        instrToSpillOffset.put(temp_spilled, (int) spillOffset_start);
                        spillOffset_start += 8; // Assuming each spill takes 4 bytes
                    }
                }
                System.out.println("Spill offsets: " + instrToSpillOffset);
                // Add spill code and update instructions
                instructions = addSpillCode(instructions, spilled);

                // Recompute liveness and interference graph
                livenessAnalyzer.recomputeLivenessForChunk(codeChunk, instructions);
                interferenceGraph = livenessAnalyzer.getInterferenceGraphForChunk(codeChunk);

                spillIterationLimit--;
            } else {
                System.out.println("No spills detected in chunk " + codeChunk.frame.label.name + "spill " + spillOffset_start);
                spilloffsets_max.put(codeChunk, 0L);
                // Get the temp-to-register mapping from Regall
                Map<String, String> tempToReg = regall.getAllocation();

                // Populate the temp-to-register mapping in LivenessAnalyzer
                this.setTempToRegMappingForChunk(codeChunk, tempToReg);

                // Update instructions with the allocated registers
                instructions = updateInstructionsWithRegisters(instructions, tempToReg, codeChunk);

                // Update the chunkToInstructions map with the final instructions
                chunkToInstructions.put(codeChunk, instructions);

                hasSpills = false;
            }
        }
    }

    private List<Instr> addSpillCode(List<Instr> instructions, Set<String> spilledValues) {
        List<Instr> updatedInstructions = new ArrayList<>();
        long offset = 0;
        for (Instr instr : instructions) {
            if (instr instanceof Instr.Oper operInstr) {
                // Handle 'def' operand
                if (spilledValues.contains(operInstr.getDef())) {
                    String tempRegister = newTemp();
                    offset = instrToSpillOffset.getOrDefault(operInstr.getDef(), 0);
                    operInstr.setOperation(tempRegister, operInstr.getUse().toArray(new String[0]));
                    updatedInstructions.add(operInstr);
                    updatedInstructions.add(new Instr.Oper(null, "sd", new String[]{tempRegister, (offset) + "(sp)"}));
                    //System.out.println("new instr added: " + updatedInstructions.get(updatedInstructions.size() - 1));
                    continue;
                }

                // Handle 'use' operands
                List<String> updatedUse = new ArrayList<>();
                for (String use : operInstr.getUse()) {
                    if (spilledValues.contains(use)) {
                        offset = instrToSpillOffset.getOrDefault(use, 0);
                        String tempRegister = newTemp();
                        updatedInstructions.add(new Instr.Oper(null, "ld", new String[]{tempRegister, (offset) + "(sp)"}));
                        updatedUse.add(tempRegister);
                    } else {
                        updatedUse.add(use);
                    }
                }
                operInstr.setOperation(operInstr.getDef(), updatedUse.toArray(new String[0]));
                updatedInstructions.add(operInstr);
            } else if (instr instanceof Instr.Move moveInstr) {
                // Handle 'def' operand
                if (spilledValues.contains(moveInstr.getDef())) {
                    String tempRegister = newTemp();
                    offset = instrToSpillOffset.getOrDefault(moveInstr.getDef(), 0);
                    moveInstr.setOperation(tempRegister, moveInstr.getUse());
                    updatedInstructions.add(moveInstr);
                    updatedInstructions.add(new Instr.Oper(null, "sd", new String[]{tempRegister,(offset) + "(sp)"}));
                    continue;
                    
                }

                // Handle 'use' operand
                if (spilledValues.contains(moveInstr.getUse())) {
                    String tempRegister = newTemp();
                    offset = instrToSpillOffset.getOrDefault(moveInstr.getUse(), 0);
                    updatedInstructions.add(new Instr.Oper(null, "ld", new String[]{tempRegister, (offset) + "(sp)"}));
                    moveInstr.setOperation(moveInstr.getDef(), tempRegister);
                    updatedInstructions.add(moveInstr);
                }
               
            } else {
                updatedInstructions.add(instr);
            }
        }
        return updatedInstructions;
    }
private List<Instr> updateInstructionsWithRegisters(List<Instr> instructions, Map<String, String> tempToReg, LIN.CodeChunk codeChunk) {
    String fpTemp = codeChunk.frame.FP.toString();
    String rvTemp = codeChunk.frame.RV.toString();
    List<Instr> updatedInstructions = new ArrayList<>();

    for (Instr instr : instructions) {
        if (instr instanceof Instr.Oper operInstr) {
            List<String> updatedUse = new ArrayList<>();
            String def = operInstr.getDef();
            String updateDef = operInstr.getDef();

            if (tempToReg.containsKey(def)) {
                updateDef = tempToReg.get(def);
            } else {
                if (def != null) {
                    if (def.equals(fpTemp)) {
                        updateDef = "s0"; // Assign FP to s0
                    } else if (def.equals(rvTemp)) {
                        updateDef = "a0"; // Assign RV to a0
                    } else {
                        System.out.println("Warning: No register mapping found for def: " + def);
                    }
                }
            }

            for (String use : operInstr.getUse()) {
                if (tempToReg.containsKey(use)) {
                    updatedUse.add(tempToReg.get(use));
                } else {
                    if (use.equals(fpTemp)) {
                        updatedUse.add("s0"); // Assign FP to s0
                    } else if (use.equals(rvTemp)) {
                        updatedUse.add("a0"); // Assign RV to a0
                    } else {
                        System.out.println("Warning: No register mapping found for use: " + use);
                    }
                }
            }

            operInstr.setOperation(updateDef, updatedUse.toArray(new String[0]));
            updatedInstructions.add(operInstr);

        } else if (instr instanceof Instr.Move moveInstr) {
            String def = moveInstr.getDef();
            String use = moveInstr.getUse();

            // Check if the instruction is `mv ret t1`
            if (def.equals(rvTemp) && tempToReg.containsKey(use)) {
                // Replace `mv ret t1` with `sw t1, 0(s0)`
                String mappedUse = tempToReg.get(use);
                updatedInstructions.add(new Instr.Oper(null, "sd", new String[]{mappedUse, "0(s0)"}));
            } else {
                // Handle `def`
                if (tempToReg.containsKey(def)) {
                    def = tempToReg.get(def);
                } else {
                    if (def.equals(fpTemp)) {
                        def = "s0"; // Assign FP to s0
                    } else if (def.equals(rvTemp)) {
                        def = "a0"; // Assign RV to a0
                    } else {
                        System.out.println("Warning: No register mapping found for def: " + def);
                    }
                }

                // Handle `use`
                if (tempToReg.containsKey(use)) {
                    use = tempToReg.get(use);
                } else {
                    if (use.equals(fpTemp)) {
                        use = "s0"; // Assign FP to s0
                    } else if (use.equals(rvTemp)) {
                        use = "a0"; // Assign RV to a0
                    } else {
                        System.out.println("Warning: No register mapping found for use: " + use);
                    }
                }

                moveInstr.setOperation(def, use);
                updatedInstructions.add(moveInstr);
            }
        } else {
            updatedInstructions.add(instr);
        }
    }

    return updatedInstructions;
}
    private String newTemp() {
        // Generate a new temporary variable (this is a placeholder, implement as needed)
        return new MEM.Temp().toString();
        
    }
     public void setTempToRegMappingForChunk(LIN.CodeChunk codeChunk, Map<String, String> tempToReg) {
        chunkToTempToReg.put(codeChunk, tempToReg);
    }

    // Method to get the temp-to-register mapping for a specific CodeChunk
    public Map<String, String> getTempToRegMappingForChunk(LIN.CodeChunk codeChunk) {
        return chunkToTempToReg.getOrDefault(codeChunk, Collections.emptyMap());
    }
    public Map<LIN.CodeChunk, Long> getSpillOffsetsMax() {
        return spilloffsets_max;
    }

}
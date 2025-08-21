package compiler.phase.livean;

import java.util.*;
import compiler.phase.asmgen.Instr;
import compiler.phase.imclin.LIN;

public class InstrLiveness {
    private final Instr instr; // The instruction
    private final LIN.CodeChunk codeChunk; // The CodeChunk associated with the instruction
    private Set<String> in; // The set of variables live before the instruction
    private Set<String> out; // The set of variables live after the instruction

    // Constructor
    public InstrLiveness(Instr instr, LIN.CodeChunk codeChunk) {
        this.instr = instr;
        this.codeChunk = codeChunk;
        this.in = new HashSet<>();
        this.out = new HashSet<>();
    }

    // Get the instruction
    public Instr getInstr() {
        return instr;
    }

    // Get the associated CodeChunk
    public LIN.CodeChunk getCodeChunk() {
        return codeChunk;
    }

    // Get the in set
    public Set<String> getIn() {
        return in;
    }

    // Get the out set
    public Set<String> getOut() {
        return out;
    }

    // Add variables to the in set
    public void addToIn(String var) {
        in.add(var);
    }

    // Add variables to the out set
    public void addToOut(String var) {
        out.add(var);
    }

    // Set the in set
    public void setIn(Set<String> in) {
        this.in = in;
    }

    // Set the out set
    public void setOut(Set<String> out) {
        this.out = out;
    }

    // Print the liveness information
    public void printLiveness() {
       
        instr.printDefAndUse();
        //System.out.println("  CodeChunk: " + codeChunk.frame.label.name);
        System.out.print("  In: " + in);
        System.out.println("  Out: " + out);
    }
}
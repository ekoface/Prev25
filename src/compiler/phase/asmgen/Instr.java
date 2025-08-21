package compiler.phase.asmgen;

import java.util.*;

public abstract class Instr {
    protected String instr;

    // Constructor that allows an instruction to be created with or without labels.
    public Instr(String instr) {
        this.instr = instr;
    }

    // Return the instruction as a string.
    @Override
    public String toString() {
        return instr;
    }

    /**
     * Represents a label in the assembly code.
     */
    public static class Label extends Instr {
        public Label(String label) {
            super(label + ":");
        }
        @Override
        public String toString() {
            return instr; // Remove the colon
        }
    }

    /**
     * Represents an operation in the assembly code.
     * It can optionally take a label (or labels) before the operation.
     */
    public static class Oper extends Instr {
        private String label;
        public  String operation; // The operation (e.g., "add", "sub", etc.)
        private String def; // The destination operand (defined by the instruction)
        private List<String> use; // The source operands (used by the instruction)

        // Constructor to create an operation with labels (optional) and operands
        public Oper(String label, String operation, String... operands) {
            super(label != null
                    ? label + "\n" + operation + " " + String.join(", ", operands)
                    : operation + " " + String.join(", ", operands));

            // Determine the def and use operands
            this.label = label; // The label (if any)
            this.operation = operation; // The operation (e.g., "add", "sub", etc.)
            if (operands.length > 0) {
                if (!(operation.equals("j") || operation.equals("jal") || operation.equals("jalr"))) {
                    this.def = isTemp(operands[0]) ? operands[0] : null; // Only set def if it's a temp
                } else {
                    this.def = null; // No destination operand for jump instructions
                }

                switch (operation) {
                    case "addi":
                    case "andi":
                    case "ori":
                    case "xori":
                    case "slli":
                    case "srli":
                    case "sltiu":
                        this.use = filterTemps(operands, 1); // Filter temps starting from the second operand
                        break;
                    
                    case "li":
                    case "lui":
                        this.use = List.of(); // No source operands
                        break;
                    case "ld":
                    case "lb":
                        // first and second operands 
                      List<String> temps = filterTemps(operands, 0);
                        if (temps.size() > 1) {
                            this.use = List.of(temps.get(1)); // Get the second operand as a single-element list
                        } else {
                            this.use = List.of(); // No second operand, set to an empty list
                        }
                        this.def = filterTemps(operands, 0).get(0); // First operand is the destination
                        break;
                         // No destination operand
                    case "sb":
                    case "sd":
                        //def = null; // No destination operand
                        this.def = null;
                        this.use = filterTemps(operands, 0); // Filter temps starting from the first operand
                        break;
                    case "beq":
                    case "bne":
                        this.def = null;
                        this.use = filterTemps(operands, 0); // Filter temps starting from the first operand
                        break;
                    case "slti":
                    
                    default:
                        this.use = filterTemps(operands, 1); // Filter temps starting from the second operand
                }
            } else {
                this.def = null; // No destination operand
                this.use = List.of(); // No source operands
            }
        }
        // Constructor to create an operation without a label
        public Oper(String operation, String... operands) {
            this(null, operation, operands); // Use null label if no label is provided
        }

        // Get the destination operand
        public String getDef() {
            return def;
        }
        
        

        // Get the source operands
        public List<String> getUse() {
            return use;
        }
        public void setUse(List<String> use) {
            this.use = use;
        }
        public void setDef(String def) {
            this.def = def;
        }
        public String[] parseInstr() {
            String instr = this.instr.trim();  // Remove leading/trailing spaces

            // Split into opcode and the rest (limit = 2 to avoid breaking operand contents)
            String[] parts = instr.split("\\s+", 2);

            String opcode = parts[0]; // The first token is always the opcode

            if (parts.length == 1) {
                // No operands (e.g., "nop")
                return new String[]{opcode};
            }

            // Split operands by comma and optional spaces
            String[] rawOperands = parts[1].split(",\\s*");

            // Merge opcode and operands into one array
            String[] result = new String[rawOperands.length + 1];
            result[0] = opcode;
            System.arraycopy(rawOperands, 0, result, 1, rawOperands.length);

            return result;
        }

        public void setOperation(String def, String... operands) {
            
            String[] old_instr = this.parseInstr();
            // print old_instr
            //System.out.println("Old instruction: " + this.instr + " operands "+ Arrays.toString(operands));
           
            StringBuilder new_instr = new StringBuilder();
            switch (old_instr[0]) {
                case "addi":
                case "andi":
                case "ori":
                case "xori":
                case "slli":
                case "srli":
                case "sltiu":
                    new_instr.append(old_instr[0]).append(" ").append(def).append(", ").append(operands[0]);
                    for (int i = 3; i < old_instr.length; i++) {
                        new_instr.append(", ").append(old_instr[i]);
                    }
                    
                    break;
                
                case "li":
                case "lui":
                    new_instr.append(old_instr[0]).append(" ").append(def);
                    for (int i = 2; i < old_instr.length; i++) {
                        new_instr.append(", ").append(old_instr[i]);
                    }
                    break;
                case "lb":
                case "ld":
                    //System.out.println("Old instruction: " + this.instr);
                    //System.out.println("operands: " + Arrays.toString(operands));
                    new_instr.append(old_instr[0]); // added the operation
                    String offset = old_instr[2]; // "0(T14)"
                    int parenIndex = offset.indexOf('(');
                    offset = offset.substring(0, parenIndex);  // "122"
                    //System.out.println("Offset: " + offset);
                    new_instr.append(" ").append(def); 
                    if (operands.length == 1) {
                        new_instr.append(", ").append(offset).append("(").append(operands[0]).append(")");
                    }
                    else {
                        new_instr.append(", ").append(old_instr[2]); // added the first operand
                    }
                    
                    break;
                case "sb":
                case "sd":
                   // System.out.println("Old instruction: " + this.instr);
                   // System.out.println("operands: " + Arrays.toString(operands));
                    new_instr.append(old_instr[0]); // sw / sb
                   // System.out.println("operands[0]" + operands[0] + " operands len" + operands.length);
                    //System.out.println("Old instruction: " + this.instr);
                    //System.out.println("operands: " + Arrays.toString(operands));
                    //System.out.println("old_instr: " + Arrays.toString(old_instr));
                    String offset_s = old_instr[2]; // "0(T14)"
                    int parenIndex_s = offset_s.indexOf('(');
                    offset_s = offset_s.substring(0, parenIndex_s);  // "122"
                    //System.out.println("Offset: " + offset_s);
                        if( operands.length > 1){
                            new_instr.append(" ").append(operands[0]).append(", ").append(offset_s).append("(").append(operands[1]).append(")"); // destination and first operand
                        }
                        else{
                            new_instr.append(" ").append(operands[0]).append(", ").append(old_instr[2]); // destination and first operand
                        }
                    //System.out.println("new_instr: " + new_instr);
                    break;
                case "jal":
                case "jalr":
                    new_instr.append(old_instr[0]).append(" ");
                    for (int i = 1; i < old_instr.length; i++) {
                        new_instr.append(old_instr[i]);
                    }
                    break;
                case "call":
                    new_instr.append(this);
                    break;
                case "beq":
                case "bne":
                    //System.out.println("Old instruction: " + this.instr);
                    //System.out.println("operands: " + Arrays.toString(operands));
                    //System.out.println("old_instr: " + Arrays.toString(old_instr));
                    new_instr.append(old_instr[0]).append(" ").append(operands[0]).append(", ").append("t0").append(", ").append(old_instr[3]);
                    break;
                default:
                    new_instr.append(old_instr[0]).append(" ").append(def);
                    for (int i = 0; i < operands.length; i++) {
                        new_instr.append(", ").append(operands[i]);
                    }
            }
            
            this.instr = new_instr.toString();
            this.operation = old_instr[0];
            this.def = def;
            this.use = new ArrayList<>();
            for (String operad : operands) {
                this.use.add(operad);
            }
        }
        // Get the label (if any)
        public String getLabel() {
            return label;
        }

        // Get the operation
        public String getOperation() {
            return operation;
        }
   }

    /**
     * Represents a move operation in the assembly code.
     */
    public static class Move extends Instr {
        String def;
        String use;

        public Move(String destination, String source) {
            super("mv " + destination + ", " + source);
            this.def = destination; // The destination operand
            this.use = source; // The source operand
        }

        // Get the destination operand
        public String getDef() {
            return def;
        }

        // Get the source operand
        public String getUse() {
            return use;
        }
        public void setUse(String use) {
            this.use = use;
        }

        // Add setter for 'def'
        public void setDef(String def) {
            this.def = def;
        }
        public void setOperation(String def, String use) {
            this.def = def;
            this.use = use;
            this.instr = "mv " + def + ", " + use;
        }
        
    }

    /**
     * Extracts the temporary register name from a memory operand.
     */
    public static String extractTemp(String input) {
        // Check for expected pattern
        int start = input.indexOf('(');
        int end = input.indexOf(')');

        if (start != -1 && end != -1 && start < end) {
            return input.substring(start + 1, end); // Extract between '(' and ')'
        } else {
            return input;
        }
    }

    /**
     * Prints the def and use operands for the instruction.
     */
    public void printDefAndUse() {
        //System.out.print("Instruction: " + this + ", ");
        if (this instanceof Oper oper) {
            oper.toString();
            //System.out.print("Instruction: " + oper + ", ");
            //System.out.print("  Def: " + oper.getDef() + ", ");
           // System.out.println("  Use: " + oper.getUse());
        } else if (this instanceof Move move) {
            move.toString();
            //System.out.print("Instruction: " + move + ", ");
            //System.out.print("  Def: " + move.getDef() + ", ");
            //System.out.println("  Use: " + move.getUse());
        } else if (this instanceof Label label) {
            //System.out.println("Label: " + label);
        } else {
            //System.out.println("Unknown instruction type: " + this);
        }
    }
        /**
     * Checks if the given operand is a temporary register (e.g., starts with "T").
     */
    private static boolean isTemp(String operand) {
        if (operand != null){
            if (operand.matches("T\\d+") || operand.matches("t\\d+")){
                return true;
            }
        }
        return operand != null && operand.matches("T\\d+") ;
    }

    /**
     * Filters the operands to include only temporary registers, starting from the given index.
     */
    private static List<String> filterTemps(String[] operands, int startIndex) {
        List<String> temps = new ArrayList<>();
        for (int i = startIndex; i < operands.length; i++) {
            String operand = operands[i];

            // If it's a memory access like "0(T14)", extract the temp
            if (operand.matches(".*\\(T\\d+\\)")) {
                int start = operand.indexOf('(');
                int end = operand.indexOf(')');
                if (start != -1 && end != -1) {
                    String temp = operand.substring(start + 1, end);
                    if (isTemp(temp)) {
                        temps.add(temp);
                    }
                }
            } else if (isTemp(operand)) {
                temps.add(operand);
            }
        }
        return temps;
    }
    private String getOperation() {
        if (this instanceof Oper oper) {
            return oper.operation;
        } else if (this instanceof Move move) {
            return "move";
        } else {
            return "";
        }
    }

    public static class Name extends Instr {
    private final String label; // The label name
    private final String directive; // The directive (e.g., ".word", ".byte", ".space")
    private final String value; // The value associated with the directive (e.g., "10")

    public Name(String label, String directive, String value) {
        super(label + ": " + directive + " " + value);
        this.label = label;
        this.directive = directive;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public String getDirective() {
        return directive;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return label + ": " + directive + " " + value;
    }
    }


}
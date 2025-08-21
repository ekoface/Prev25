package compiler.phase.asmgen;

import java.util.*;
import compiler.phase.imcgen.*;
import compiler.phase.memory.*;
import compiler.phase.imclin.LIN;
import compiler.phase.imcgen.IMC;

public class AsmGen {

    /** List of generated RISC-V instructions. */
    private  List<Instr> instructions = new ArrayList<>();

    /** Temp to register mapping (can be extended to register allocation later). */
    private final Map<MEM.Temp, String> tempRegMap = new HashMap<>();
    private final Map<LIN.CodeChunk, Instr> instr_map = new HashMap<>();

    private final Map<LIN.CodeChunk, List<Instr>> codeChunkToInstructions = new HashMap<>();

    public AsmGen(List<LIN.CodeChunk> codeChunks) {
        for (LIN.CodeChunk codeChunk : codeChunks) {
            instructions = new ArrayList<>();
           // System.out.println("Processing code chunk: " + codeChunk);
            processCodeChunk(codeChunk);

            // Store the codeChunk and its instructions in the map
            codeChunkToInstructions.put(codeChunk, new ArrayList<>(instructions));
        }
    }
    public List<Instr> getInstructionsForCodeChunk(LIN.CodeChunk codeChunk) {
        return codeChunkToInstructions.getOrDefault(codeChunk, Collections.emptyList());
    }
    public Map<LIN.CodeChunk, List<Instr>> getCodeChunkToInstructions() {
        return codeChunkToInstructions;
    }
    

    private void processCodeChunk(LIN.CodeChunk codeChunk) {
        //instructions.add(new Instr.Label(formatLabel(codeChunk.entryLabel)));
        for (IMC.Stmt stmt : codeChunk.stmts()) {
            translateStmt(stmt,codeChunk);
        }
        //instructions.add(new Instr.Label(formatLabel(codeChunk.exitLabel)));
    }

    private void translateStmt(IMC.Stmt stmt, LIN.CodeChunk codeChunk) {
        if (stmt instanceof IMC.LABEL labelStmt) {
            instructions.add(new Instr.Label(formatLabel(labelStmt.label)));
        } else if (stmt instanceof IMC.MOVE moveStmt) {
            translateMove(moveStmt, codeChunk);
        } else if (stmt instanceof IMC.CJUMP cjump) {
            translateCJump(cjump);
        } else if (stmt instanceof IMC.JUMP jump) {
            instructions.add(new Instr.Oper(null, "jal", new String[]{ formatLabel(jump.addr) }));
        } else if (stmt instanceof IMC.ESTMT estmt) {
            translateExpr(estmt.expr);  // for side-effects
        } else {
            throw new UnsupportedOperationException("Unsupported statement: " + stmt.getClass());
        }
    }

    private void translateMove(IMC.MOVE move, LIN.CodeChunk codeChunk) {
        String srcReg = translateExpr(move.src);

        if (move.dst instanceof IMC.TEMP dstTemp) {  
            String dstReg = formatTemp(dstTemp.temp);
            instructions.add(new Instr.Move(dstReg, srcReg));
            
        } 
        else if (move.dst instanceof IMC.MEM8 mem8) {
            String addr = translateExpr(mem8.addr);
            instructions.add(new Instr.Oper(null, "sd", new String[]{ srcReg, "0(" + addr + ")" }));
        } 
        else if (move.dst instanceof IMC.MEM1 mem1) {
            String addr = translateExpr(mem1.addr);
            instructions.add(new Instr.Oper(null, "sb", new String[]{ srcReg, "0(" + addr + ")" }));
        } 
        else {
            throw new UnsupportedOperationException("MOVE destination not supported: " + move.dst.getClass());
        }
    }

    private void translateCJump(IMC.CJUMP cjump) {
        String condReg = translateExpr(cjump.cond);
        String trueLabel = formatLabel(cjump.posAddr);
        String falseLabel = formatLabel(cjump.negAddr);
        instructions.add(new Instr.Oper(null, "beq",new String[]{ condReg, "0", trueLabel })); // Assuming T0 is zero register
        instructions.add(new Instr.Oper(null, "jal", new String[]{ falseLabel }));
        //System.out.println("Jumping to " + trueLabel + " if condition is true");
        //System.out.println("Jumping to " + falseLabel + " if condition is false");
        //System.out.println("**************");
    }

    private String translateExpr(IMC.Expr expr) {
        if (expr instanceof IMC.CONST constant) {
            String dst = newTemp();
            instructions.add(new Instr.Oper(null, "li", new String[]{ dst, Integer.toString((int) constant.value) }));
            return dst;

        } else if (expr instanceof IMC.TEMP tempExpr) {
            return formatTemp(tempExpr.temp);
        }
        else if (expr instanceof IMC.BINOP binop) {
        String left = translateExpr(binop.fstExpr); // Translate the left operand
        String dst = newTemp(); // Allocate a new temporary register for the result
        if (binop.sndExpr instanceof IMC.CONST constExpr) {
            int value = (int) constExpr.value;

            // Handle supported immediate operations
            switch (binop.oper) {
                case ADD -> instructions.add(new Instr.Oper(null, "addi", new String[]{ dst, left, Integer.toString(value) }));
                case SUB -> instructions.add(new Instr.Oper(null, "addi", new String[]{ dst, left, Integer.toString(-value) })); // Use negative value for subtraction
                case AND -> instructions.add(new Instr.Oper(null, "andi", new String[]{ dst, left, Integer.toString(value) }));
                case OR  -> instructions.add(new Instr.Oper(null, "ori", new String[]{ dst, left, Integer.toString(value) }));
                default -> {
                    // For unsupported immediate operations, translate the constant into a register
                    String right = newTemp();
                    instructions.add(new Instr.Oper(null, "li", new String[]{ right, Integer.toString(value) })); // Load constant into a register
                    String op = switch (binop.oper) {
                        case MUL -> "mul";
                        case DIV -> "div";
                        case MOD -> "rem";
                        case LTH ->{
                            String temp = newTemp();
                            instructions.add(new Instr.Oper(null, "slt", new String[]{ temp, left, right }));
                            instructions.add(new Instr.Oper(null, "xori", new String[]{ dst, temp, "1" })); // Negate result
                            yield null; // Skip adding another instruction
                        }
                        
                        case GTH -> {
                            String temp = newTemp();
                            instructions.add(new Instr.Oper(null, "slt", new String[]{ temp, right, left })); // Reverse operands
                            instructions.add(new Instr.Oper(null, "xori", new String[]{ dst, temp, "1" })); // Negate result
                            yield null; // Skip adding another instruction

                        }
                        case GEQ -> {
                            String temp = newTemp();
                            instructions.add(new Instr.Oper(null, "slt", new String[]{ temp, left, right }));
                            instructions.add(new Instr.Oper(null, "xori", new String[]{ dst, temp, "0" })); // Negate result
                            yield null; // Skip adding another instruction
                        }
                        case LEQ -> {
                            String temp = newTemp();
                            instructions.add(new Instr.Oper(null, "slt", new String[]{ temp, right, left })); // Reverse operands
                            instructions.add(new Instr.Oper(null, "xori", new String[]{ dst, temp, "0" })); // Negate result
                            yield null; // Skip adding another instruction
                        }
                        case EQU -> {
                            String temp = newTemp();
                            instructions.add(new Instr.Oper(null, "xor", new String[]{ temp, left, right })); // if equal, result is 0
                            instructions.add(new Instr.Oper(null, "sltiu", new String[]{ dst, temp, "1" })); // if result is 0, dst is 1
                            instructions.add(new Instr.Oper(null, "xori", new String[]{ dst, dst, "1" })); // Negate result
                            yield null; // Skip adding another instruction
                        }
                        case NEQ -> {
                            String temp = newTemp();
                            instructions.add(new Instr.Oper(null, "xor", new String[]{ temp, left, right }));
                            instructions.add(new Instr.Oper(null, "sltiu", new String[]{ dst, temp, "1" })); // Check if result is non-zero
                            yield null; // Skip adding another instruction
                        }
                        default -> throw new IllegalArgumentException("Unsupported BINOP: " + binop.oper +" (second one)");
                    };

                    if (op != null) {
                        instructions.add(new Instr.Oper(null, op, new String[]{ dst, left, right }));
                    }
                }
            }
        }
        else {
            // Right-hand side is not a constant
            String right = translateExpr(binop.sndExpr);
            //System.out.println("OP is " + binop.oper);
            String op = switch (binop.oper) {
                case ADD -> "add";
                case SUB -> "sub";
                case MUL -> "mul";
                case DIV -> "div";
                case MOD -> "rem";
                case OR  -> "or";
                case AND -> "and";
                case LTH ->{
                    String temp = newTemp();
                    instructions.add(new Instr.Oper(null, "slt", new String[]{ temp, left, right }));
                    instructions.add(new Instr.Oper(null, "xori", new String[]{ dst, temp, "1" })); // Negate result
                    yield null; // Skip adding another instruction
                }
                case GTH ->{
                    String temp = newTemp();
                    instructions.add(new Instr.Oper(null, "slt", new String[]{ temp, right, left })); // Reverse operands
                    instructions.add(new Instr.Oper(null, "xori", new String[]{ dst, temp, "1" })); // Negate result
                    yield null; // Skip adding another instruction
                }
                case GEQ -> {
                    String temp = newTemp();
                    instructions.add(new Instr.Oper(null, "slt", new String[]{ temp, left, right }));
                    instructions.add(new Instr.Oper(null, "xori", new String[]{ dst, temp, "0" })); // Negate result
                    yield null; // Skip adding another instruction
                }
                 case LEQ -> {
                    String temp = newTemp();
                    instructions.add(new Instr.Oper(null, "slt", new String[]{ temp, right, left })); // Reverse operands
                    instructions.add(new Instr.Oper(null, "xori", new String[]{ dst, temp, "0" })); // Negate result
                    yield null; // Skip adding another instruction
                }
                case EQU -> {
                    String temp = newTemp();
                    instructions.add(new Instr.Oper(null, "xor", new String[]{ temp, left, right })); // if equal, result is 0
                    instructions.add(new Instr.Oper(null, "sltiu", new String[]{ dst, temp, "1" })); // if result is 0, dst is 1
                    instructions.add(new Instr.Oper(null, "xori", new String[]{ dst, dst, "1" })); // Negate result
                    yield null; // Skip adding another instruction
                }
                case NEQ -> {
                    String temp = newTemp();
                    instructions.add(new Instr.Oper(null, "xor", new String[]{ temp, left, right }));
                    instructions.add(new Instr.Oper(null, "sltiu", new String[]{ dst,temp, "0" })); // Check if result is non-zero
                    yield null; // Skip adding another instruction
                }
                    default -> throw new IllegalArgumentException("Unsupported BINOP: " + binop.oper + " not a constant");
                };

                if (op != null) {
                    instructions.add(new Instr.Oper(null, op, new String[]{ dst, left, right }));
                }
            }

            return dst; // Return the destination register holding the result
        }      
          else if (expr instanceof IMC.NAME name) {
            String dst = newTemp();
            String label = formatLabel(name.label);
            instructions.add(new Instr.Oper(null, "lui", new String[]{ dst, "%hi(" + label + ")" }));
            instructions.add(new Instr.Oper(null, "addi", new String[]{ dst, dst, "%lo(" + label + ")" }));
            return dst;

        } else if (expr instanceof IMC.CALL call) {
            for (int i = 0; i < call.args.size(); i++) {
                IMC.Expr arg = call.args.get(i);
                String argReg = translateExpr(arg);
                Long offset =  call.offs.get(i);
                instructions.add(new Instr.Oper(null, "sd", new String[]{ argReg, offset + "(sp)" })); // Store argument in stack
               
                
            }
        
            instructions.add(new Instr.Oper(null, "call", new String[]{ formatExpr(call.addr) }));
            // get the return value from sp + 0
            String dst = newTemp();
            instructions.add(new Instr.Oper(null, "ld", new String[]{ dst.toString(), "0(sp)" })); // Load return value into a0
            return dst;

        } else if (expr instanceof IMC.MEM8 mem8) {
            String addr = translateExpr(mem8.addr);
            String dst = newTemp();
            instructions.add(new Instr.Oper(null, "ld", new String[]{ dst, "0(" + addr + ")" }));
            return dst;
        } else if (expr instanceof IMC.MEM1 mem1) {
            String addr = translateExpr(mem1.addr);
            String dst = newTemp();
            instructions.add(new Instr.Oper(null, "lb", new String[]{ dst, "0(" + addr + ")" }));
            return dst;

        }
        else if (expr instanceof IMC.UNOP unop) {
        // Handle unary operations
            String src = translateExpr(unop.subExpr);
            String dst = newTemp();

            String op = switch (unop.oper) {
                case NEG -> "neg";  // Unary negation
                case NOT -> "seqz"; // Logical negation
                default -> throw new IllegalArgumentException("Unsupported UNOP: " + unop.oper);
            };

            instructions.add(new Instr.Oper(null, op, new String[]{ dst, src }));
            return dst;
        }
        else {
            throw new UnsupportedOperationException("Unsupported expression: " + expr.getClass());
        }
    }

    private String formatTemp(MEM.Temp temp) {
        return tempRegMap.computeIfAbsent(temp, t -> t.toString());


    }

    private String formatLabel(Object label) {
        if (label instanceof MEM.Label memLabel) {
            return memLabel.name;
        }
        if (label instanceof IMC.LABEL imcLabel) {
            return imcLabel.label.name;
        }
        if (label instanceof IMC.NAME name) {
            return name.label.name;
        }
        return label != null ? label.toString() : "null";
    }

    private String formatExpr(IMC.Expr expr) {
        if (expr instanceof IMC.NAME name) {
            return formatLabel(name.label);
        } else if (expr instanceof IMC.TEMP temp) {
            return formatTemp(temp.temp);
        } else if (expr instanceof IMC.CONST constant) {
            return Integer.toString((int) constant.value);
        } else {
            throw new UnsupportedOperationException("Unsupported expression type: " + expr.getClass());
        }
    }

    private String newTemp() {
        MEM.Temp temp = new MEM.Temp(); // Generate a new MEM.Temp
        return formatTemp(temp);        // Format it into a string (e.g., "t0", "t1", etc.)
    }

    public List<Instr> getInstructions() {
        return instructions;
    }
}
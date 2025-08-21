package compiler.phase.imcgen;

import compiler.phase.abstr.*;
import compiler.phase.memory.*;
import compiler.phase.imcgen.IMC.*;
import compiler.common.report.*;
import compiler.phase.seman.*;
import java.util.*;
/**
 * Intermediate code generator.
 * 
 * @author bostjan.slivnik@fri.uni-lj.si
 */
public class ImcGenerator implements AST.FullVisitor<Object, Object> {

    private AST.DefFunDefn currentFunction;
    private MEM.Frame currentFrame;
    private IMC.NAME currentName;
    private IMC.NAME returnName;
    private MEM.Label currentLabel;
    private IMC.TEMP returnTemp;
    private IMC.TEMP currentTemp;

    public static IMC.Expr unwrapMem8(IMC.Expr expr) {
    if (expr instanceof IMC.MEM8) {
        // If the expression is a MEM8, return the address inside it
        return ((IMC.MEM8) expr).addr;
    }
    if (expr instanceof IMC.MEM1){
        return ((IMC.MEM1) expr).addr;
    }
    // Otherwise, return the expression as is
    return expr;
}

    @Override
public Object visit(AST.ReturnStmt returnStmt, Object arg) {
    //System.err.println("Generating intermediate code for return statement");

    // Generate intermediate code for the return expression
    Expr returnExpr = null;
    if (returnStmt.retExpr != null) {
        returnExpr = (Expr) returnStmt.retExpr.accept(this, arg);
    }

    // Check if the return expression was successfully generated
    if (returnExpr == null) {
        throw new Report.Error("Failed to generate intermediate code for return expression.");
    }

    // Create a RETURN intermediate code
    // STMTS MOCE TEMP of nameconst of name and jump and where
    // make move temp and const 
    
    IMC.MOVE move = new IMC.MOVE(returnTemp, returnExpr);
    IMC.JUMP jump = new IMC.JUMP(returnName);
    IMC.STMTS stmts = new IMC.STMTS(new Vector<>(Arrays.asList(move, jump)));
    ImcGen.stmt.put(returnStmt, stmts);
    return stmts;
}

    @Override
    public Object visit(AST.AtomType atomType, Object arg) {
       // System.err.println("Visiting AtomType: " + atomType.type);    
        return new IMC.CONST(1L);
    }
@Override
    public Object visit(AST.AtomExpr atomExpr, Object arg) {
        // Če gre za konstanto tipa INT:
        
        if (atomExpr.type == AST.AtomExpr.Type.INT) {
            long value = Long.parseLong(atomExpr.value);
            IMC.Expr c = new CONST(value);
            ImcGen.expr.put(atomExpr, c);
            return c;
        }
        else if (atomExpr.type == AST.AtomExpr.Type.CHAR) {
            String raw = atomExpr.value;
                char decoded;
                if (raw.length() == 3) {
                    decoded = raw.charAt(1);
                } else if (raw.equals("'\\''")) {
                    decoded = '\'';
                } else if (raw.equals("'\\\\'")) {
                    decoded = '\\';
                } else if (raw.startsWith("'\\0x")) {
                    String hex = raw.substring(4, 6);
                    decoded = (char) Integer.parseInt(hex, 16);
                } else {
                    decoded = '?';
                }
                ImcGen.expr.put(atomExpr,new CONST((int) decoded));
                return new CONST((int) decoded);
        }
        else if (atomExpr.type == AST.AtomExpr.Type.BOOL) {
            long val = atomExpr.value.equals("true") ? 1 : 0;
            ImcGen.expr.put(atomExpr, new CONST(val));
            return new CONST(val);
        }
        else if (atomExpr.type == AST.AtomExpr.Type.STR) {
            // get label from Memory
            MEM.AbsAccess absAccess = Memory.strings.get(atomExpr);
            if (absAccess == null) {
                throw new Report.Error("String '" + atomExpr.value + "' not found in memory.");
            }
            //get label from access
            IMC.Expr name = new NAME(absAccess.label);
            //System.err.println("String label: " + name);
            ImcGen.expr.put(atomExpr, name);
            return name;
        }
        else if(atomExpr.type == AST.AtomExpr.Type.PTR){
            ImcGen.expr.put(atomExpr, new CONST(0));
            return new CONST(0);
        }
        return null;
    }


    @Override
public Object visit(AST.DefFunDefn defFunDefn, Object arg) {
    //System.err.println("Generating intermediate code for function definition: " + defFunDefn.name);

    // Generate entry and exit labels for the function
    MEM.Label entryLabel = new MEM.Label();
    MEM.Label exitLabel = new MEM.Label();

    // Store the labels in the semantic attributes
    ImcGen.entryLabel.put(defFunDefn, entryLabel);
    ImcGen.exitLabel.put(defFunDefn, exitLabel);
    MEM.Frame cFrame = currentFrame;
    IMC.NAME rName = returnName;
    IMC.TEMP rTemp = returnTemp;
    IMC.TEMP cTemp = currentTemp;

    currentFunction = defFunDefn;
    currentFrame = Memory.frames.get(defFunDefn);
    returnName = new IMC.NAME(exitLabel);
    //get new from FP of frame
    returnTemp = new IMC.TEMP(currentFrame.RV);
    currentTemp = new IMC.TEMP(currentFrame.FP);
    
    // Generate intermediate code for the function body
    if (defFunDefn.stmts != null) {
        for (AST.Stmt stmt : defFunDefn.stmts) {
            stmt.accept(this, arg);
        }

    }

    // Log the generated labels
   // System.err.println("Generated entry label: " + entryLabel.name);
   // System.err.println("Generated exit label: " + exitLabel.name);
    currentFrame = cFrame;
    returnName = rName;
    returnTemp = rTemp;
    currentTemp = cTemp;
    return null;
}


@Override
public Object visit(AST.PfxExpr pfxExpr, Object arg) {
    //System.err.println("Generating intermediate code for UnaryExpr: " + pfxExpr.oper);
    // Generate intermediate code for the operand (E)
    IMC.Expr operand = (IMC.Expr) pfxExpr.subExpr.accept(this, arg);
    IMC.UNOP.Oper operation;
    switch (pfxExpr.oper) {
        case NOT:
            operation = IMC.UNOP.Oper.NOT;
            break;
        case SUB:
            operation = IMC.UNOP.Oper.NEG;
            break;
        case ADD:
            ImcGen.expr.put(pfxExpr, operand); 
            return operand; 
        case PTR:
            operand = unwrapMem8(operand);
            ImcGen.expr.put(pfxExpr, operand);
            return operand;
        default:
            throw new Report.Error("Unsupported unary operator: " + pfxExpr.oper);
    }

    // Create the UNOP intermediate code
    IMC.UNOP unop = new IMC.UNOP(operation, operand);

    // Store the generated expression in the semantic attribute
    ImcGen.expr.put(pfxExpr, unop);

   // System.err.println("Generated intermediate code for UnaryExpr: " + unop);
    return unop;
}
@Override
public Object visit(AST.BinExpr binExpr, Object arg){
    IMC.Expr leftExpr = (IMC.Expr) binExpr.fstExpr.accept(this, arg);
    IMC.Expr rightExpr = (IMC.Expr) binExpr.sndExpr.accept(this, arg);
    IMC.BINOP.Oper operation;
    switch(binExpr.oper){
        case ADD:
            operation = IMC.BINOP.Oper.ADD;
            break;
        case SUB:
            operation = IMC.BINOP.Oper.SUB;
            break;
        case MUL:
            operation = IMC.BINOP.Oper.MUL;
            break;
        case DIV:
            operation = IMC.BINOP.Oper.DIV;
            break;
        case MOD:
            operation = IMC.BINOP.Oper.MOD;
            break;
        case AND:
            operation = IMC.BINOP.Oper.AND;
            break;
        case OR:    
            operation = IMC.BINOP.Oper.OR;
            break;
        case EQU:
            operation = IMC.BINOP.Oper.EQU;
            break;
        case NEQ:
            operation = IMC.BINOP.Oper.NEQ;
            break;
        case LTH:
            operation = IMC.BINOP.Oper.LTH;
            break;
        case GTH:
            operation = IMC.BINOP.Oper.GTH;
            break;
        case LEQ:
            operation = IMC.BINOP.Oper.LEQ;
            break;
        case GEQ:
            operation = IMC.BINOP.Oper.GEQ;
            break;  
        default:
            throw new Report.Error("Unsupported binary operator: " + binExpr.oper);
    }
        IMC.BINOP binop = new IMC.BINOP(operation, leftExpr, rightExpr);
        ImcGen.expr.put(binExpr, binop);
       // System.err.println("Generated intermediate code for BinExpr: " + binop);
        return binop;
}
@Override
public Object visit(AST.AssignStmt assignStmt, Object arg) {
   // System.err.println("Generating intermediate code for assignment statement");

    // Generate intermediate code for the left-hand side (LHS) and right-hand side (RHS)
    IMC.Expr lhs = (IMC.Expr) assignStmt.srcExpr.accept(this, arg);
    IMC.Expr rhs = (IMC.Expr) assignStmt.dstExpr.accept(this, arg);


    // Create a MOVE intermediate code
   // System.err.println("Creating MOVE intermediate code" + " with " + lhs + " and " + rhs); 
    IMC.MOVE move = new IMC.MOVE(rhs, lhs);

    // Store the generated statement in the semantic attribute
    ImcGen.stmt.put(assignStmt, move);

   // System.err.println("intermediate code for assignment statement!!!: " + move);

    return move;
}


@Override
public Object visit(AST.NameExpr nameExpr, Object arg) {
    //System.err.println("Generating intermediate code for NameExpr: " + nameExpr.name);

    // Retrieve the declaration associated with the NameExpr
    AST.Defn decl = SemAn.defAt.get(nameExpr);
    //System.err.println("Declaration found: " + decl);
    if (decl == null) {
        throw new Report.Error("Declaration for variable or function '" + nameExpr.name + "' not found.");
    }

    // Handle external functions
    if (decl instanceof AST.ExtFunDefn) {
        MEM.Label label = new MEM.Label(nameExpr.name); // Use the function's name as the label
        IMC.NAME nameExprImc = new IMC.NAME(label);
        ImcGen.expr.put(nameExpr, nameExprImc);
       // System.err.println("Generated intermediate code for external function: " + nameExprImc);
        return nameExprImc;
    }

    // Handle function definitions
    if (decl instanceof AST.DefFunDefn) {
        MEM.Frame referencedFrame = Memory.frames.get(decl);
        if (referencedFrame == null) {
            throw new Report.Error("Frame for function '" + nameExpr.name + "' not found.");
        }
        // Generate a MEM8(NAME) for the function's label
        IMC.NAME functionLabel = new IMC.NAME(referencedFrame.label);
            ImcGen.expr.put(nameExpr, functionLabel);
           // System.err.println("Generated intermediate code for non-nested function: " + functionLabel);
            return functionLabel;
        
    }

    // Handle variable accesses
    MEM.Access access = Memory.accesses.get(decl);
    if (access == null) {
        throw new Report.Error("Variable '" + nameExpr.name + "' is not defined or memory was not allocated.");
    }

    IMC.Expr result;
    if (access instanceof MEM.AbsAccess absAccess) {
        // Global variable: Use MEM8(NAME(_name))
        if (absAccess.size == 1L) {
            result = new IMC.MEM1(new IMC.NAME(absAccess.label));
        } else {
            result = new IMC.MEM8(new IMC.NAME(absAccess.label));
        }
     } else if (access instanceof MEM.RelAccess relAccess) {
        // Local variable: Traverse the static chain if necessary
        IMC.Expr framePointer = currentTemp; // Start with the current frame's FP

        // Check if the variable is in a parent frame (nested function scenario)
       // System.err.println("Current frame depth: " + currentFrame.depth);
      //  System.err.println("Relative access depth: " + relAccess.depth);
        if (currentFrame.depth+1 > relAccess.depth) {
            // Traverse the static chain to reach the correct frame
            for (long i = currentFrame.depth +1; i > relAccess.depth; i--) {
                framePointer = new IMC.MEM8(framePointer); // Dereference the FP
            }
            
        
        }
       
        // Compute the address of the variable
        IMC.Expr offset = new IMC.CONST(relAccess.offset);
        IMC.BINOP binop = new IMC.BINOP(IMC.BINOP.Oper.ADD, framePointer, offset);

        // Wrap the address in MEM8 or MEM1 based on the size of the variable
        if (relAccess.size == 1L) {
            result = new IMC.MEM1(binop);
        } else {
            result = new IMC.MEM8(binop);
        }
        ImcGen.expr.put(nameExpr, result);
       // System.err.println("Generated intermediate code for local variable: " + result);
    } else {
        throw new Report.Error("Unsupported access type for variable '" + nameExpr.name + "'.");
    }

    ImcGen.expr.put(nameExpr, result);
    return result;
}

@Override
public Object visit(AST.SfxExpr sfxExpr, Object arg) {
      sfxExpr.subExpr.accept(this, arg);
        IMC.Expr baseImc = ImcGen.expr.get(sfxExpr.subExpr);

        // 2) Obdelamo operator
        switch (sfxExpr.oper) {
        case PTR: 
            {
                TYP.Type t = SemAn.ofType.get(sfxExpr);
                IMC.Expr read;
                long size = t.accept(new SizeEvaluator(), null);
                if (size == 1) {
                    read = new MEM1(baseImc);
                } else {
                    read = new MEM8(baseImc);
                }
                ImcGen.expr.put(sfxExpr, read);
                return read;
            }
            
        }
        return null;
    }
@Override
public Object visit(AST.ArrExpr arrExpr, Object arg) {
    // 1) Process arrExpr.arrExpr and arrExpr.idx
    arrExpr.arrExpr.accept(this, arg);
    arrExpr.idx.accept(this, arg);

    // 2) Retrieve the base address of the array
    IMC.Expr arrBase = ImcGen.expr.get(arrExpr.arrExpr);
    arrBase = unwrapMem8(arrBase); // Use your helper method to unwrap MEM8 if necessary

    // 3) Retrieve the value of the index expression
    IMC.Expr idxVal = ImcGen.expr.get(arrExpr.idx);

    // 4) Determine the type of the array and the size of its elements
    TYP.Type baseType = SemAn.ofType.get(arrExpr.arrExpr);
    long elemSize = 8; // Default element size

    if (baseType instanceof TYP.ArrType arrType) {
        // Use SizeEvaluator to determine the size of the array element
        TYP.Type elemType = arrType.elemType;
        elemSize = elemType.accept(new SizeEvaluator(), null);
    } else {
        throw new Report.Error(arrExpr, "Expression is not of array type.");
    }

    // 5) Compute the address of the array element
    IMC.Expr address = new IMC.BINOP(
        IMC.BINOP.Oper.ADD,
        arrBase,
        new IMC.BINOP(
            IMC.BINOP.Oper.MUL,
            idxVal,
            new IMC.CONST(elemSize)
        )
    );

    // 6) Access the value at the computed address
    IMC.Expr mem;
    if (elemSize == 1) {
        mem = new IMC.MEM1(address);
    } else {
        mem = new IMC.MEM8(address);
    }

    // 7) Store the resulting expression in ImcGen.expr
    //System.err.println("Generated intermediate code for array expression: " + mem);
    ImcGen.expr.put(arrExpr, mem);

    return mem;
}
// @Override
// public Object visit(AST.ArrExpr arrExpr, Object arg) {
//     System.err.println("Generating intermediate code for array expression");

//     // Generate intermediate code for the array base address (NameExpr)
//     IMC.Expr arrayBase = (IMC.Expr) arrExpr.arrExpr.accept(this, arg);

//     // Generate intermediate code for the index expression (AtomExpr)
//     IMC.Expr indexExpr = (IMC.Expr) arrExpr.idx.accept(this, arg);

//     // Retrieve the array's memory access information
//     AST.Defn arrayDefn = SemAn.defAt.get(arrExpr.arrExpr);
//     if (arrayDefn == null) {
//         throw new Report.Error(arrExpr, "Array definition not found.");
//     }

//     MEM.Access access = Memory.accesses.get(arrayDefn);
//     if (access == null) {
//         throw new Report.Error(arrExpr, "Memory access for array not found.");
//     }

//     // Determine the size of the array elements
//     long elementSize;
//     if (access instanceof MEM.RelAccess relAccess) {
//         elementSize = relAccess.size; // Use the size from the memory access
//     } else if (access instanceof MEM.AbsAccess absAccess) {
//         elementSize = absAccess.size; // Use the size from the memory access
//     } else {
//         throw new Report.Error(arrExpr, "Unsupported access type for array.");
//     }

//     // Multiply the index by the size of the array elements
//     IMC.BINOP indexOffset = new IMC.BINOP(IMC.BINOP.Oper.MUL, indexExpr, new IMC.CONST(elementSize));

//     // Add the offset to the base address
//     IMC.BINOP elementAddress = new IMC.BINOP(IMC.BINOP.Oper.ADD, arrayBase, indexOffset);

//     // Store the generated expression in the semantic attribute
//     ImcGen.expr.put(arrExpr, elementAddress);

//     System.err.println("Generated intermediate code for array expression: " + elementAddress);
//     return elementAddress;
// }
@Override
public Object visit(AST.CastExpr castExpr, Object arg) {
    //System.err.println("Generating intermediate code for cast expression");

    // Generate intermediate code for the expression
    IMC.Expr expr = (IMC.Expr) castExpr.expr.accept(this, arg);
    IMC.BINOP.Oper operation;
    //System.err.println("Cast expression type: " + castExpr.type);
    if (castExpr.type instanceof AST.AtomType){
        AST.AtomType atomType = (AST.AtomType) castExpr.type;
        if (atomType.type == AST.AtomType.Type.INT){
            // Cast to int: expr
            ImcGen.expr.put(castExpr, expr);
            return expr;
        }
        else if (atomType.type == AST.AtomType.Type.BOOL){
            // Cast to bool: expr
            operation = IMC.BINOP.Oper.MOD;
            IMC.BINOP binop = new IMC.BINOP(operation, expr, new IMC.CONST(2L));
            ImcGen.expr.put(castExpr, binop);
            return binop;
        }
        else if (atomType.type == AST.AtomType.Type.CHAR){
            // Cast to char: expr
            operation = IMC.BINOP.Oper.MOD;
            IMC.BINOP binopChar = new IMC.BINOP(operation, expr, new IMC.CONST(256L));
            ImcGen.expr.put(castExpr, binopChar);
            return binopChar;
        }
    }
    else{
        ImcGen.expr.put(castExpr, expr);
        return expr;
    }
    
    return null;
}

@Override
public Object visit(AST.ExprStmt exprStmt, Object arg) {
  //  System.err.println("Generating intermediate code for expression statement");

    // Generate intermediate code for the expression
    IMC.Expr expr = (IMC.Expr) exprStmt.expr.accept(this, arg);

    // Wrap the expression in an ESTMT (Expression Statement)
    IMC.ESTMT eStmt = new IMC.ESTMT(expr);

    // Store the generated statement in the semantic attribute
    ImcGen.stmt.put(exprStmt, eStmt);

   // System.err.println("Generated intermediate code for expression statement: " + eStmt);

    return eStmt;
}

@Override
public Object visit(AST.IfThenStmt ifThenStmt, Object arg) {
        ifThenStmt.condExpr.accept(this, arg);
        IMC.Expr condImc = ImcGen.expr.get(ifThenStmt.condExpr);

        // 2) Pripravimo labele
        MEM.Label thenLabel = new MEM.Label();
        MEM.Label endLabel  = new MEM.Label();

        // 3) Naredimo CJUMP(cond, thenLabel, endLabel)
        IMC.CJUMP cjump = new IMC.CJUMP(condImc, new IMC.NAME(thenLabel), new IMC.NAME(endLabel));

        // 4) Zložimo "thenStmts" v en sam IMC.Stmt (ali IMC.STMTS).
        Vector<IMC.Stmt> thenImcList = new Vector<>();
        for (AST.Stmt s : ifThenStmt.thenStmt) {
            s.accept(this, arg);                  
            thenImcList.add(ImcGen.stmt.get(s));  
        }
        IMC.Stmt thenBody = new IMC.STMTS(thenImcList);

        Vector<IMC.Stmt> seq = new Vector<>();
        seq.add(cjump);
        seq.add(new IMC.LABEL(thenLabel));
        seq.add(thenBody);
        seq.add(new IMC.JUMP(new IMC.NAME(endLabel)));
        seq.add(new IMC.LABEL(endLabel));

        IMC.STMTS result = new IMC.STMTS(seq);

        // 6) Shranimo v ImcGen.stmt
        ImcGen.stmt.put(ifThenStmt, result);

        return arg;
    }

@Override
public Object visit(AST.IfThenElseStmt ifElse, Object arg) {
   // System.err.println("Generating intermediate code for if-then-else statement");

    // Generate intermediate code for the condition
    IMC.Expr condition = (IMC.Expr) ifElse.condExpr.accept(this, arg);

    // Generate intermediate code for the "then" statements
    Vector<IMC.Stmt> thenStmts = new Vector<>();
    if (ifElse.thenStmt != null) {
        for (AST.Stmt stmt : ifElse.thenStmt) {
            IMC.Stmt imcStmt = (IMC.Stmt) stmt.accept(this, arg);
            thenStmts.add(imcStmt);
        }
    }

    // Generate intermediate code for the "else" statements
    Vector<IMC.Stmt> elseStmts = new Vector<>();
    if (ifElse.elseStmt != null) {
        for (AST.Stmt stmt : ifElse.elseStmt) {
            IMC.Stmt imcStmt = (IMC.Stmt) stmt.accept(this, arg);
            elseStmts.add(imcStmt);
        }
    }

    // Create labels for the "then", "else", and "end" branches
    MEM.Label thenLabel = new MEM.Label();
    MEM.Label elseLabel = new MEM.Label();
    MEM.Label endLabel = new MEM.Label();


    IMC.LABEL thenLabelStmt = new IMC.LABEL(thenLabel);
    IMC.LABEL elseLabelStmt = new IMC.LABEL(elseLabel);
    IMC.LABEL endLabelStmt = new IMC.LABEL(endLabel);
    IMC.NAME  thenName = new IMC.NAME(thenLabel);
    IMC.NAME  elseName = new IMC.NAME(elseLabel);
    IMC.NAME  endName = new IMC.NAME(endLabel);

    // Create a conditional jump (CJUMP) to the "then" or "else" branch
    IMC.CJUMP cjump = new IMC.CJUMP(condition, thenName, elseName);

    // Wrap the "then" statements in an IMC.STMTS
    IMC.STMTS thenBlock = new IMC.STMTS(thenStmts);

    // Wrap the "else" statements in an IMC.STMTS
    IMC.STMTS elseBlock = new IMC.STMTS(elseStmts);

    // Create an unconditional jump to the "end" label after the "then" block
    IMC.JUMP jumpToEnd = new IMC.JUMP(endName);

    // Combine all statements into a single vector
    Vector<IMC.Stmt> ifElseStmtsVector = new Vector<>();
    ifElseStmtsVector.add(cjump);          // Conditional jump
    ifElseStmtsVector.add(thenLabelStmt);  // "Then" label
    ifElseStmtsVector.add(thenBlock);      // "Then" block
    ifElseStmtsVector.add(jumpToEnd);      // Jump to "end"
    ifElseStmtsVector.add(elseLabelStmt);  // "Else" label
    ifElseStmtsVector.add(elseBlock);      // "Else" block
    ifElseStmtsVector.add(jumpToEnd);      // Jump to "end"
    ifElseStmtsVector.add(endLabelStmt);   // "End" label

    // Wrap the combined statements in an IMC.STMTS
    IMC.STMTS ifElseStmts = new IMC.STMTS(ifElseStmtsVector);

    // Store the generated intermediate code in the semantic attribute
    ImcGen.stmt.put(ifElse, ifElseStmts);

   // System.err.println("Generated intermediate code for if-then-else statement: " + ifElseStmts);
    return ifElseStmts;
}

 @Override
    public Object visit(AST.WhileStmt whileStmt, Object arg) {
        // 1) Najprej obdelamo pogoj in dobimo njegovo vmesno kodo
        whileStmt.condExpr.accept(this, arg);
        IMC.Expr condImc = ImcGen.expr.get(whileStmt.condExpr);

        // 2) Ustvarimo labele 
        MEM.Label loopLabel = new MEM.Label();
        MEM.Label bodyLabel = new MEM.Label();
        MEM.Label exitLabel = new MEM.Label();

        // 3) Zgradimo CJUMP za prehod med bodyLabel in exitLabel
        IMC.CJUMP cjump = new IMC.CJUMP(condImc, new IMC.NAME(bodyLabel), new IMC.NAME(exitLabel));

        // 4) Zložimo telo zanke S1, …, Ss v zaporedje (IMC.STMTS).
        Vector<IMC.Stmt> bodyList = new Vector<>();
        for (AST.Stmt s : whileStmt.stmts) {
            s.accept(this, arg);                  
            if(ImcGen.stmt.get(s) != null)
                bodyList.add(ImcGen.stmt.get(s));    
        }
        IMC.STMTS bodySeq = new IMC.STMTS(bodyList);

        Vector<IMC.Stmt> stmtsSeq = new Vector<>();
        stmtsSeq.add(new IMC.LABEL(loopLabel)); //start of the loop
        stmtsSeq.add(cjump);  // jump to the body
        stmtsSeq.add(new IMC.LABEL(bodyLabel)); // body label
        stmtsSeq.add(bodySeq); // make body
        stmtsSeq.add(new IMC.JUMP(new IMC.NAME(loopLabel))); // add to start beggining of the loop
        stmtsSeq.add(new IMC.LABEL(exitLabel)); // get a

        IMC.STMTS whileImc = new IMC.STMTS(stmtsSeq);

        // 6) Shranimo rezultat v ImcGen.stmt
        ImcGen.stmt.put(whileStmt, whileImc);

        return null;
    }

//letStmt
@Override
public Object visit(AST.LetStmt letStmt, Object arg) {
   // System.err.println("Generating intermediate code for let statement");
    letStmt.defns.accept(this, arg);

    Vector<IMC.Stmt> stmtList = new Vector<>();
    for (AST.Stmt s : letStmt.stmts) {
        s.accept(this, arg);                     
        IMC.Stmt stmtImc = ImcGen.stmt.get(s);    
        if(stmtImc != null)
            stmtList.add(stmtImc);
    }

    //  Združimo stavke v en IMC.STMTS
    IMC.STMTS body = new IMC.STMTS(stmtList);

    //V `ImcGen.stmt` zapišemo rezultat
    ImcGen.stmt.put(letStmt, body);

    return null;
}
@Override
public Object visit(AST.CompExpr compExpr, Object arg) {
   // System.err.println("Generating intermediate code for CompExpr: " + compExpr.name);

    // Generate intermediate code for the record expression
    IMC.Expr recExpr = (IMC.Expr) compExpr.recExpr.accept(this, arg);

    // Get the type of the record expression
    TYP.Type recType = SemAn.ofType.get(compExpr.recExpr);
    if (recType == null) {
        throw new Report.Error(compExpr, "Record type not found.");
    }

    recType = recType.actualType();

    // Find the offset of the field using Memory.accesses
    long fieldOffset = -1;
    if (recType instanceof TYP.StrType strType) {
        AST.StrType astStr = TypeResolver.str_hash.get(strType);
        if (astStr != null) {
            for (AST.CompDefn comp : astStr.comps) {
                if (comp.name.equals(compExpr.name)) {
                    MEM.Access access = Memory.accesses.get(comp);
                    if (access instanceof MEM.RelAccess relAccess) {
                        fieldOffset = relAccess.offset;
                    } else {
                        throw new Report.Error(compExpr, "Invalid access type for field: " + compExpr.name);
                    }
                    break;
                }
            }
        }
    } else if (recType instanceof TYP.UniType uniType) {
        AST.UniType astUni = TypeResolver.uni_hash.get(uniType);
        if (astUni != null) {
            for (AST.CompDefn comp : astUni.comps) {
                if (comp.name.equals(compExpr.name)) {
                    MEM.Access access = Memory.accesses.get(comp);
                    if (access instanceof MEM.RelAccess relAccess) {
                        fieldOffset = relAccess.offset;
                    } else {
                        throw new Report.Error(compExpr, "Invalid access type for field: " + compExpr.name);
                    }
                    break;
                }
            }
        }
    } else {
        throw new Report.Error(compExpr, "Expression is not a struct or union.");
    }

    if (fieldOffset == -1) {
        throw new Report.Error(compExpr, "Field '" + compExpr.name + "' not found in record type.");
    }

    // Generate intermediate code for accessing the field
    recExpr = unwrapMem8(recExpr);
    IMC.Expr fieldAddress = new IMC.BINOP(IMC.BINOP.Oper.ADD, recExpr, new IMC.CONST(fieldOffset));
    IMC.MEM8 fieldAccess = new IMC.MEM8(fieldAddress);

    // Store the generated intermediate code in the semantic attribute
    ImcGen.expr.put(compExpr, fieldAccess);

   // System.err.println("Generated intermediate code for CompExpr: " + fieldAccess);
    return fieldAccess;
}
private long align8(long size) {
        return (size % 8 == 0) ? size : ((size / 8) + 1) * 8;
    }

@Override
public Object visit(AST.CallExpr callExpr, Object arg) {
    //System.err.println("Generating intermediate code for CallExpr: " + callExpr.funExpr);
    AST.Defn function;
    // Retrieve the function definition
    if (callExpr.funExpr instanceof AST.CastExpr){
        AST.CastExpr castExpr = (AST.CastExpr) callExpr.funExpr;
        function = SemAn.defAt.get(castExpr.expr);
    }
    else{
        try {
            function = SemAn.defAt.get(callExpr.funExpr);
        } catch (InternalError e) {
            throw new Report.Error(callExpr, "Function '" + callExpr + "' not found.");
        }
    }
    if (function == null) {
        throw new Report.Error(callExpr, "Function '" + callExpr + "' not found.");
    }
    //System.err.println("Function found: " + function);

    // Generate intermediate code for the function name (funExpr)
    IMC.Expr funExpr = (IMC.Expr) callExpr.funExpr.accept(this, arg);

    // Generate intermediate code for the arguments
    Vector<IMC.Expr> args = new Vector<>();
    Vector<Long> offsets = new Vector<>();
    long currentOffset = 0;
   // System.err.println("Current offset: " + currentOffset);
    args.add(currentTemp);
    offsets.add(currentOffset);
    if (callExpr.argExprs != null) {
        for (AST.Expr argExpr : callExpr.argExprs) {
            // Generate intermediate code for each argument
            IMC.Expr imcArg = (IMC.Expr) argExpr.accept(this, arg);
            args.add(imcArg);
            // Add the offset for the argument
            
            // Update the current offset (assuming all arguments are 8 bytes for simplicity)
            TYP.Type argType = SemAn.ofType.get(argExpr);
            long argSize = argType.accept(new SizeEvaluator(), null);
            //System.err.println(" argTypeer ___" + argType + " " +align8(argSize));
            currentOffset += align8(argSize);
            //System.err.println("Current offset: " + currentOffset);
            offsets.add(currentOffset);
        }
    }

    // Create a CALL expression with the function name, offsets, and arguments
    //System.err.println("Function name: " + funExpr);
    //System.err.println("Offsets: " + offsets);
    //System.err.println("Arguments: " + args);
    IMC.CALL call = new IMC.CALL(funExpr, offsets, args);

    // Store the generated intermediate code in the semantic attribute
    ImcGen.expr.put(callExpr, call);

   // System.err.println("Generated intermediate code for CallExpr: " + call);
    return call;
}
 @Override
    public Object visit(AST.SizeExpr sizeExpr, Object arg) {
        //System.err.println("Generating intermediate code for SizeExpr: " + sizeExpr);
        TYP.Type typ = SemAn.isType.get(sizeExpr.type);
        if (typ == null) {
            throw new Report.InternalError();
        }
    
        long size = typ.accept(new SizeEvaluator(), null);
        IMC.CONST return_const = new IMC.CONST(size);
        ImcGen.expr.put(sizeExpr,return_const);
        return return_const;
    }
}
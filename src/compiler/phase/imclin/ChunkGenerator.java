package compiler.phase.imclin;

import java.util.Vector;

import compiler.phase.abstr.*;
import compiler.phase.imcgen.*;
import compiler.phase.memory.*;

public class ChunkGenerator implements AST.FullVisitor<Object, LIN.CodeChunk>, IMC.Visitor<IMC.Expr, Vector<IMC.Stmt>> {

    // ===== AST Visitors =====

    @Override
    public Object visit(AST.VarDefn variable, LIN.CodeChunk dummyChunk) {
        MEM.Access accessInfo = Memory.accesses.get(variable);
        // System.err.println("Visiting variable definition: " + variable.name);
        if (accessInfo instanceof MEM.AbsAccess)
            ImcLin.addDataChunk(new LIN.DataChunk((MEM.AbsAccess) accessInfo));
        return null;
    }

    // ====== AST.Defn Visitors ======
    @Override
    public Object visit(AST.DefFunDefn functionNode, LIN.CodeChunk ignoreChunk) {
        // System.err.println("Generating code chunk for function: " + functionNode.name);
        if ((functionNode.stmts != null) || (!compiler.Compiler.devMode())) {

            for (AST.Stmt stmt : functionNode.stmts) {
                // System.err.println("Visiting statement in function body");
                stmt.accept(this, ignoreChunk);
            }

            MEM.Label entryLabel = ImcGen.entryLabel.get(functionNode);
            MEM.Label exitLabel = ImcGen.exitLabel.get(functionNode);

            Vector<IMC.Stmt> bodyStmts = new Vector<>();
            bodyStmts.add(new IMC.LABEL(entryLabel));

            for (AST.Stmt stmt : functionNode.stmts) {
                IMC.Stmt imcStmt = ImcGen.stmt.get(stmt);
                // System.err.println("Translating statement to IMC: " + imcStmt);
                imcStmt.accept(this, bodyStmts);
            }

            bodyStmts.add(new IMC.LABEL(exitLabel));
            MEM.Frame functionFrame = Memory.frames.get(functionNode);

            ImcLin.addCodeChunk(new LIN.CodeChunk(
                functionFrame,
                bodyStmts,
                entryLabel,
                exitLabel
            ));
        }
        return null;
    }

    // ===== AST.Expr Visitors =====
    @Override
    public Object visit(AST.AtomExpr atom, LIN.CodeChunk unused) {
        // System.err.println("Visiting atom expression: " + atom);
        if (atom.type == AST.AtomExpr.Type.STR)
            ImcLin.addDataChunk(new LIN.DataChunk(Memory.strings.get(atom)));
        return null;
    }

    // ===== IMC Visitors =====

    @Override
    public IMC.Expr visit(IMC.CONST literal, Vector<IMC.Stmt> collector) {
        // System.err.println("Visiting CONST: " + literal.value);
        return literal;
    }

    @Override
    public IMC.Expr visit(IMC.TEMP tempRef, Vector<IMC.Stmt> collector) {
        // System.err.println("Visiting TEMP: " + tempRef.temp);
        return tempRef;
    }

    @Override
    public IMC.Expr visit(IMC.NAME labelName, Vector<IMC.Stmt> collector) {
        // System.err.println("Visiting NAME: " + labelName.label);
        return labelName;
    }

    @Override
    public IMC.Expr visit(IMC.UNOP unary, Vector<IMC.Stmt> out) {
        // System.err.println("Processing UNOP: " + unary.oper);
        IMC.Expr operand = unary.subExpr.accept(this, out);
        IMC.TEMP resultHolder = new IMC.TEMP(new MEM.Temp());
        out.add(new IMC.MOVE(resultHolder, new IMC.UNOP(unary.oper, operand)));
        return resultHolder;
    }

    @Override
    public IMC.Expr visit(IMC.BINOP binary, Vector<IMC.Stmt> out) {
        // System.err.println("Processing BINOP: " + binary.oper);
        IMC.Expr leftSide = binary.fstExpr.accept(this, out);
        IMC.Expr rightSide = binary.sndExpr.accept(this, out);
        IMC.TEMP resultSlot = new IMC.TEMP(new MEM.Temp());
        IMC.BINOP operation = new IMC.BINOP(binary.oper, leftSide, rightSide);
        out.add(new IMC.MOVE(resultSlot, operation));
        return resultSlot;
    }

    @Override
    public IMC.Expr visit(IMC.CALL call, Vector<IMC.Stmt> out) {
        // System.err.println("Handling CALL to: " + call.addr);
        Vector<IMC.Expr> actuals = new Vector<>();
        for (IMC.Expr arg : call.args) {
            // System.err.println("Processing CALL argument...");
            actuals.add(arg.accept(this, out));
        }
        IMC.TEMP callResult = new IMC.TEMP(new MEM.Temp());
        out.add(new IMC.MOVE(callResult, new IMC.CALL(call.addr, call.offs, actuals)));
        return callResult;
    }

    @Override
    public IMC.Expr visit(IMC.CJUMP branch, Vector<IMC.Stmt> out) {
        // System.err.println("Processing conditional jump...");
        IMC.Expr condition = branch.cond.accept(this, out);
        MEM.Label falseJump = new MEM.Label();
        out.add(new IMC.CJUMP(condition, branch.posAddr, new IMC.NAME(falseJump)));
        out.add(new IMC.LABEL(falseJump));
        out.add(new IMC.JUMP(branch.negAddr));
        return null;
    }

    @Override
    public IMC.Expr visit(IMC.LABEL label, Vector<IMC.Stmt> out) {
        // System.err.println("Adding label: " + label.label);
        out.add(label);
        return null;
    }

    @Override
    public IMC.Expr visit(IMC.JUMP jump, Vector<IMC.Stmt> out) {
        // System.err.println("Processing jump to: " + jump.addr);
        out.add(new IMC.JUMP(jump.addr.accept(this, out)));
        return null;
    }

    @Override
    public IMC.Expr visit(IMC.ESTMT sideEffect, Vector<IMC.Stmt> out) {
        // System.err.println("Evaluating ESTMT...");
        sideEffect.expr.accept(this, out);
        return null;
    }

    @Override
    public IMC.Expr visit(IMC.STMTS block, Vector<IMC.Stmt> out) {
        // System.err.println("Visiting block of statements...");
        for (IMC.Stmt stmt : block.stmts)
            stmt.accept(this, out);
        return null;
    }

    @Override
    public IMC.Expr visit(IMC.MOVE assignment, Vector<IMC.Stmt> out) {
        // System.err.println("Handling MOVE operation...");
        IMC.Expr destination = switch (assignment.dst) {
            case IMC.MEM1 mem1 -> new IMC.MEM1(mem1.addr.accept(this, out));
            case IMC.MEM8 mem8 -> new IMC.MEM8(mem8.addr.accept(this, out));
            default -> assignment.dst.accept(this, out);
        };
        IMC.Expr value = assignment.src.accept(this, out);
        out.add(new IMC.MOVE(destination, value));
        return null;
    }

    @Override
    public IMC.Expr visit(IMC.MEM1 mem, Vector<IMC.Stmt> out) {
        // System.err.println("Accessing MEM1...");
        IMC.Expr addr = mem.addr.accept(this, out);
        IMC.TEMP temp = new IMC.TEMP(new MEM.Temp());
        out.add(new IMC.MOVE(temp, new IMC.MEM1(addr)));
        return temp;
    }

    @Override
    public IMC.Expr visit(IMC.MEM8 mem, Vector<IMC.Stmt> out) {
        // System.err.println("Accessing MEM8...");
        IMC.Expr addr = mem.addr.accept(this, out);
        IMC.TEMP temp = new IMC.TEMP(new MEM.Temp());
        out.add(new IMC.MOVE(temp, new IMC.MEM8(addr)));
        return temp;
    }
}

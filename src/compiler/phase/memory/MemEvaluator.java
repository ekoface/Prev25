package compiler.phase.memory;

import java.util.*;
import compiler.phase.abstr.*;
import compiler.phase.seman.*;

/**
 * Computing memory layout: stack frames and variable accesses.
 * 
 * @author bostjan.slivnik@fri.uni-lj.si
 */
public class MemEvaluator implements AST.FullVisitor<Object, Object> {

    // Offset for local variables in the current stack frame
    private long localVarOffset;
    private long depth = -1;

    // Current stack frame
    private MEM.Frame currentFrame;
    SizeEvaluator sizeEvaluator = new SizeEvaluator();
    private Long all_args = 0L;
    private Long var_offset = 0L;
    private Long cast_args = 0L;
    private Long locs_fun = 0L;
    public Long nested_args = 0L;
    
@Override
public Object visit(AST.DefFunDefn defFunDefn, Object arg) {
    //System.err.println("Memory layout for function " + defFunDefn.name);

    Long returnDepth = depth;
    Long current_depth = depth;
    Long locs_before = locs_fun;
    Long args_before = all_args;
    Long var_offset_before = var_offset;

    locs_fun = 0L;
    all_args = 0L;

    MEM.Label label = new MEM.Label();
    if (depth == -1) {
        depth = 0L;
        current_depth = 0L;
        label = new MEM.Label(defFunDefn.name);
    }
    depth++;

    localVarOffset = 0;
    var_offset = 0L;

    long offset_pars = 8L;
    long own_args_size = 0L;

    // process parameters (you may want to count their space)
    for (AST.ParDefn parDefn : defFunDefn.pars) {
        offset_pars = (Long) parDefn.accept(this, offset_pars);
    }

    // Store own args size before entering stmts
    own_args_size = all_args;
    all_args = 0L; // reset to track nested

    long nested_max_args = 0L;

    for (AST.Stmt stmt : defFunDefn.stmts) {
        stmt.accept(this, localVarOffset);
        nested_max_args = Math.max(nested_max_args, all_args); // track nested
    }
    // Now get the max between own and nested
    long total_args = Math.max(own_args_size, nested_max_args);
    all_args = total_args;

    long size_fun = align8(16L + locs_fun + all_args);
    currentFrame = new MEM.Frame(label, current_depth, locs_fun, all_args, size_fun);
    Memory.frames.put(defFunDefn, currentFrame);

    // Restore
    depth = returnDepth;
    localVarOffset = 0;
    var_offset = var_offset_before;
    all_args = args_before;
    locs_fun = locs_before;

    return localVarOffset;
}


private long align8(long size) {
    return (size % 8 == 0) ? size : ((size / 8 + 1) * 8);
}


    @Override
    public Object visit(AST.ParDefn parDefn, Object arg) {
        //System.err.println("  Parameter " + parDefn.name);
        TYP.Type type = SemAn.ofType.get(parDefn);
        
        long size = type.accept(sizeEvaluator, null);
        long argSize = (arg == null) ? 0L : (long) arg;
        //System.err.println("    Size: " + size);
       // System.err.println("    Offset: " + argSize);
        MEM.Label label = new MEM.Label(parDefn.name);

        // Create an AbsAccess for the parameter
        MEM.RelAccess access = new MEM.RelAccess(size, argSize, depth);
        Memory.accesses.put(parDefn, access);
        return size + argSize;
    }


    @Override
public Object visit(AST.VarDefn varDefn, Object arg) {
    //System.err.println("  Local variable " + varDefn.name);
    int add_min = 1;
    if (depth != -1) {
        add_min = -1;
    }
    TYP.Type type = SemAn.ofType.get(varDefn);
    if (type == null) {
        //throw new Report.Error(varDefn.location(), "Type of variable " + varDefn.name + " is not defined.");
    }
    varDefn.type.accept(this, arg);
    long size = type.accept(sizeEvaluator, null) * add_min;
   // System.err.println("    Size: " + size);
   // System.err.println("    Offset: " + localVarOffset);
    MEM.Label label = new MEM.Label(varDefn.name);
    if (add_min<0){
        var_offset += size == -1 ? -8 : size;
        var_offset *= -1;
        var_offset = (var_offset % 8 == 0) ? var_offset : ((var_offset / 8) + 1) * 8;
        var_offset *=-1;
        MEM.RelAccess access = new MEM.RelAccess(-size, var_offset, depth);
        Memory.accesses.put(varDefn, access);
        locs_fun = -var_offset;
        return size;
    }
    
    MEM.AbsAccess access = new MEM.AbsAccess(size, label);
    Memory.accesses.put(varDefn, access);

    return size;
}
// StrType
@Override
public Object visit(AST.StrType strType, Object arg) {
   // System.err.println("  String type");
    Long Offset = (arg == null) ? 0L : (Long) arg; 
    for (AST.CompDefn compDefn : strType.comps) {
        //System.err.println("    Component " + compDefn.name + " offset: " + Offset);
        TYP.Type type = SemAn.ofType.get(compDefn);
        Offset = (Long) compDefn.accept(this, Offset);
    }
    return Offset;
}
@Override
public Object visit(AST.UniType uniType, Object arg) {
   // System.err.println("  Union type");
    Long Offset = (arg == null) ? 0L : (Long) arg; 
    Long BeforeOffset = Offset;
    for (AST.CompDefn compDefn : uniType.comps) {
       // System.err.println("    Component " + compDefn.name);
        TYP.Type type = SemAn.ofType.get(compDefn);
        Offset = (Long) compDefn.accept(this, BeforeOffset);
    
    }
    return Offset;
}
@Override
public Object visit(AST.CompDefn compDefn, Object arg) {
   // System.err.println("    Component " + compDefn.name);
    Long Offset = (Long) arg;
    TYP.Type type = SemAn.ofType.get(compDefn);
    Long result_fr = (arg == null) ? 0L : (Long) arg;
    Long new_offset = 0L;
    if (type == null) {
        //throw new Report.Error(compDefn.location(), "Type of variable " + compDefn.name + " is not defined.");
    }
    if (type instanceof TYP.StrType) {
       // System.err.println("      Component is of type StrType");
        // Delegate to visit(AST.StrType)
        Object result = compDefn.type.accept(this, new_offset);
    }
    else if (type instanceof TYP.UniType) {
       // System.err.println("      Component is of type UniType");
        // Delegate to visit(AST.UniType)
        Object result = compDefn.type.accept(this, new_offset);
    }
    long size = type.accept(sizeEvaluator, null);
   
        // Create a RelAccess for the component
    MEM.RelAccess access = new MEM.RelAccess(size, Offset,-1L);
    Memory.accesses.put(compDefn, access);

    // Update the offset
    
    Offset += size;
    return Offset;
}

@Override
public Object visit(AST.ExtFunDefn extrFunDefn, Object arg) {
    //System.err.println("Memory layout for function " + extrFunDefn.name);
    Long returnDepth = depth;
    
    depth +=2;
    Long current_depth = depth +1;
    Long offset = (arg == null) ? 0L : (Long) arg;
    offset += 8L;
    for (AST.ParDefn parDefn : extrFunDefn.pars) {
        offset = (Long) parDefn.accept(this, offset);
    }
    offset -= 8L;
    depth = returnDepth;
    return offset;
}
//go over all stmts
@Override
public Object visit(AST.IfThenStmt ifThenStmt, Object arg) {
    //except cond
    ifThenStmt.condExpr.accept(this, arg);
    for(AST.Stmt stmt : ifThenStmt.thenStmt){
        stmt.accept(this, arg);
    }
    return arg;
}
@Override
public Object visit(AST.IfThenElseStmt ifThenElseStmt, Object arg) {
    ifThenElseStmt.condExpr.accept(this, arg);
    for(AST.Stmt stmt : ifThenElseStmt.thenStmt){
        stmt.accept(this, arg);
    }
    for(AST.Stmt stmt : ifThenElseStmt.elseStmt){
        stmt.accept(this, arg);
    }
    return arg;
}
@Override
public Object visit(AST.WhileStmt whileStmt, Object arg) {
    whileStmt.condExpr.accept(this, arg);
    for(AST.Stmt stmt : whileStmt.stmts){
        stmt.accept(this, arg);
    }
    return arg;
}
@Override
public Object visit(AST.LetStmt letStmt, Object arg) {
   // System.err.println("  Let statement __________");
    for(AST.FullDefn stmt : letStmt.defns){
        stmt.accept(this, arg);
    }
    for(AST.Stmt decl : letStmt.stmts){
        decl.accept(this, arg);
    }
    return arg;
}
@Override
    public Object visit(AST.AtomExpr atomExpr, Object arg) {
        if (atomExpr.type == AST.AtomExpr.Type.STR) {
            String raw = atomExpr.value;
            StringBuilder decoded = new StringBuilder();
            int i = 1; // Skip starting quote
            while (i < raw.length() - 1) { // Skip ending quote
                char ch = raw.charAt(i);
                if (ch == '\\') {
                    char next = raw.charAt(i + 1);
                    switch (next) {
                        case 'n':
                            decoded.append('\n');
                            i += 2;
                            break;
                        case 't':
                            decoded.append('\t');
                            i += 2;
                            break;
                        case '"':
                            decoded.append('\"');
                            i += 2;
                            break;
                        case '\\':
                            decoded.append('\\');
                            i += 2;
                            break;
                        case '0':
                            if (i + 4 < raw.length() && raw.charAt(i + 2) == 'x') {
                                String hex = raw.substring(i + 3, i + 5);
                                int val = Integer.parseInt(hex, 16);
                                decoded.append((char) val);
                                i += 5;
                            } else {
                                decoded.append('\\').append('0');
                                i += 2;
                            }
                            break;
                        default:
                            decoded.append(next);
                            i += 2;
                            break;
                    }
                } else {
                    decoded.append(ch);
                    i++;
                }
            }
            String finalString = decoded.toString();
            String xmlReady = xmlSafe(finalString);
            //long size = align8(finalString.length());
            long size = finalString.length() + 1; // +1 for null terminator
            MEM.Label label = new MEM.Label();
            MEM.AbsAccess absAccess = new MEM.AbsAccess(size, label, xmlReady);
           // System.err.println("[MemEvaluator] STR literal: \"" + finalString + "\" (size=" + size + ") @ " + label.name);
            Memory.strings.put(atomExpr, absAccess);
        }
        return arg;
    }


private String xmlSafe(String str) {
    return str.replace("&", "&amp;")
              .replace("<", "&lt;")
              .replace(">", "&gt;")
              .replace("\"", "&quot;")
              .replace("'", "&apos;");
}

@Override
public Object visit(AST.CallExpr callExpr, Object arg) {
   // System.err.println("  Call expression");
    Long Offset = (arg == null) ? 0L : (Long) arg;
    cast_args = 0L;
    Long new_offset = 0L;
    Long pass_offset = 0L;
    callExpr.funExpr.accept(this, Offset);
    
    for (AST.Expr expr : callExpr.argExprs) {
     //   System.err.println("    Argument " + expr);
        Long argOfset = (Long) (expr.accept(this, pass_offset));
         if (argOfset == null) {
            throw new NullPointerException("Argument offset is null for: " + expr);
        }
      //  System.err.println("    Argument offset " + argOfset);
        Offset += argOfset;
        
        //System.err.println("    Size: " + Offset+ "for " + expr);
    }
    for (AST.Expr e : callExpr.argExprs) {
        TYP.Type argType = SemAn.ofType.get(e);
        long argSize = argType.accept(sizeEvaluator, null);
        pass_offset += align8(argSize);
    }
    //System.err.println("    Pass offset " + pass_offset);
    if (pass_offset > 0) {
        //args + static linker
        if (pass_offset % 8 != 0) {
            pass_offset = (pass_offset / 8 + 1) * 8;
        }
        all_args = Math.max(all_args,pass_offset + 8L);
        //System.err.println("----Size: " + all_args);
    }
   // System.err.println("    Size: " + Offset);
    Long return_arg = arg == null ? 0L : (Long) arg;
    return_arg += Offset;
    //System.out.println("+++++Size: " + all_args);
    return arg;
}
@Override
public Object visit(AST.NameExpr nameExpr, Object arg) {
    //System.err.println("  Name expression " + nameExpr.name);
    TYP.Type type = SemAn.ofType.get(nameExpr);
    Long sizer = type.accept(sizeEvaluator, null);
    cast_args += sizer;
    return 8L + (arg == null ? 0L : (Long) arg);
}
@Override
public Object visit(AST.AssignStmt assignStmt, Object arg) {
    assignStmt.dstExpr.accept(this, arg);
    assignStmt.srcExpr.accept(this, arg);
    return arg;
}
@Override
public Object visit(AST.SfxExpr sfxExpr, Object arg) {
    //System.err.println("  Suffix expression " + sfxExpr.name);
    TYP.Type type = SemAn.ofType.get(sfxExpr);
    cast_args += 8L;
    //visit 
    sfxExpr.subExpr.accept(this, arg);

    return 8L + (arg == null ? 0L : (Long) arg);
}
@Override
public Object visit(AST.BinExpr binExpr, Object arg) {
    //System.err.println("  Binary expression " + binExpr.oper);
    TYP.Type type = SemAn.ofType.get(binExpr);
    //cast_args = 8L;
    //visit both sides
    binExpr.fstExpr.accept(this, arg);
    binExpr.sndExpr.accept(this, arg);
    return 8L + (arg == null ? 0L : (Long) arg);
}
@Override
public Object visit(AST.PfxExpr pfxExpr, Object arg) {
    //System.err.println("  Prefix expression " + pfxExpr.oper);
    TYP.Type type = SemAn.ofType.get(pfxExpr);
    cast_args = 8L;
    return 8L + (arg == null ? 0L : (Long) arg);
}
@Override
public Object visit(AST.CastExpr castExpr, Object arg) {
    //System.err.println("  Cast expression " + castExpr.type);
    TYP.Type type = SemAn.ofType.get(castExpr);
    //cast_args = 8L;
    return 8L + (arg == null ? 0L : (Long) arg);
}
//ArrExpr
@Override
public Object visit(AST.ArrExpr arrExpr, Object arg) {
    TYP.Type type = SemAn.ofType.get(arrExpr);
    //get size of array
    long size = type.accept(sizeEvaluator, null);
    //System.err.println("  Array expression " + arrExpr);
   // System.err.println("    Size: " + size);
    return size + (arg == null ? 0L : (Long) arg);
}
public Object visit(AST.CompExpr compExpr, Object arg) {
    //System.err.println("  Component expression " + compExpr.name);
    TYP.Type type = SemAn.ofType.get(compExpr);
    //get size of component
    long size = type.accept(sizeEvaluator, null);
   // System.err.println("    Size: " + size);
    return arg;
}


//FunType 
}

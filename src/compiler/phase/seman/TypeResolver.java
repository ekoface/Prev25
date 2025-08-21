package compiler.phase.seman;

import java.util.*;

import compiler.common.report.*;
import compiler.phase.abstr.*;

/**
 * Type resolver.
 * 
 * @author 
 */

public class TypeResolver implements AST.FullVisitor<TYP.Type, TypeResolver.Mode> { 

    /** Constructs a new type resolver. */
    public TypeResolver() {
    }

    /** Modes for type resolution. */
    protected enum Mode {
        DECLARE,
        RESOLVE, 
		RECURSION
    }

    
    private final Set<TYP.Type> resolvingTypes = new HashSet<>();
	public static final Map<TYP.StrType, AST.StrType> str_hash = new HashMap<>();
    public static final Map<TYP.UniType, AST.UniType> uni_hash = new HashMap<>();
    public static boolean isMain = false;

	@Override
	public TYP.Type visit(AST.Nodes<? extends AST.Node> nodes, Mode mode) {
		if (nodes == null) return null;
        if (mode == null) {
            for (AST.Node node : nodes) {
				if(node instanceof AST.DefFunDefn){
					AST.DefFunDefn funDef = (AST.DefFunDefn) node;
					if(funDef.name.equals("main")){
						isMain = true;
                        //System.err.println("Main function found.");
					}
				}
                if (node != null) {
                    node.accept(this, Mode.DECLARE);
                }
            }
            for (AST.Node node : nodes) {
                if (node != null) {
                    node.accept(this, Mode.RESOLVE);
                }
            }
			for (AST.Node node : nodes) {
				if (node != null) {
					node.accept(this, Mode.RECURSION);
				}
			}
        } else {
            for (AST.Node node : nodes) {
                if (node != null) {
                    node.accept(this, mode);
                }
            }
        }
		//TYP:: 1 #TO DO uncomment that line in !isMain !!!!!
		if(isMain == false){
			//throw new Report.Error("Main function is missing.");
		}
        return null;
	}
    @Override
    public TYP.Type visit(AST.AtomType atomType, Mode mode) {
        //System.err.println("Visiting AtomType");
        TYP.Type resolvedType;
        switch (atomType.type) {
            case INT:
                resolvedType = TYP.IntType.type;
                break;
            case CHAR:
                resolvedType = TYP.CharType.type;
                break;
            case BOOL:
                resolvedType = TYP.BoolType.type;
                break;
            case VOID:
                resolvedType = TYP.VoidType.type;
                break;
            default:
                throw new Report.Error(atomType.location(), "Unknown atomic type.");
        }
        //System.err.println("Resolved AtomType to: " + resolvedType);
        SemAn.isType.put(atomType, resolvedType);
        return resolvedType;
    }

	@Override
public TYP.Type visit(AST.TypDefn typDefn, Mode mode) {
   // System.err.println("Visiting TypDefn: " + typDefn.name + " mode: " + mode);

    if (mode == Mode.DECLARE) {
        // Create a NameType for the TypDefn and store it in isType
        TYP.NameType nameType = new TYP.NameType(typDefn.name);
        SemAn.isType.put(typDefn, nameType);
    } else if (mode == Mode.RESOLVE) {
        TYP.NameType nameType = (TYP.NameType) SemAn.isType.get(typDefn);
        // Resolve the actual type of the TypDefn
        TYP.Type actualType = typDefn.type.accept(this, mode);
		if (actualType == null) {
			throw new Report.Error(typDefn.location(), "Type is not resolved.");
		}
        nameType.setActType(actualType);
        //System.err.println("Resolved TypDefn " + typDefn.name + " to: " + actualType);
    }
	else if (mode == Mode.RECURSION) {
    TYP.NameType nameType = (TYP.NameType) SemAn.isType.get(typDefn);
    //System.err.println("Visiting TypDefn: " + typDefn.name + " mode: " + mode + " Type: " + nameType.actualType());
    if (nameType == null) {
        throw new Report.Error(typDefn.location(), "Type " + typDefn.name + " is not declared.");
    }
    if (resolvingTypes.contains(nameType)) {
        //throw new Report.Error(typDefn.location(), "Circular type definition detected for " + typDefn.name);
    }
    TYP.Type actualType_c = nameType.actualType();
    if (actualType_c instanceof TYP.StrType || actualType_c instanceof TYP.UniType) {
       // System.err.println("Adding: " + actualType_c);
            //resolvingTypes.add(nameType);
    }

    TYP.Type actualType = typDefn.type.accept(this, Mode.RECURSION);
    if (actualType_c instanceof TYP.StrType || actualType_c instanceof TYP.UniType) {
         //resolvingTypes.remove(nameType);
    }
    if (resolvingTypes.contains(nameType)) {
        //throw new Report.Error(typDefn.location(), "Circular type definition  after detected for " + typDefn.name);
        System.err.println("Circular type definition detected for " + typDefn.name + " after");
    }

    //System.err.println("Recursively resolved TypDefn " + typDefn.name + " to: " + actualType);
    return actualType;
}



    return null;
}
@Override
public TYP.Type visit(AST.NameType nameType, Mode mode) {
        if (mode == Mode.RESOLVE) {
            AST.Defn defn = SemAn.defAt.get(nameType);

             if (!(defn instanceof AST.TypDefn)) {
				throw new Report.Error(nameType.location(), "Name '" + nameType.name + "' is not a type." + "but is " + defn.getClass());
			}

            TYP.Type resolved = SemAn.isType.get(defn);
            if (resolved == null) {
                throw new Report.Error(nameType.location(), "Type " + nameType.name + " is not declared.");
			}

            SemAn.isType.put(nameType, resolved);
            return resolved;
        }
		if(mode == Mode.RECURSION){
			//System.err.println("Visiting NameType: " + nameType.name + " mode: " + mode);
			AST.Defn defn = SemAn.defAt.get(nameType);
			
			TYP.Type resolved = SemAn.isType.get(defn);
			//System.err.println("Resolved NameType " + nameType.name + " to: " + resolved);
			if (resolved == null) {
				throw new Report.Error(nameType.location(), "Type " + nameType.name + " is not declared.");
			}
			// Check if we are already resolving this type (to detect circular dependencies)
			if (resolvingTypes.contains((TYP.NameType) resolved)) {
				throw new Report.Error(nameType.location(), "Circular type definition detected for " + nameType.name + " Nametype");
			
			}
            resolvingTypes.add((TYP.NameType) resolved);
			TYP.Type actualType = defn.accept(this, Mode.RECURSION);
			//System.err.println("Recursively resolved NameType " + nameType.name + " to: " + actualType);
            resolvingTypes.remove((TYP.NameType) resolved);
			return actualType;
		}
		
			return null;
    }
	@Override
	public TYP.Type visit(AST.PtrType ptrType , Mode mode) {
        if (mode == Mode.DECLARE){
            ptrType.baseType.accept(this, mode);
            return null;
        }
		if (mode == Mode.RECURSION) {
			return TYP.IntType.type;
		}
		//System.err.println("Visiting PtrType");
       
		TYP.Type resolvedType = ptrType.baseType.accept(this, mode);
        if (resolvedType == null) {
            throw new Report.Error(ptrType.location(), "Base type of pointer is not resolved.");
        }
        if (resolvedType instanceof TYP.VoidType){
            throw new Report.Error(ptrType.location(), "Pointer cannot have void type.");
        }
       
		TYP.Type pointerType = new TYP.PtrType(resolvedType);
		SemAn.isType.put(ptrType, pointerType);
      //  System.err.println("Resolved PtrType to: " + pointerType + " ww " + ptrType );
		return pointerType;
	}

@Override
public TYP.Type visit(AST.ArrType arrType, Mode mode) {
  //  System.err.println("Visiting ArrType: " + arrType.elemType + " mode: " + mode); 
	if (mode == Mode.RECURSION) {
		//System.err.println("Visiting ArrType: " + arrType.elemType + " mode: " + mode);
		TYP.Type resolvedType = arrType.elemType.accept(this, Mode.RECURSION);
		if (resolvedType == TYP.VoidType.type) {
			throw new Report.Error(arrType.location(), "Array cannot have void type.");
		}
		return null;
	}
    if (mode == Mode.DECLARE) {
        TYP.Type elemType = arrType.elemType.accept(this, mode);
        //System.out.println("elemType: " + elemType);
        return arrType.elemType.accept(this, mode);
        
    }
    
    //System.err.println("Visiting ArrType, mode: " + mode);
	//check if not to void 
	//TYP::15
	if (arrType.elemType instanceof AST.AtomType) {
		AST.AtomType atomType = (AST.AtomType) arrType.elemType;
		if (atomType.type == AST.AtomType.Type.VOID) {
			throw new Report.Error(arrType.location(), "Array cannot have void type.");
		}
	}
	// check that [n] is from 1 to 2^63 -1
	// typ::16
	if (arrType.numElems.isEmpty()) {
		throw new Report.Error(arrType.location(), "Invalid array size: " + arrType.numElems);
	}
	Long sizeLong_c;
    if (arrType.numElems.charAt(0) == '0') {
        throw new Report.Error(arrType.location(), "Invalid array size format: " + arrType.numElems);
    }
	//typ::16
	try {
		sizeLong_c = Long.parseLong(arrType.numElems);
	} catch (NumberFormatException e) {
		throw new Report.Error(arrType.location(), "Invalid array size format: " + arrType.numElems);
	}
	// Resolve the base type of the array
	TYP.Type baseType = arrType.elemType.accept(this, mode);

    if (baseType == null) {
        throw new Report.Error(arrType.location(), "Base type of array is not resolved.");
    }

    // Resolve the size of the array
    String size = arrType.numElems; // Use the correct method
    if ( size.isEmpty()) {
        throw new Report.Error(arrType.location(), "Invalid array size.");
    }

    // Create the array type
	//string to Long
	Long sizeLong = Long.parseLong(size);
	TYP.ArrType arrayType = new TYP.ArrType(baseType, sizeLong);

    // Store the resolved array type in SemAn.isType
    SemAn.isType.put(arrType, arrayType);

    //System.err.println("Resolved ArrType to: " + arrayType);
    return arrayType;
}
//StrType@Override
public TYP.Type visit(AST.StrType strType, Mode mode) {
	List<TYP.Type> new_typs = new ArrayList<>();
	if (mode == Mode.RECURSION) {
		//System.err.println("Visiting StrType: " + " mode: " + mode);
		for (AST.CompDefn compDefn : strType.comps) {
			//System.err.println("CompDefn: " + compDefn.name);
			TYP.Type compType = SemAn.ofType.get(compDefn);
            if (compType == null) {
                throw new Report.Error(compDefn.location(), "Component type is not resolved during recursion.");
            }
			new_typs.add(compType);
            //resolvingTypes.add(compType);
		}

		return SemAn.isType.get(strType);
	}
    if (mode == Mode.DECLARE){return null;}
   // System.err.println("Visiting StrType, mode: " + mode);

    // List to store resolved component types
    List<TYP.Type> componentTypes = new ArrayList<>();

    // Resolve each component and collect its type
    for (AST.CompDefn compDefn : strType.comps) {
       // System.err.println("CompDefn: " + compDefn.name);
       // System.err.println("CompDefn: " + compDefn.type);
        TYP.Type compType = compDefn.accept(this, mode);
        if (compType == null) {
            throw new Report.Error(compDefn.location(), "Component type is not resolved in compDefn.");
        }
		//typ::17
		if (compType == TYP.VoidType.type) {
			throw new Report.Error(compDefn.location(), "Component type cannot be void.");
		}
        componentTypes.add(compType);
    }
	

    // Create the struct type using the resolved component types
    TYP.StrType structType = new TYP.StrType(componentTypes);
    SemAn.isType.put(strType, structType);
	TypeResolver.str_hash.put(structType, strType);

    return structType;
}
//CompDefn
@Override
public TYP.Type visit(AST.CompDefn compDefn, Mode mode) {
	if (mode == Mode.RECURSION) {
        //System.err.println("Visiting CompDefn: " + " mode: " + mode);
        TYP.Type compType = SemAn.ofType.get(compDefn);
        if (compType == null) {
            throw new Report.Error(compDefn.location(), "Component type in compDefn is not resolved during recursion.");
        }
		return SemAn.ofType.get(compDefn);
	}
    //System.err.println("Visiting CompDefn: " + compDefn.name + " mode: " + mode);

    // Resolve the type of the component
    TYP.Type compType = compDefn.type.accept(this, mode);
    if (compType == null) {
        throw new Report.Error(compDefn.location(), "Component type is not resolved.");
    }

    SemAn.ofType.put(compDefn, compType);
    //System.err.println("Resolved CompDefn " + compDefn.name + " to: " + compType);
    return compType;
}
//RecType
@Override
public TYP.Type visit(AST.UniType uniType, Mode mode){
	//System.err.println("Visiting UniType: " + " mode: " + mode);
	List<TYP.Type> compDefns = new ArrayList<>();
    if (mode == Mode.DECLARE){return null;}
    if (mode == Mode.RECURSION) {
        //System.err.println("Visiting UniType: " + " mode: " + mode);
        for (AST.CompDefn compDefn : uniType.comps) {
            //System.err.println("CompDefn: " + compDefn.name);
            TYP.Type compType = this.visit(compDefn, mode);
            if (compType == null) {
                throw new Report.Error(compDefn.location(), "Component type is not resolved during recursion.");
            }
            compDefns.add(compType);
            //resolvingTypes.add(compType);
        }

        return SemAn.isType.get(uniType);
    }
    //System.err.println("Visiting UniType, mode: " + mode);
    List <TYP.Type> compTypes = new ArrayList<>();
    for (AST.CompDefn compDefn : uniType.comps) {
        //System.err.println("CompDefn: " + compDefn.name);
        TYP.Type compType = compDefn.accept(this, mode);
        if (compType == null) {
            throw new Report.Error(compDefn.location(), "Component type is not resolved in compDefn.");
        }
        //typ::17
        if (compType == TYP.VoidType.type) {
            throw new Report.Error(compDefn.location(), "Component type cannot be void.");
        }
        compTypes.add(compType);
    }
    TYP.UniType uniTypeResolved = new TYP.UniType(compTypes);
    SemAn.isType.put(uniType, uniTypeResolved);
    TypeResolver.uni_hash.put(uniTypeResolved, uniType);
	return uniTypeResolved;
}

public TYP.Type check_fun(TYP.Type type, AST.Node type_ast, boolean is_ret){
    if (type instanceof TYP.VoidType && !is_ret){
        throw new Report.Error(type_ast.location(), "Function cannot have void type.");
    }
    if (type instanceof TYP.StrType || type instanceof TYP.UniType){
        throw new Report.Error(type_ast.location(), "Function cannot have struct or union type.");
    }
    if (type instanceof TYP.NameType){
        if (type.actualType() == TYP.VoidType.type){
            throw new Report.Error(type_ast.location(), "Function cannot have void type.");
        }
        if (type.actualType() instanceof TYP.StrType || type.actualType() instanceof TYP.UniType){
            throw new Report.Error(type_ast.location(), "Function cannot have struct or union type.");
        }
        if (type.actualType() instanceof TYP.ArrType){
            throw new Report.Error(type_ast.location(), "Function cannot have array type.");
        }
    }
    if (type instanceof TYP.ArrType){
        throw new Report.Error(type_ast.location(), "Function cannot have array type.");
    }
    return type;
}
// FunType
@Override
public TYP.Type visit(AST.FunType funType, Mode mode) {
	//System.err.println("Visiting FunType: " + " mode: " + mode);
	if (mode == Mode.RESOLVE){	
		TYP.Type returnType = funType.resType.accept(this, mode);
        
	if (returnType == null) {
		throw new Report.Error(funType.location(), "Return type is not resolved.");
	}
    TYP.Type checked_return = check_fun(returnType, funType,true);
	// Resolve the parameter types of the function
	List<TYP.Type> paramTypes = new ArrayList<>();
	for (AST.Type param : funType.parTypes) {
		TYP.Type paramType = param.accept(this, mode);
		//System.err.println("ParamType: " + paramType + "Mode " + mode);
		if (paramType == null) {
			throw new Report.Error(param.location(), "Parameter type is not resolved.");
		}
		// TYP::4
		TYP.Type checked = check_fun(paramType,funType,false);
    
		paramTypes.add(paramType);
	}

	// Create the function type using the resolved return and parameter types
	TYP.FunType funTypeResolved = new TYP.FunType(paramTypes,returnType);

	// Store the resolved function type in SemAn.isType
	SemAn.isType.put(funType, funTypeResolved);
    
	//System.err.println("Resolved FunType to: " + funTypeResolved);
	return funTypeResolved;
	}
	return null;
}
//defFunDefn

@Override
public TYP.Type visit(AST.DefFunDefn defFunDefn, Mode mode) {
    //System.err.println("Visiting DefFunDefn: " + defFunDefn.name + " mode: " + mode);

    if (mode == Mode.DECLARE) {
        // Create a placeholder function type and store it in SemAn.ofType
        TYP.FunType funType = new TYP.FunType(new ArrayList<>(), TYP.VoidType.type);
        SemAn.ofType.put(defFunDefn, funType);
        // SemAn.isConst.put(defFunDefn, false);
        // SemAn.isAddr.put(defFunDefn, false);
        //System.err.println("Declared function " + defFunDefn.name + " with placeholder type.");
        for (AST.Stmt stmt : defFunDefn.stmts) {
            if (stmt instanceof AST.LetStmt) {
                stmt.accept(this, mode);
            }
        }
    } else if (mode == Mode.RESOLVE) {
        // Retrieve the placeholder function type
        TYP.FunType funType = (TYP.FunType) SemAn.ofType.get(defFunDefn);
        if (funType == null) {
            throw new Report.Error(defFunDefn.location(), "Function type is not declared.");
        }
        // Resolve the return type of the function
        TYP.Type returnType = defFunDefn.type.accept(this, mode);
        if (returnType == null) {
            throw new Report.Error(defFunDefn.location(), "Return type is not resolved.");
        }
        TYP.Type checked_return = check_fun(returnType,defFunDefn,true);
        List<TYP.Type> paramTypes = new ArrayList<>();
        for (AST.ParDefn param : defFunDefn.pars) {
            TYP.Type paramType = param.type.accept(this, mode);
            if (paramType == null) {
                throw new Report.Error(param.location(), "Parameter type is not resolved.");
            }
            TYP.Type checked = check_fun(paramType,defFunDefn,false);
			SemAn.ofType.put(param, paramType);
           // System.err.println("ParamType: " + paramType + "Type " + paramType);
            paramTypes.add(paramType);
        }
        for (AST.Stmt stmt : defFunDefn.stmts) {
            stmt.accept(this, mode);
        }
        // Create a new function type with resolved parameter and return types
        TYP.FunType resolvedFunType = new TYP.FunType(paramTypes, returnType);

        // Update the function type in SemAn.ofType
        SemAn.ofType.put(defFunDefn, resolvedFunType);

        //System.err.println("Resolved function " + defFunDefn.name + " to type: " + resolvedFunType);
    } else if (mode == Mode.RECURSION) {
        // Recursively resolve the function body if applicable
        if (defFunDefn.stmts != null) {
            for (AST.Stmt stmt : defFunDefn.stmts) {
                stmt.accept(this, mode);
            }
        }

    }

    return null;
}
//LetStmt
@Override
public TYP.Type visit(AST.LetStmt letStmt, Mode mode) {
    //System.err.println("Visiting LetStmt: " + " mode: " + mode);
    for (AST.FullDefn def : letStmt.defns) {
                def.accept(this, mode);
            }
    //System.err.println("Visiting LetStmt " + " Stmts");
    for (AST.Stmt stmt : letStmt.stmts) {
        stmt.accept(this, mode);
    }
    
    return null;
}


//ParDefn
@Override
public TYP.Type visit(AST.ParDefn parDefn, Mode mode) {
	//System.err.println("Visiting ParDefn: " + parDefn.name + " mode: " + mode);

	// Resolve the type of the parameter
	TYP.Type paramType = parDefn.type.accept(this, mode);
	if (paramType == null) {
		throw new Report.Error(parDefn.location(), "Parameter type is not resolved.");
	}

	// Store the resolved type in SemAn.ofType
	SemAn.ofType.put(parDefn, paramType);

	//System.err.println("Resolved ParDefn " + parDefn.name + " to: " + paramType);
	return paramType;
}

@Override
public TYP.Type visit(AST.ExtFunDefn extFunDefn, Mode mode) {
    //System.err.println("Visiting ExtFunDefn: " + extFunDefn.name + " mode: " + mode);

    if (mode == Mode.DECLARE) {
        // Create a placeholder function type and store it in SemAn.ofType
        TYP.FunType funType = new TYP.FunType(new ArrayList<>(), TYP.VoidType.type);
        SemAn.ofType.put(extFunDefn, funType);
       // System.err.println("Declared external function " + extFunDefn.name + " with placeholder type.");
    } else if (mode == Mode.RESOLVE) {
        TYP.FunType funType = (TYP.FunType) SemAn.ofType.get(extFunDefn);
        if (funType == null) {
            throw new Report.Error(extFunDefn.location(), "External function type is not declared.");
        }
        TYP.Type returnType = extFunDefn.type.accept(this, mode);
        if (returnType == null) {
            throw new Report.Error(extFunDefn.location(), "Return type of external function is not resolved.");
        }
        TYP.Type checkedReturnType = check_fun(returnType, extFunDefn, true);

        // Resolve the parameter types of the external function
        List<TYP.Type> paramTypes = new ArrayList<>();
        for (AST.ParDefn param : extFunDefn.pars) {
            TYP.Type paramType = param.type.accept(this, mode);
            if (paramType == null) {
                throw new Report.Error(param.location(), "Parameter type of external function is not resolved.");
            }
            TYP.Type checkedParamType = check_fun(paramType, extFunDefn, false);
            SemAn.ofType.put(param, paramType);
            paramTypes.add(paramType);
        }

        // Create a new function type with resolved parameter and return types
        TYP.FunType resolvedFunType = new TYP.FunType(paramTypes, returnType);

        // Update the function type in SemAn.ofType
        SemAn.ofType.put(extFunDefn, resolvedFunType);

       // System.err.println("Resolved external function " + extFunDefn.name + " to type: " + resolvedFunType);
    } else if (mode == Mode.RECURSION) {
        // External functions typically do not require recursion handling
        return null;
    }

    return null;
}
 public TYP.Type visit(AST.CastExpr castExpr, Mode mode) {
    if (mode == Mode.DECLARE) {
        castExpr.type.accept(this, mode);
        return null;
    }
    if (mode == Mode.RECURSION){return null;}
   // System.err.println("Visiting CastExpr: " + castExpr.type + " mode: " + mode);
    TYP.Type castType = castExpr.type.accept(this, mode);
    if (castType == null) {
        throw new Report.Error(castExpr.location(), "Cast type is not resolved.");
    }
   // System.err.println("Resolved CastExpr to in TypeReolver: " + castType);
    SemAn.ofType.put(castExpr, castType);
    return castType;

}
@Override
    public TYP.Type visit(AST.WhileStmt whileStmt, Mode mode){
        whileStmt.condExpr.accept(this, mode);
        for (AST.Stmt stmt : whileStmt.stmts) {
            stmt.accept(this, mode);
        }
        return null;
    }
@Override
    public TYP.Type visit(AST.IfThenStmt ifThenStmt, Mode mode){
        ifThenStmt.condExpr.accept(this, mode);
        for (AST.Stmt stmt : ifThenStmt.thenStmt) {
            stmt.accept(this, mode);
        }
        return null;
    }
    public TYP.Type visit(AST.IfThenElseStmt ifThenElseStmt, Mode mode){
        ifThenElseStmt.condExpr.accept(this, mode);
        for (AST.Stmt stmt : ifThenElseStmt.thenStmt) {
            stmt.accept(this, mode);
        }
        for (AST.Stmt stmt : ifThenElseStmt.elseStmt) {
            stmt.accept(this, mode);
        }
        return null;
    }

}


package compiler.phase.seman;

import java.util.*;

import compiler.common.report.*;
import compiler.phase.abstr.*;

/**
 * Type checker.
 */
public class TypeChecker implements AST.FullVisitor<TYP.Type, TypeChecker.Mode> {

    public TypeChecker() {
    }

    public enum Mode {
        CHECK,
        RESOLVE,
        UNISTR
    }

    // Maps and helper structures
	public final Map<String, TYP.StrType> structTypes = new HashMap<>();
    public final Map<String, TYP.UniType> unionTypes = new HashMap<>();
    public Map<String, TYP.Type> fields = new HashMap<>();
    public Map<String, AST.UniType> types_uni = new HashMap<>();
    public Map<String, AST.StrType> types_str = new HashMap<>();
    public Boolean isMain = false;
    public TYP.UniType getUnionType(String name) {
        return unionTypes.get(name);
    }

    public TYP.StrType getStructType(String name) {
        return structTypes.get(name);
    }

    public TYP.Type isAssignable(TYP.Type lhsType, TYP.Type rhsType) {
        if (lhsType instanceof TYP.NameType) {
            TYP.Type resolvedLhs = lhsType.actualType();
            if (resolvedLhs.equals(rhsType)) {
                return lhsType;
            }
        }
        if (rhsType instanceof TYP.NameType) {
            TYP.Type resolvedRhs = rhsType.actualType();
            if (lhsType.equals(resolvedRhs)) {
                return rhsType;
            }
        }

        lhsType = lhsType.actualType();
        rhsType = rhsType.actualType();
        //System.err.println("lhsType: " + lhsType);
        //System.err.println("rhsType: " + rhsType);

        if (lhsType.equals(rhsType)) {
            return lhsType;
        }

        if ((lhsType instanceof TYP.IntType && rhsType instanceof TYP.IntType) ||
            (lhsType instanceof TYP.CharType && rhsType instanceof TYP.CharType) ||
            (lhsType instanceof TYP.BoolType && rhsType instanceof TYP.BoolType) ||
            (lhsType instanceof TYP.VoidType && rhsType instanceof TYP.VoidType)) {
            return lhsType;
        }

        if (lhsType instanceof TYP.PtrType && rhsType instanceof TYP.PtrType) {
            TYP.PtrType lhsPtr = (TYP.PtrType) lhsType;
            TYP.PtrType rhsPtr = (TYP.PtrType) rhsType;

            if (rhsPtr.baseType instanceof TYP.VoidType) {
                return lhsType;
            }
            return isAssignable(lhsPtr.baseType, rhsPtr.baseType);
        }
        if (lhsType instanceof TYP.ArrType) {
            TYP.ArrType lhsArr = (TYP.ArrType) lhsType;

            // Check if the RHS type matches the element type of the array
            if (isAssignable(lhsArr.elemType, rhsType) != null) {
                return lhsType;
            }
        }

        if (lhsType instanceof TYP.ArrType && rhsType instanceof TYP.ArrType) {
            TYP.ArrType lhsArr = (TYP.ArrType) lhsType;
            TYP.ArrType rhsArr = (TYP.ArrType) rhsType;

            if (lhsArr.numElems.equals(rhsArr.numElems)) {
                TYP.Type elemType = isAssignable(lhsArr.elemType, rhsArr.elemType);
                if (elemType != null) {
                    return lhsType;
                }
            }
        }

        if (lhsType instanceof TYP.StrType && rhsType instanceof TYP.StrType) {
            TYP.StrType lhsStr = (TYP.StrType) lhsType;
            TYP.StrType rhsStr = (TYP.StrType) rhsType;

            if (lhsStr.compTypes.size() != rhsStr.compTypes.size()) {
                return null;
            }
            for (int i = 0; i < lhsStr.compTypes.size(); i++) {
                if (isAssignable(lhsStr.compTypes.get(i), rhsStr.compTypes.get(i)) == null) {
                    return null;
                }
            }
            return lhsType;
        }
        

        if (lhsType instanceof TYP.UniType && rhsType instanceof TYP.UniType) {
            TYP.UniType lhsUni = (TYP.UniType) lhsType;
            TYP.UniType rhsUni = (TYP.UniType) rhsType;

            if (lhsUni.compTypes.size() != rhsUni.compTypes.size()) {
                return null;
            }
            for (int i = 0; i < lhsUni.compTypes.size(); i++) {
                if (isAssignable(lhsUni.compTypes.get(i), rhsUni.compTypes.get(i)) == null) {
                    return null;
                }
            }
            return lhsType;
        }

        if (lhsType instanceof TYP.FunType && rhsType instanceof TYP.FunType) {
            TYP.FunType lhsFun = (TYP.FunType) lhsType;
            TYP.FunType rhsFun = (TYP.FunType) rhsType;

            if (lhsFun.parTypes.size() != rhsFun.parTypes.size()) {
                return null;
            }
            for (int i = 0; i < lhsFun.parTypes.size(); i++) {
                if (isAssignable(lhsFun.parTypes.get(i), rhsFun.parTypes.get(i)) == null) {
                    return null;
                }
            }
            if (isAssignable(lhsFun.resType, rhsFun.resType) != null) {
                return lhsType;
            }
        }

        return null;
    }

    @Override
    public TYP.Type visit(AST.Nodes<? extends AST.Node> nodes, Mode mode) {
        if (nodes == null) return null;
        if (mode == null) {
            for (AST.Node node : nodes) {
				//check if instance of ComPDefn or TypDefn
				if (node != null) {
					node.accept(this, Mode.CHECK);
				}
                if (node instanceof AST.DefFunDefn) {
                    AST.DefFunDefn defFunDefn = (AST.DefFunDefn) node;
                }
				
            }
            for (AST.Node node : nodes) {
                if (node != null) {
                    node.accept(this, Mode.RESOLVE);
                }
            }
        } else {
            for (AST.Node node : nodes) {
                if (node != null) {
                    node.accept(this, mode);
                }
            }
        }
        if (!TypeResolver.isMain) {
            throw new Report.Error("Main function not found.");
        }
        return null;
    }

    @Override
    public TYP.Type visit(AST.VarDefn varDefn, Mode mode) {
        //System.err.println("Visiting VarDefn: " + varDefn.name);
        TYP.Type varType = SemAn.isType.get(varDefn.type);
        if (varType == null) {
            throw new Report.Error(varDefn, "Type not found");
        }
        if(varType instanceof TYP.VoidType){
            throw new Report.Error(varDefn, "Variable cannot be of type void");
        }
        SemAn.ofType.put(varDefn, varType);
       // SemAn.isAddr.put(varDefn, false);
       // SemAn.isConst.put(varDefn, true);
        //System.err.println("VarDefn: " + varDefn.name + " declared with type " + varType);
        return varType;
    }

    // NameExpr
    @Override
    public TYP.Type visit(AST.NameExpr nameExpr, Mode mode) {
        //System.err.println("Visiting NameExpr: " + nameExpr.name + " mode: " + mode);
        if (mode == Mode.CHECK) {
            SemAn.isConst.put(nameExpr, false);
            SemAn.isAddr.put(nameExpr, true);
           // System.err.println("getting defat");
            AST.Defn defAt = SemAn.defAt.get(nameExpr);
            //System.err.println("getting type " + defAt.name);
            if (defAt instanceof AST.TypDefn) {	
                SemAn.isAddr.put(nameExpr, false);
                //System.err.println("getting type for TypDefn " + defAt.name);
				SemAn.ofType.put(nameExpr, SemAn.isType.get(defAt));	
				return SemAn.isType.get(defAt);

			} else {
				// It's a variable, function, parameter, etc.
                //System.err.println("getting type " + defAt.name);
				SemAn.ofType.put(nameExpr, SemAn.ofType.get(defAt));
                //System.err.println("oks for Type");
                //System.out.println("SemAn.ofType.get(defAt);" + SemAn.ofType.get(defAt));
				return SemAn.ofType.get(defAt);
			}
        }

        AST.Defn defn = SemAn.defAt.get(nameExpr);
        if (defn == null) {
            throw new Report.Error(nameExpr, "Name not declared");
        }
        TYP.Type ofType = SemAn.ofType.get(defn);
        if (ofType == null) {
            throw new Report.Error(nameExpr, "Type not found");
        }
        SemAn.ofType.put(nameExpr, ofType);
        SemAn.isConst.put(nameExpr, false);
        SemAn.isAddr.put(nameExpr,true );
        if (ofType instanceof TYP.FunType) {
            SemAn.isAddr.put(nameExpr, false);
        }

       // System.err.println("NameExpr: " + nameExpr.name + " has type " + ofType);
        return ofType;
    }

    @Override
    public TYP.Type visit(AST.DefFunDefn defFunDefn, Mode mode) {
       // System.err.println("Visiting DefFunDefn: " + defFunDefn.name + " mode: " + mode);

        if (mode == Mode.CHECK) {
            for (AST.Stmt stmt : defFunDefn.stmts) {
                stmt.accept(this, mode);
            }
        } else if (mode == Mode.RESOLVE) {
            TYP.FunType funType = (TYP.FunType) SemAn.ofType.get(defFunDefn);
            if (funType == null) {
                throw new Report.Error(defFunDefn.location(), "Function type is not declared.");
            }
            for (AST.Stmt stmt : defFunDefn.stmts) {
                TYP.Type stmtType = stmt.accept(this, mode);
                if (stmt instanceof AST.ReturnStmt) {
                    if (stmtType == null) {
                        throw new Report.Error(defFunDefn.location(), "Return statement type not found.");
                    }
                    if (isAssignable(funType.resType, stmtType) == null) {
                        throw new Report.Error(defFunDefn.location(), "Return statement type does not match function type.");
                    }
                }
            }
        }
        return null;
    }

    @Override
    public TYP.Type visit(AST.AtomExpr atomExpr, Mode mode) {
       // System.err.println("Visiting AtomExpr");
        TYP.Type resolvedType;
        switch (atomExpr.type) {
            case INT:
                resolvedType = TYP.IntType.type;
                break;
            case CHAR:
                resolvedType = TYP.CharType.type;
                break;
            case BOOL:
                resolvedType = TYP.BoolType.type;
                break;
            case STR:
            case PTR:
                resolvedType = TYP.PtrType.type;
                break;
            default:
                throw new Report.Error(atomExpr.location(), "Unknown atomic type.");
        }
        SemAn.ofType.put(atomExpr, resolvedType);
        SemAn.isConst.put(atomExpr, true);
        SemAn.isAddr.put(atomExpr, false);
       // System.err.println("resolvedType: " + resolvedType);
        return resolvedType;
    }

    @Override
    public TYP.Type visit(AST.BinExpr binExpr, Mode mode) {
       // System.err.println("Visiting BinExpr" + " oper" + binExpr.oper + " mode: " + mode);
        if (mode == Mode.CHECK) {  
           // System.err.println("visiting BinExpr" + " oper" + binExpr.oper);
            binExpr.fstExpr.accept(this, mode);
           // System.err.println("fstExpr: " + binExpr.fstExpr);
            binExpr.sndExpr.accept(this, mode);
           // System.err.println("fstExpr: " + binExpr.sndExpr);
            return null;
        }
        else{
           // System.err.println("checking left side");
            binExpr.fstExpr.accept(this, mode);
          //  System.err.println("fstExpr: " + binExpr.fstExpr);
            binExpr.sndExpr.accept(this, mode);
            TYP.Type leftType = SemAn.ofType.get(binExpr.fstExpr);
            TYP.Type rightType = SemAn.ofType.get(binExpr.sndExpr);
          //  System.err.println("visiting BinExpr" + " oper" + binExpr.oper);
          //  System.err.println("leftType: " + leftType);
          //  System.err.println("rightType: " + rightType);
            TYP.Type resolvedType = null;

            switch (binExpr.oper) {
                case MUL:
                case DIV:
                case ADD:
                case SUB:
                case MOD:
                    if (leftType.actualType() != TYP.IntType.type || rightType.actualType() != TYP.IntType.type) {
                        throw new Report.Error(binExpr.location(), "Operands must be of type int.");
                    }
                    resolvedType = TYP.IntType.type;
                    break;
                case EQU:
                case NEQ:
                case LTH:
                case GTH:
                case LEQ:
                case GEQ:
                    if (leftType.actualType() == TYP.VoidType.type || rightType.actualType() == TYP.VoidType.type) {
                        throw new Report.Error(binExpr.location(), "Operands must not be of type void.");
                    }
                    TYP.Type assignable = isAssignable(leftType, rightType);
                    //System.err.println("assignable: " + assignable);
                    if (assignable == null) {
                        throw new Report.Error(binExpr.location(), "Operands must be of the same type.");
                    }
                    if ( binExpr.oper == AST.BinExpr.Oper.EQU || binExpr.oper == AST.BinExpr.Oper.NEQ) {
                        resolvedType = TYP.BoolType.type;
                        break;
                    }
                    resolvedType = TYP.BoolType.type;
                    break;
                case OR:
                case AND:
                    if (leftType.actualType() != TYP.BoolType.type || rightType.actualType() != TYP.BoolType.type) {
                        throw new Report.Error(binExpr.location(), "Operands must be of type bool.");
                    }
                    TYP.Type assignable2 = isAssignable(leftType, rightType);
                    if (assignable2 == null) {
                        throw new Report.Error(binExpr.location(), "Operands must be of the same type.");
                    }
                    resolvedType = assignable2;
                    break;
            }
            SemAn.ofType.put(binExpr, resolvedType);
            //System.err.println("resolvedType: " + resolvedType);
            // System.err.println("leftType: " + leftType + " left name " + binExpr.fstExpr);
            // System.err.println("rightType: " + rightType + " right name " + binExpr.sndExpr);
            SemAn.isConst.put(binExpr, SemAn.isConst.get(binExpr.fstExpr) && SemAn.isConst.get(binExpr.sndExpr));
            SemAn.isAddr.put(binExpr, false);
            //System.err.println("isAddr: " + SemAn.isAddr.get(binExpr));
            return resolvedType;
        }
        
    }

    @Override
    public TYP.Type visit(AST.PfxExpr pfxExpr, Mode mode) {
        if(Mode.CHECK == mode){
           // System.err.println("Visiting PfxExpr");
            pfxExpr.subExpr.accept(this, mode);
            return null;
        }
       // System.err.println("Visiting PfxExpr");
        TYP.Type exprType = pfxExpr.subExpr.accept(this, mode);
       // System.err.println("exprType: " + exprType);
        exprType = SemAn.ofType.get(pfxExpr.subExpr);
        TYP.Type resolvedType;
        if (exprType == null) {
            throw new Report.Error(pfxExpr.location(), "Type not found pfxE.");
        }
        switch (pfxExpr.oper) {
            case ADD:
            case SUB:
                if (exprType.actualType() != TYP.IntType.type) {
                    throw new Report.Error(pfxExpr.location(), "Operand must be of type int. it is " + exprType.actualType());
                }
                resolvedType = TYP.IntType.type;
                break;
            case NOT:
                if (exprType.actualType() != TYP.BoolType.type) {
                    throw new Report.Error(pfxExpr.location(), "Operand must be of type bool.");
                }
                resolvedType = exprType;
                break;
            case PTR:
                if (exprType.actualType() instanceof TYP.VoidType) {
                    throw new Report.Error(pfxExpr.location(), "Operand must not be of type void.");
                }
                //check if addresable 
                if (!SemAn.isAddr.get(pfxExpr.subExpr)) {
                    throw new Report.Error(pfxExpr.subExpr, "Operand must be an address.");
                }
                resolvedType = new TYP.PtrType(exprType);
                SemAn.isAddr.put(pfxExpr, false);
                SemAn.isConst.put(pfxExpr, false);
                SemAn.ofType.put(pfxExpr, resolvedType);
               // System.err.println("resolvedType in pfx: " + resolvedType);
                return resolvedType;
            default:
                throw new Report.Error(pfxExpr.location(), "Invalid prefix operation.");
        }

        SemAn.ofType.put(pfxExpr, resolvedType);
        SemAn.isConst.put(pfxExpr, SemAn.isConst.get(pfxExpr.subExpr));
        SemAn.isAddr.put(pfxExpr, false);
        return resolvedType;
    }

    @Override
    public TYP.Type visit(AST.ArrExpr arrExpr, Mode mode) {
        //System.out.println("Visiting ArrExpr: " + arrExpr.arrExpr + " mode: " + mode);  
         if (mode == Mode.CHECK) {
             arrExpr.arrExpr.accept(this, mode);
             arrExpr.idx.accept(this, mode);
             return SemAn.ofType.get(arrExpr.arrExpr);
         }
        TYP.Type arrExpr_type_123 = arrExpr.arrExpr.accept(this, mode);
        TYP.Type arrExpr_type = SemAn.ofType.get(arrExpr.arrExpr);
        //System.out.println("arrExpr_type: " + arrExpr_type);
        if (arrExpr_type.actualType() instanceof TYP.PtrType ptrType) {
        arrExpr_type = ptrType.baseType.actualType();
        }

        // Check if the resolved type is an array
        if (!(arrExpr_type instanceof TYP.ArrType)) {
            throw new Report.Error(arrExpr.arrExpr, "Expression must be of array type. but got " + arrExpr_type.actualType() + " or " + arrExpr_type);
        }
        

        TYP.Type indexType = arrExpr.idx.accept(this, mode);
        if (!(indexType.actualType() instanceof TYP.IntType)) {
            throw new Report.Error(arrExpr.idx, "Index must be of type int.");
        }

        TYP.Type elemType = ((TYP.ArrType) arrExpr_type.actualType()).elemType;
        //check if the left side is an address
        if (!SemAn.isAddr.get(arrExpr.arrExpr)) {
            throw new Report.Error(arrExpr.arrExpr, "Array must be an address.");
        }

        SemAn.isAddr.put(arrExpr, true);
        SemAn.isConst.put(arrExpr,false);
        SemAn.ofType.put(arrExpr, elemType);
        return elemType;
    }


   
    @Override
    public TYP.Type visit(AST.CompExpr compExpr, Mode mode) {
        //System.out.println("Visiting CompExpr: " + compExpr.name + " mode: " + mode);
        SemAn.isAddr.put(compExpr, true);
        SemAn.isConst.put(compExpr, false);
        if (mode == Mode.CHECK){
			compExpr.recExpr.accept(this, mode);
            TYP.Type recType = SemAn.ofType.get(compExpr.recExpr);
			if (recType == null)
				throw new Report.Error(compExpr, compExpr.name +"The type not found.");

			recType = recType.actualType();

			TYP.Types<TYP.Type> comps;
			if (recType instanceof TYP.StrType strType) {
				comps = strType.compTypes;

				AST.StrType astStr = TypeResolver.str_hash.get(strType);
				if (astStr != null) {
					for (int i = 0; i < astStr.comps.size(); i++) {
						if (astStr.comps.get(i).name.equals(compExpr.name)) {
							TYP.Type comp = comps.get(i);
							SemAn.ofType.put(compExpr, comp);
                            //System.err.println("compExpr: " + compExpr.name + " has type " + comp);
							return comp;
						}
					}
				}

			} else if (recType instanceof TYP.UniType uniType) {
				comps = uniType.compTypes;

				AST.UniType astUni = TypeResolver.uni_hash.get(uniType);
				if (astUni != null) {
					for (int i = 0; i < astUni.comps.size(); i++) {
						if (astUni.comps.get(i).name.equals(compExpr.name)) {
							TYP.Type comp = comps.get(i);
							SemAn.ofType.put(compExpr, comp);
							return comp;
						}
					}
				}

			} else {
				throw new Report.Error(compExpr, "It is not union or struct.");
			}

			throw new Report.Error(compExpr, " type does not have '" + compExpr.name + "'.");
		}
        
        //System.out.println("Visiting CompExpr: " + compExpr.name + " mode: " + mode + " recExpr: " + compExpr.recExpr);
        TYP.Type recType = SemAn.ofType.get(compExpr.recExpr).actualType();
        TYP.Types<TYP.Type> comps;
			if (recType instanceof TYP.StrType strType) {
				comps = strType.compTypes;

				AST.StrType astStr = TypeResolver.str_hash.get(strType);
                if (astStr != null) {
                    for (int i = 0; i < astStr.comps.size(); i++) {
                        if (astStr.comps.get(i).name.equals(compExpr.name)) {
                            TYP.Type comp = comps.get(i);
                            SemAn.ofType.put(compExpr, comp);
                            //System.err.println("compExpr: " + compExpr.name + " has type " + comp);
                            return comp;
                        }
                    }
                }
            } else if (recType instanceof TYP.UniType uniType) {
                comps = uniType.compTypes;
                AST.UniType astUni = TypeResolver.uni_hash.get(uniType);
                if (astUni != null) {
                    for (int i = 0; i < astUni.comps.size(); i++) {
                        if (astUni.comps.get(i).name.equals(compExpr.name)) {
                            TYP.Type comp = comps.get(i);
                            SemAn.ofType.put(compExpr, comp);
                            return comp;
                        }
                    }
                }
            }
            else {
                throw new Report.Error(compExpr, "It is not union or struct. but got " + recType.actualType());
            }

        return null;
    }


		private String findStructName(TYP.StrType strType) {
			for (Map.Entry<String, TYP.StrType> entry : structTypes.entrySet()) {
				if (entry.getValue() == strType) {
					return entry.getKey();
				}
			}
			return null;
		
	}

		

		@Override
		public TYP.Type visit(AST.SfxExpr sfxExpr, Mode mode) {
            SemAn.isAddr.put(sfxExpr, true);
            SemAn.isConst.put(sfxExpr, false);
			//System.err.println("Visiting SfxExpr");
            //System.err.println("sfxExpr: " + sfxExpr);
            //System.err.println("sfxExpr.subExpr: " + sfxExpr.subExpr);
			TYP.Type subType = sfxExpr.subExpr.accept(this, mode);
            if (mode == Mode.CHECK){
                return subType;
            }
            if (subType == null) {
                throw new Report.Error(sfxExpr, "Type not found.");
            }
        
            subType = subType.actualType();
        
            if (sfxExpr.subExpr instanceof AST.AtomExpr atom && atom.type == AST.AtomExpr.Type.INT) {
                subType = new TYP.PtrType(TYP.IntType.type);
            }
        
            if (!(subType instanceof TYP.PtrType)) {
                throw new Report.Error(sfxExpr, "Expression must be of pointer type.");
            }
        
            TYP.Type derefType = ((TYP.PtrType) subType).baseType.actualType();
            SemAn.ofType.put(sfxExpr, derefType);
           // System.err.println("derefType Sfx : " + derefType);
            return derefType;
        }
            
	

@Override
public TYP.Type visit(AST.TypDefn typeDefn, Mode mode) {
   // System.err.println("Visiting TypeDefn: " + typeDefn.name);
    TYP.Type type = SemAn.isType.get(typeDefn.type);
    if (type == null) {
        throw new Report.Error(typeDefn.location(), "Type not found in TypDefn.");
    }

    // If it's a struct type
    if (type.actualType() instanceof TYP.StrType) {
        TYP.StrType underlying = (TYP.StrType) type.actualType();
        // Store the TYP.StrType by the name from the typedef
        structTypes.put(typeDefn.name, underlying);

        // Also store the AST.StrType node so you can later access comps
        if (typeDefn.type instanceof AST.StrType) {
            AST.StrType astStr = (AST.StrType) typeDefn.type;
            types_str.put(typeDefn.name, astStr);
        }
       // System.err.println("TypeDefn: " + typeDefn.name + " declared as struct");
    }

    // If it's a union type
    else if (type.actualType() instanceof TYP.UniType) {
        TYP.UniType uni = (TYP.UniType) type.actualType();
        unionTypes.put(typeDefn.name, uni);

        if (typeDefn.type instanceof AST.UniType) {
            AST.UniType astUni = (AST.UniType) typeDefn.type;
            types_uni.put(typeDefn.name, astUni);
        }
       // System.err.println("TypeDefn: " + typeDefn.name + " declared as union");
    }

    return null;
}
    @Override
    public TYP.Type visit(AST.UniType uniType, Mode mode) {
        //System.err.println("Visiting UniType: " + uniType);
        for (AST.CompDefn compDefn : uniType.comps){
            compDefn.accept(this, mode);
        }
        return null;
    }
    @Override
    public TYP.Type visit(AST.StrType strType, Mode mode) {
        //System.err.println("Visiting StrType: " + strType);
        for (AST.CompDefn compDefn : strType.comps){
            compDefn.accept(this, mode);
        }
        return null;
    }



    @Override
    public TYP.Type visit(AST.CastExpr castExpr, Mode mode) {
        if (mode == Mode.CHECK) {
            castExpr.expr.accept(this, mode);
            castExpr.type.accept(this, mode);
            return null;
        }
       // System.err.println("Visiting CastExpr");
        castExpr.expr.accept(this, mode);
        castExpr.type.accept(this, mode);
        TYP.Type castType = SemAn.isType.get(castExpr.type);
        TYP.Type exprType = castExpr.expr.accept(this, mode);
      //  System.err.println("castType: " + castType +" : " +castExpr.type + " mode " + mode);
      //  System.err.println("exprType: " + exprType + " : " +  castExpr.expr + " mode " + mode);
        if (castType == null) {
            throw new Report.Error(castExpr.location(), "Type not found. type " + castExpr.type);
        }
        if (exprType == null) {
            throw new Report.Error(castExpr.location(), "Type not found. expr " + castExpr.expr);
        }
        if (castType == TYP.VoidType.type) {
            throw new Report.Error(castExpr.location(), "Casting to void is not allowed.");
        }
        if (exprType == TYP.VoidType.type) {
            throw new Report.Error(castExpr.location(), "Casting from void is not allowed.");
        }
        SemAn.isAddr.put(castExpr, SemAn.isAddr.get(castExpr.expr));
        SemAn.isConst.put(castExpr, SemAn.isConst.get(castExpr.expr));
        SemAn.ofType.put(castExpr, castType);
        return castType;
    }

    @Override
    public TYP.Type visit(AST.AssignStmt assignStmt, Mode mode) {
        if (mode == Mode.RESOLVE) {
          //  System.err.println("Visiting AssignStmt");
			assignStmt.dstExpr.accept(this, mode);
			assignStmt.srcExpr.accept(this, mode);

            TYP.Type lhsType = SemAn.ofType.get(assignStmt.dstExpr);
            TYP.Type rhsType = SemAn.ofType.get(assignStmt.srcExpr);
            if (lhsType == null) {
                throw new Report.Error(assignStmt.location(), "Type not found. lhs in assignStmt.");
            }
            if (rhsType == null) {
                throw new Report.Error(assignStmt.location(), "Type not found. rhs in assignStmt.");
            }
            TYP.Type assignable = isAssignable(lhsType, rhsType);
            if (assignable == null) {
                throw new Report.Error(assignStmt.location(), "Types are not assignable." + lhsType + " " + rhsType);
            }
            if(SemAn.isConst.get(assignStmt.dstExpr)){
                throw new Report.Error(assignStmt.location(), "Left side of assignment must not be constant.");
            }
            return null;
        } else {
            assignStmt.dstExpr.accept(this, mode);
            assignStmt.srcExpr.accept(this, mode);
        }
        return null;
    }

    @Override
    public TYP.Type visit(AST.ReturnStmt returnStmt, Mode mode) {
        //System.err.println("Visiting ReturnStmt");
        if (mode == Mode.CHECK) {
            returnStmt.retExpr.accept(this, mode);
            return null;
        }
        TYP.Type exprType = returnStmt.retExpr.accept(this, mode);
        if (exprType.actualType() == null) {
            throw new Report.Error(returnStmt.location(), "Type not found.");
        }
        return exprType;
    }

    @Override
    public TYP.Type visit(AST.WhileStmt whileStmt, Mode mode) {
       // System.err.println("Visiting WhileStmt");
        if (mode == Mode.RESOLVE) {
            whileStmt.condExpr.accept(this, mode);
            TYP.Type condType = SemAn.ofType.get(whileStmt.condExpr);
            if (condType == null) {
                throw new Report.Error(whileStmt.location(), "Type not found. whileStmt.");
            }
            if (condType.actualType() != TYP.BoolType.type) {
                throw new Report.Error(whileStmt.location(), "Condition must be of type bool.");
            }
            whileStmt.stmts.accept(this, mode);
            
        }
        else{
            whileStmt.condExpr.accept(this, mode);
            whileStmt.stmts.accept(this, mode);
        }
        return null;
    }

    @Override
    public TYP.Type visit(AST.IfThenStmt ifStmt, Mode mode) {
        if (mode == Mode.RESOLVE) {
            //System.err.println("Visiting IfStmt " + mode);
            TYP.Type condType = ifStmt.condExpr.accept(this, mode);
            if (condType == null) {
                throw new Report.Error(ifStmt.location(), "Type not found.");
            }
            if (condType.actualType() != TYP.BoolType.type) {
                throw new Report.Error(ifStmt.location(), "Condition must be of type bool.");
            }
            TYP.Type stmtType = ifStmt.thenStmt.accept(this, mode);
            return null;



        }
        else{
            ifStmt.condExpr.accept(this, mode);
            ifStmt.thenStmt.accept(this, mode);
        }

        return null;
    }

    @Override
    public TYP.Type visit(AST.IfThenElseStmt ifThenElseStmt, Mode mode) {
       // System.err.println("Visiting IfThenElseStmt");
       if (mode == Mode.RESOLVE) {
            TYP.Type condType = ifThenElseStmt.condExpr.accept(this, mode);
            if (condType == null) {
                throw new Report.Error(ifThenElseStmt.location(), "Type not found.");
            }
            if (condType.actualType() != TYP.BoolType.type) {
                throw new Report.Error(ifThenElseStmt.location(), "Condition must be of type bool.");
            }
            ifThenElseStmt.thenStmt.accept(this, mode);
            ifThenElseStmt.elseStmt.accept(this, mode);
            return null;
        }
        else{
            ifThenElseStmt.condExpr.accept(this, mode);
            ifThenElseStmt.thenStmt.accept(this, mode);
            ifThenElseStmt.elseStmt.accept(this, mode);
        }
        return null;
    }
    @Override
    public TYP.Type visit(AST.LetStmt letStmt, Mode mode) {
        if (mode == Mode.RESOLVE) {
            //System.err.println("Visiting LetStmt " + " Defns");
            for (AST.FullDefn def : letStmt.defns) {
                def.accept(this, mode);
            }
            //System.err.println("Visiting LetStmt " + " Stmts");
            for (AST.Stmt stmt : letStmt.stmts) {
                stmt.accept(this, mode);
            }
        }
        else{
           // System.err.println("Visiting LetStmt " + " Defns");
            for (AST.FullDefn def : letStmt.defns) {
                def.accept(this, mode);
            }
           // System.err.println("Visiting LetStmt " + " Stmts");
            for (AST.Stmt stmt : letStmt.stmts) {
                stmt.accept(this, mode);
            }
        }
        return null;
    }
    //SizeExpr
    @Override
    public TYP.Type visit(AST.SizeExpr sizeExpr, Mode mode) {
        SemAn.isAddr.put(sizeExpr, false);
        SemAn.isConst.put(sizeExpr, true);
        //System.err.println("Visiting SizeExpr");
        TYP.Type sizeExpr_type = sizeExpr.type.accept(this, Mode.CHECK);
        //System.err.println("got something");
        if (sizeExpr_type == null) {
            throw new Report.Error(sizeExpr.location(), "Type not found.");
        }
        sizeExpr_type = sizeExpr_type.actualType();
        if (sizeExpr_type == null) {
            throw new Report.Error(sizeExpr.location(), "Type not found.");
        }
        if (sizeExpr_type.actualType() == TYP.VoidType.type) {
            throw new Report.Error(sizeExpr.location(), "Size of void type is not allowed.");
        }
        SemAn.ofType.put(sizeExpr, TYP.IntType.type);
        return TYP.IntType.type;
    }
    //NameType 
    @Override
    public TYP.Type visit(AST.NameType nameType, Mode mode) {
       // System.err.println("Visiting NameType");
        AST.Defn defn = SemAn.defAt.get(nameType);
        if (defn == null) {
            throw new Report.Error(nameType.location(), "Type not found.");
        }
        TYP.Type type = SemAn.isType.get(defn);
        if (type == null) {
            throw new Report.Error(nameType.location(), "Type not found.");
        }
        SemAn.isType.put(nameType, type);
        return type;
    }
    
    //CallExpr 
    @Override
    public TYP.Type visit(AST.CallExpr callExpr, Mode mode) {
        if (mode == Mode.CHECK) {
            callExpr.funExpr.accept(this, mode);
            
            for (AST.Expr arg : callExpr.argExprs) {
                arg.accept(this, mode);
            }
            return null;
        }
        //System.err.println("Visiting CallExpr OOO whattt");
        TYP.Type funType = callExpr.funExpr.accept(this, mode).actualType();
        if (funType == null) {
            throw new Report.Error(callExpr.location(), "Function type not found.");
        }
        TYP.FunType fType = (TYP.FunType) funType;
    
        if (fType.parTypes.size() != callExpr.argExprs.size()) {
            throw new Report.Error(callExpr, "Number of arguments does not match the number of parameters.");
        }
        for (int i = 0; i < callExpr.argExprs.size(); i++) {
            //System.out.println("Visiting CallExpr arg: " + i + " " + callExpr.argExprs.get(i));
            TYP.Type argType = callExpr.argExprs.get(i).accept(this, mode);
            //argType = SemAn.ofType.get(callExpr.argExprs.get(i));
            //System.out.println("argType got this: " + argType);
            TYP.Type parType = fType.parTypes.get(i).actualType();
            //System.out.println("parType: " + parType);
            if (!argType.getClass().equals(parType.getClass())) {
                throw new Report.Error(callExpr.argExprs.get(i),
                        "Argument type does not match the parameter type. got " + argType + " but expected " + parType);
            
            }
            //System.out.println("Loop finished for arg: " + i);
    }
        SemAn.isAddr.put(callExpr, false);
        SemAn.isConst.put(callExpr, false);
        SemAn.ofType.put(callExpr, fType.resType);
        //System.err.println("CallExpr: " + callExpr.funExpr + " has type " + fType.resType);
        return fType.resType;
        
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
    
}
package compiler.phase.seman;

import java.util.*;

import compiler.common.report.*;
import compiler.phase.abstr.*;

/**
 * Name resolver.
 * 
 * The name resolver connects each node of a abstract syntax tree where a name
 * is used with the node where it is defined. The only exceptions are struct and
 * union component names which are connected with their definitions by the type
 * resolver. The results of the name resolver are stored in
 * {@link compiler.phase.seman.SemAn#defAt}.
 * 
 * @author bostjan.slivnik@fri.uni-lj.si
 */
public class NameResolver implements AST.FullVisitor<Object, NameResolver.Mode> {

	/** Constructs a new name resolver. */
	public NameResolver() {
	}

	/** Two passes of name resolving. */
	protected enum Mode {
		/** The first pass: declaring names. */
		DECLARE,
		/** The second pass: resolving names. */
		RESOLVE,
	}
	
	 


	/** The symbol table. */
	private SymbTable symbTable = new SymbTable();

	// *** TODO ***
	// override Result visitor
	@Override
	public Object visit(AST.Nodes<? extends AST.Node> nodes, Mode mode) {
        if (nodes == null) return null;
        if (mode == null) {
            for (AST.Node node : nodes) {
                if (node != null) {
                    node.accept(this, Mode.DECLARE);
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
        return null;
    	
    }
	// ===== DECLARATIONS =====

    @Override
    public Object visit(AST.TypDefn typeDef, Mode mode) {
        if (mode == Mode.DECLARE) {
            try {
                symbTable.ins(typeDef.name, typeDef);
            } catch (SymbTable.CannotInsNameException e) {
                throw new Report.Error(typeDef, "Type '" + typeDef.name + "' already declared.");
            }
			typeDef.type.accept(this, mode);
        }
		else {
			typeDef.type.accept(this, mode);
		}
		return null;
	}

	

    @Override
    public Object visit(AST.VarDefn varDef, Mode mode) {
        if (mode == Mode.DECLARE) {
            try {
                symbTable.ins(varDef.name, varDef);
            } catch (SymbTable.CannotInsNameException e) {
                throw new Report.Error(varDef, "Variable '" + varDef.name + "' already declared.");
            }
        }
		else {
			varDef.type.accept(this, Mode.DECLARE);
			varDef.type.accept(this, Mode.RESOLVE);
		}

        return null;
    }

    @Override
    public Object visit(AST.DefFunDefn funDef, Mode mode) {
        if (mode == Mode.DECLARE) {
            try {
                symbTable.ins(funDef.name, funDef);
            } catch (SymbTable.CannotInsNameException e) {
                throw new Report.Error(funDef, "Function '" + funDef.name + "' already declared.");
            }
			for (AST.ParDefn parDef : funDef.pars) {
				parDef.type.accept(this, Mode.DECLARE);
			}
		}
        else if (mode == Mode.RESOLVE) {
			funDef.type.accept(this, Mode.DECLARE);
			funDef.type.accept(this, Mode.RESOLVE);
			symbTable.newScope();
			for (AST.ParDefn parDef : funDef.pars) {
				parDef.accept(this, Mode.DECLARE);
			}
			for (AST.Stmt stmt : funDef.stmts) {
				stmt.accept(this, Mode.DECLARE);
			}
			for (AST.ParDefn parDef : funDef.pars) {
				parDef.accept(this, Mode.RESOLVE);
			}
			for (AST.Stmt stmt : funDef.stmts) {
				stmt.accept(this, Mode.RESOLVE);
			}
			symbTable.oldScope();
		}
		
		
        return null;
    }

    @Override
    public Object visit(AST.ExtFunDefn extFunDef, Mode mode) {
        if (mode == Mode.DECLARE) {
            try {
                symbTable.ins(extFunDef.name, extFunDef);
            } catch (SymbTable.CannotInsNameException e) {
                throw new Report.Error(extFunDef, "External function '" + extFunDef.name + "' already declared.");
            }
			symbTable.newScope();
			for (AST.ParDefn parDef : extFunDef.pars) {
				parDef.accept(this, Mode.DECLARE);
			}
			for (AST.ParDefn parDef : extFunDef.pars) {
				parDef.accept(this, Mode.RESOLVE);
			}
			symbTable.oldScope();
			
        }
		else {
			extFunDef.type.accept(this, Mode.DECLARE);
			extFunDef.type.accept(this, Mode.RESOLVE);
		}
        return null;
    }

    @Override
    public Object visit(AST.ParDefn parDef, Mode mode) {
        if (mode == Mode.DECLARE) {
            try {
                symbTable.ins(parDef.name, parDef);
            } catch (SymbTable.CannotInsNameException e) {
                throw new Report.Error(parDef, "Parameter '" + parDef.name + "' already declared.");
            }
        }
		if (mode == Mode.RESOLVE) {
			parDef.type.accept(this, Mode.DECLARE);
			parDef.type.accept(this, Mode.RESOLVE);
		}
		
        return null;
    }

    @Override
    public Object visit(AST.CompDefn compDef, Mode mode) {
        if (mode == Mode.DECLARE) {
            try {
                symbTable.ins(compDef.name, compDef);
            } catch (SymbTable.CannotInsNameException e) {
                throw new Report.Error(compDef, "Component '" + compDef.name + "' already declared.");
            }
			compDef.type.accept(this, mode);
        }
		else if (mode == Mode.RESOLVE) {
			compDef.type.accept(this, mode);
		}
        return null;
    }
	@Override
	public Object visit(AST.FunType funDefn, Mode mode) {
		//System.err.println("Visiting FunType: " + funDefn + " in mode " + mode);
		for (AST.Type parType : funDefn.parTypes) {
			parType.accept(this, mode);
		}
		funDefn.resType.accept(this, mode);
		return null;
	}


    // ===== STATEMENTS =====

    @Override
    public Object visit(AST.LetStmt letStmt, Mode mode) {
		symbTable.newScope();
        if (letStmt.defns != null){
            letStmt.defns.accept(this, null);
		}
        if (letStmt.stmts != null){
            letStmt.stmts.accept(this, mode);
		}
        symbTable.oldScope();
        return null;
	}



        

    // @Override
    // public Object visit(AST.AssignStmt assignStmt, Mode mode) {
    //     assignStmt.dstExpr.accept(this, mode);
    //     assignStmt.srcExpr.accept(this, mode);
    //     return null;
    // }

    // @Override
    // public Object visit(AST.ReturnStmt returnStmt, Mode mode) {
    //     returnStmt.retExpr.accept(this, mode);
    //     return null;
    // }

    // @Override
    // public Object visit(AST.IfThenStmt ifThenStmt, Mode mode) {
    //     ifThenStmt.condExpr.accept(this, mode);
    //     for (AST.Stmt stmt : ifThenStmt.thenStmt) {
    //         stmt.accept(this, mode);
    //     }
    //     return null;
    // }

    // @Override
    // public Object visit(AST.IfThenElseStmt ifThenElseStmt, Mode mode) {
    //     ifThenElseStmt.condExpr.accept(this, mode);
    //     for (AST.Stmt stmt : ifThenElseStmt.thenStmt) {
    //         stmt.accept(this, mode);
    //     }
    //     for (AST.Stmt stmt : ifThenElseStmt.elseStmt) {
    //         stmt.accept(this, mode);
    //     }
    //     return null;
    // }

    // @Override
    // public Object visit(AST.WhileStmt whileStmt, Mode mode) {
    //     whileStmt.condExpr.accept(this, mode);
    //     for (AST.Stmt stmt : whileStmt.stmts) {
    //         stmt.accept(this, mode);
    //     }
    //     return null;
    // }
	// ===== EXPRESSIONS =====
	
    @Override
    public Object visit(AST.NameExpr nameExpr, Mode mode) {
        if (mode == Mode.RESOLVE) {
            try {
                AST.Defn defn = symbTable.fnd(nameExpr.name);
                SemAn.defAt.put(nameExpr, defn);
				//System.err.println("NameExpr: " + nameExpr.name+ " is declared in NameResolver " + nameExpr);
            } catch (SymbTable.CannotFndNameException e) {
                throw new Report.Error(nameExpr, "Name '" + nameExpr.name + "' not declared.");
            }
        }
        return null;
    }
	//CallExpr
	@Override
		public Object visit(AST.CallExpr callExpr, Mode mode) {
		callExpr.funExpr.accept(this, mode);
		
		for (AST.Expr expr : callExpr.argExprs) {
			expr.accept(this, mode);
		}
		return null;
	}
	 @Override
	public Object visit(AST.CastExpr castExpr, Mode mode) {
		castExpr.expr.accept(this, mode);
		castExpr.type.accept(this, mode);
		return null;
	}


	// // ArrExpr
	// @Override
	// public Object visit(AST.ArrExpr arrExpr, Mode mode) {
	// 	arrExpr.arrExpr.accept(this, mode);
	// 	return null;
	// }
	
	// //BinExpr
	// @Override
	// public Object visit(AST.BinExpr binExpr, Mode mode) {
	// 	binExpr.fstExpr.accept(this, mode);
	// 	binExpr.sndExpr.accept(this, mode);
	// 	return null;
	// }
	// //CallExpr
	// @Override
	// public Object visit(AST.CallExpr callExpr, Mode mode) {
	// 	callExpr.funExpr.accept(this, mode);
	// 	for (AST.Expr expr : callExpr.argExprs) {
	// 		expr.accept(this, mode);
	// 	}
	// 	return null;
	// }
	//CastExpr
	// @Override
	// public Object visit(AST.CastExpr castExpr, Mode mode) {
	// 	castExpr.expr.accept(this, mode);
	// 	return null;
	// }
	// //CompExpr
	// //SizeExpr
	// @Override
	// public Object visit(AST.SizeExpr sizeExpr, Mode mode) {
	// 	return null;
	// }

	// ===== TYPES =====
	
	
	public Object visit(AST.NameType nameType, Mode mode) {
		if (mode == Mode.DECLARE) {
			try {
				AST.Defn defn = symbTable.fnd(nameType.name);
				SemAn.defAt.put(nameType, defn);
			} catch (SymbTable.CannotFndNameException e) {
				throw new Report.Error(nameType, "Type '" + nameType.name + "' not declared.");
			}
			//System.err.println("NameType: " + nameType.name+ " is declared in NameResolver " + nameType);
		}
		
		return null;
	}
	

	// ===== SYMBOL TABLE =====

	/**
	 * A symbol table.
	 */
	public class SymbTable {

		/**
		 * A symbol table record denoting a definition of a name within a certain scope.
		 */
		private class ScopedDefn {

			/** The depth of the scope the definition belongs to. */
			public final int depth;

			/** The definition. */
			public final AST.Defn defn;

			/**
			 * Constructs a new record denoting a definition of a name within a certain
			 * scope.
			 * 
			 * @param depth The depth of the scope the definition belongs to.
			 * @param defn  The definition.
			 */
			public ScopedDefn(int depth, AST.Defn defn) {
				this.depth = depth;
				this.defn = defn;
			}

		}

		/**
		 * A mapping of names into lists of records denoting definitions at different
		 * scopes. At each moment during the lifetime of a symbol table, the definition
		 * list corresponding to a particular name contains all definitions that name
		 * within currently active scopes: the definition at the inner most scope is the
		 * first in the list and is visible, the other definitions are hidden.
		 */
		private final HashMap<String, LinkedList<ScopedDefn>> allDefnsOfAllNames;

		/**
		 * The list of scopes. Each scope is represented by a list of names defined
		 * within it.
		 */
		private final LinkedList<LinkedList<String>> scopes;

		/** The depth of the currently active scope. */
		private int currDepth;

		/** Whether the symbol table can no longer be modified or not. */
		private boolean lock;

		/**
		 * Constructs a new symbol table.
		 */
		public SymbTable() {
			allDefnsOfAllNames = new HashMap<String, LinkedList<ScopedDefn>>();
			scopes = new LinkedList<LinkedList<String>>();
			currDepth = 0;
			lock = false;
			newScope();
		}

		/**
		 * Returns the depth of the currently active scope.
		 * 
		 * @return The depth of the currently active scope.
		 */
		public int currDepth() {
			return currDepth;
		}

		/**
		 * Inserts a new definition of a name within the currently active scope or
		 * throws an exception if this name has already been defined within this scope.
		 * Once the symbol table is locked, any attempt to insert further definitions
		 * results in an internal error.
		 * 
		 * @param name The name.
		 * @param defn The definition.
		 * @throws CannotInsNameException Thrown if this name has already been defined
		 *                                within the currently active scope.
		 */
		public void ins(String name, AST.Defn defn) throws CannotInsNameException {
			if (lock)
				throw new Report.InternalError();

			LinkedList<ScopedDefn> allDefnsOfName = allDefnsOfAllNames.get(name);
			if (allDefnsOfName == null) {
				allDefnsOfName = new LinkedList<ScopedDefn>();
				allDefnsOfAllNames.put(name, allDefnsOfName);
			}

			if (!allDefnsOfName.isEmpty()) {
				ScopedDefn defnOfName = allDefnsOfName.getFirst();
				if (defnOfName.depth == currDepth)
					throw new CannotInsNameException();
			}

			allDefnsOfName.addFirst(new ScopedDefn(currDepth, defn));
			scopes.getFirst().addFirst(name);
			//System.out.println("Inserted into symbol table: " + name + " at depth " + currDepth);
		}

		/**
		 * Returns the currently visible definition of the specified name. If no
		 * definition of the name exists within these scopes, an exception is thrown.
		 * 
		 * @param name The name.
		 * @return The definition.
		 * @throws CannotFndNameException Thrown if the name is not defined within the
		 *                                currently active scope or any scope enclosing
		 *                                it.
		 */
		public AST.Defn fnd(String name) throws CannotFndNameException {
			LinkedList<ScopedDefn> allDefnsOfName = allDefnsOfAllNames.get(name);
			if (allDefnsOfName == null)
				throw new CannotFndNameException();

			if (allDefnsOfName.isEmpty())
				throw new CannotFndNameException();

			return allDefnsOfName.getFirst().defn;
		}

		/** Used for selecting the range of scopes. */
		public enum XScopeSelector {
			/** All live scopes. */
			ALL,
			/** Currently active scope. */
			ACT,
		}

		/**
		 * Constructs a new scope within the currently active scope. The newly
		 * constructed scope becomes the currently active scope.
		 */
		public void newScope() {
			if (lock)
				throw new Report.InternalError();

			currDepth++;
			scopes.addFirst(new LinkedList<String>());
		}

		/**
		 * Destroys the currently active scope by removing all definitions belonging to
		 * it from the symbol table. Makes the enclosing scope the currently active
		 * scope.
		 */
		public void oldScope() {
			if (lock)
				throw new Report.InternalError();

			if (currDepth == 0)
				throw new Report.InternalError();

			for (String name : scopes.getFirst()) {
				allDefnsOfAllNames.get(name).removeFirst();
			}
			scopes.removeFirst();
			currDepth--;
		}

		/**
		 * Prevents further modifications of this symbol table.
		 */
		public void lock() {
			lock = true;
		}

		/**
		 * An exception thrown when the name cannot be inserted into a symbol table.
		 */
		@SuppressWarnings("serial")
		public class CannotInsNameException extends Exception {

			/**
			 * Constructs a new exception.
			 */
			private CannotInsNameException() {
			}

		}

		/**
		 * An exception thrown when the name cannot be found in the symbol table.
		 */
		@SuppressWarnings("serial")
		public class CannotFndNameException extends Exception {

			/**
			 * Constructs a new exception.
			 */
			private CannotFndNameException() {
			}

		}

	}

}

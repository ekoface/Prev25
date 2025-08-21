parser grammar Prev25Parser;

@header {
    package compiler.phase.synan;
    import java.util.*;
    import compiler.common.report.*;
    import compiler.phase.lexan.*;
    import compiler.phase.abstr.AST;
}

@members {
    private Location loc(Token tok) { return new Location((LexAn.LocLogToken)tok); }
    private Location loc(Token tok1, Token tok2) { return new Location((LexAn.LocLogToken)tok1, (LexAn.LocLogToken)tok2); }
    private Location loc(Token tok1, Locatable loc2) { return new Location((LexAn.LocLogToken)tok1, loc2); }
    private Location loc(Locatable loc1, Token tok2) { return new Location(loc1, (LexAn.LocLogToken)tok2); }
    private Location loc(Locatable loc1, Locatable loc2) { return new Location(loc1, loc2); }
}

options {
    tokenVocab=Prev25Lexer;
}

// Entry point
source
    returns [AST.Nodes<AST.FullDefn> ast]
    : defs EOF
    { $ast = new AST.Nodes<AST.FullDefn>($defs.ast); }
    ;

defs
    returns [ArrayList<AST.FullDefn> ast]
    : def { $ast = new ArrayList<AST.FullDefn>(); $ast.add($def.ast); }
    | d=defs def { $ast = $d.ast; $ast.add($def.ast); }
    ;

//all starting here
def
    returns [AST.FullDefn ast]
    : typealias { $ast = $typealias.ast; }
    | vardeclaration { $ast = $vardeclaration.ast; }
    | fundeclaration { $ast = $fundeclaration.ast; }
    | fundefinition  { $ast = $fundefinition.ast;  }
    ;

// Type alias definition
typealias
    returns [AST.TypDefn ast]
    : TYP ID ASSIGN typeExpr
    { $ast = new AST.TypDefn(loc($TYP, $typeExpr.ast), $ID.getText(), $typeExpr.ast); }
    ;

// Variable declaration
vardeclaration
    returns [AST.VarDefn ast]
    : VAR ID COLON typeExpr
    { $ast = new AST.VarDefn(loc($VAR, $typeExpr.ast), $ID.getText(), $typeExpr.ast); }
    ;

// Function declaration
fundeclaration
    returns [AST.ExtFunDefn ast]
    : FUN ID LPAREN funParams RPAREN COLON typeExpr
    { $ast = new AST.ExtFunDefn(loc($FUN, $typeExpr.ast), $ID.getText(), $funParams.ast, $typeExpr.ast); }
    ;

// function definition
fundefinition
	returns  [AST.DefFunDefn ast]
	: FUN ID LPAREN funParams RPAREN COLON typeExpr ASSIGN statements
	{ $ast = new AST.DefFunDefn(loc($FUN, $statements.stop), $ID.getText(), $funParams.ast, $typeExpr.ast, $statements.ast); }
	;

// Function parameters
funParams
    returns [List<AST.ParDefn> ast]
    : paramList { $ast = $paramList.ast; }
    | { $ast = new ArrayList<>(); }
    ;

// Parameter list
paramList
    returns [List<AST.ParDefn> ast]
    : param { $ast = new ArrayList<>(); $ast.add($param.ast); }
    (COMMA param { $ast.add($param.ast); })*
    ;

// Single parameter
param
    returns [AST.ParDefn ast]
    : ID COLON typeExpr
    { $ast = new AST.ParDefn(loc($ID, $typeExpr.stop), $ID.getText(), $typeExpr.ast); }
    ;

paramList_CD
    returns [List<AST.CompDefn> ast]
    : param_CD { $ast = new ArrayList<>(); $ast.add($param_CD.ast); }
    (COMMA param_CD { $ast.add($param_CD.ast); })*
    ;

// Single parameter for CompDefn
param_CD
    returns [AST.CompDefn ast]
    : ID COLON typeExpr
    { $ast = new AST.CompDefn(loc($ID, $typeExpr.stop), $ID.getText(), $typeExpr.ast); }
    ;

// Zero or more statements
statements 
	returns [List<AST.Stmt> ast]
	: { $ast = new ArrayList<>(); }
	| statements_more { $ast = $statements_more.ast; }
	;

// One or more statements separated by commas
statements_more
	returns [List<AST.Stmt> ast]
	: statement { $ast = new ArrayList<>(); $ast.add($statement.ast); }
	(COMMA statement { $ast.add($statement.ast); })*
	;

statement
    returns [AST.Stmt ast]
    : expression { $ast = new AST.ExprStmt(loc($expression.start, $expression.stop), $expression.ast); }
    | lhs=expression ASSIGN rhs=expression { $ast = new AST.AssignStmt(loc($lhs.start, $rhs.stop), $lhs.ast, $rhs.ast); }
    | RETURN expression { $ast = new AST.ReturnStmt(loc($RETURN, $expression.stop), $expression.ast); }
    | whileStatement { $ast = $whileStatement.ast; }
    | ifThenStatement { $ast = $ifThenStatement.ast; }
    | ifThenElseStatement { $ast = $ifThenElseStatement.ast; }
    | letInStatement { $ast = $letInStatement.ast; }
    ;

// While loop
whileStatement returns [AST.WhileStmt ast]
	: WHILE expression DO statements END
	{ $ast = new AST.WhileStmt(loc($WHILE, $END), $expression.ast, $statements.ast); }
	;

// If-then statement
ifThenStatement returns [AST.IfThenStmt ast]
	: IF expression THEN statements END
	{ $ast = new AST.IfThenStmt(loc($IF, $END), $expression.ast, $statements.ast); }
	;


// If-then-else statement
ifThenElseStatement returns [AST.IfThenElseStmt ast]
	: IF expression THEN ls=statements ELSE rs=statements END
	{ $ast = new AST.IfThenElseStmt(loc($IF, $END), $expression.ast, $ls.ast, $rs.ast); }
	;

// Let-in expression
letInStatement returns [AST.LetStmt ast]
	: LET defs+ IN statements_more END
	{ $ast = new AST.LetStmt(loc($LET, $END), $defs.ast, $statements_more.ast); }
	;

// Type expression
typeExpr
returns [AST.Type ast]
    : INT { $ast = new AST.AtomType(loc($INT), AST.AtomType.Type.INT); }
	| CHAR { $ast = new AST.AtomType(loc($CHAR), AST.AtomType.Type.CHAR); }
	| BOOL { $ast = new AST.AtomType(loc($BOOL), AST.AtomType.Type.BOOL); }
	| VOID { $ast = new AST.AtomType(loc($VOID), AST.AtomType.Type.VOID); }
	| ID { $ast = new AST.NameType(loc($ID), $ID.text); }
	| LBRACKET INTEGER_CONSTANT RBRACKET typeExpr
	{ $ast = new AST.ArrType(loc($LBRACKET, $typeExpr.stop), $typeExpr.ast, $INTEGER_CONSTANT.text); }
	| CARET typeExpr { $ast = new AST.PtrType(loc($CARET, $typeExpr.stop), $typeExpr.ast); }
	| LESS plCD=paramList_CD GREATER
        {
            $ast = new AST.StrType(loc($LESS, $GREATER), $paramList_CD.ast);
        }
	| LBRACE plCD = paramList_CD RBRACE
		{
			$ast = new AST.UniType(loc($LBRACE, $RBRACE), $paramList_CD.ast) ;
		}

	| LPAREN typeList RPAREN COLON typeExpr
	{ $ast = new AST.FunType(loc($LPAREN, $typeExpr.ast), $typeList.ast, $typeExpr.ast); }
	;

typeList
	returns [List<AST.Type> ast]
	: { $ast = new ArrayList<>(); }
	| typeExpr { $ast = new ArrayList<>(); $ast.add($typeExpr.ast); }
	(COMMA typeExpr { $ast.add($typeExpr.ast); })*
	;


//EXPRESSIONS
expression returns [AST.Expr ast]
	: expr { $ast = $expr.ast; }
	;

expr returns [AST.Expr ast]
	: logic_or { $ast = $logic_or.ast; }
	;
logic_or returns [AST.Expr ast]
    : left=logic_or PIPE right=logic_and 
    { $ast = new AST.BinExpr(loc($left.start, $right.stop), AST.BinExpr.Oper.OR, $left.ast, $right.ast); }
    | logic_and { $ast = $logic_and.ast; }
    ;

logic_and returns [AST.Expr ast]
    : left=logic_and AMPERSAND right=comparison 
    { $ast = new AST.BinExpr(loc($left.start, $right.stop), AST.BinExpr.Oper.AND, $left.ast, $right.ast); }
    | comparison { $ast = $comparison.ast; }
    ;


comparison returns [AST.Expr ast]
    : left=additive EQUAL right=additive  { $ast = new AST.BinExpr(loc($left.start, $right.stop), AST.BinExpr.Oper.EQU, $left.ast, $right.ast); }
	| left=additive NOT_EQUAL right=additive  { $ast = new AST.BinExpr(loc($left.start, $right.stop), AST.BinExpr.Oper.NEQ, $left.ast, $right.ast); }
	| left=additive LESS right=additive  { $ast = new AST.BinExpr(loc($left.start, $right.stop), AST.BinExpr.Oper.LTH, $left.ast, $right.ast); }
	| left=additive GREATER right=additive  { $ast = new AST.BinExpr(loc($left.start, $right.stop), AST.BinExpr.Oper.GTH, $left.ast, $right.ast); }
	| left=additive LESS_EQUAL right=additive  { $ast = new AST.BinExpr(loc($left.start, $right.stop), AST.BinExpr.Oper.LEQ, $left.ast, $right.ast); }
	| left=additive GREATER_EQUAL right=additive  { $ast = new AST.BinExpr(loc($left.start, $right.stop), AST.BinExpr.Oper.GEQ, $left.ast, $right.ast); }
	| additive { $ast = $additive.ast; }
    ;


additive returns [AST.Expr ast]
	: left=additive op=PLUS right=multiplicative { $ast = new AST.BinExpr(loc($left.start, $right.stop), AST.BinExpr.Oper.ADD, $left.ast, $right.ast); }
	| left=additive op=MINUS right=multiplicative { $ast = new AST.BinExpr(loc($left.start, $right.stop), AST.BinExpr.Oper.SUB, $left.ast, $right.ast); }
    | multiplicative { $ast = $multiplicative.ast; }
    ;

multiplicative returns [AST.Expr ast]
	: left=multiplicative MULTIPLY right=unary  { $ast = new AST.BinExpr(loc($left.start, $right.stop), AST.BinExpr.Oper.MUL, $left.ast, $right.ast); }
	| left=multiplicative DIVIDE right=unary  { $ast = new AST.BinExpr(loc($left.start, $right.stop), AST.BinExpr.Oper.DIV, $left.ast, $right.ast); }
	| left=multiplicative MODULO right=unary  { $ast = new AST.BinExpr(loc($left.start, $right.stop), AST.BinExpr.Oper.MOD, $left.ast, $right.ast); }
    | unary { $ast = $unary.ast; }
    ;

unary returns [AST.Expr ast]
	: PLUS right=unary   { $ast = new AST.PfxExpr(loc($PLUS, $right.stop), AST.PfxExpr.Oper.ADD, $right.ast); }
	| MINUS right=unary  { $ast = new AST.PfxExpr(loc($MINUS, $right.stop), AST.PfxExpr.Oper.SUB, $right.ast); }
	| op=EXCLAMATION right=unary  { $ast = new AST.PfxExpr(loc($op, $right.stop), AST.PfxExpr.Oper.NOT, $right.ast); }
	| op=CARET right=unary  { $ast = new AST.PfxExpr(loc($op, $right.stop), AST.PfxExpr.Oper.PTR, $right.ast); }
    | postfix { $ast = $postfix.ast; }
    ;

postfix returns [AST.Expr ast]
    : primary { $ast = $primary.ast; }
    | left=postfix LBRACKET index=expr RBRACKET 
    { $ast = new AST.ArrExpr(loc($left.start, $RBRACKET), $left.ast, $index.ast); }
    | left=postfix DOT ID 
    { $ast = new AST.CompExpr(loc($left.start, $ID), $left.ast, $ID.text); }
    | left=postfix CARET 
    { $ast = new AST.SfxExpr(loc($left.start, $CARET), AST.SfxExpr.Oper.PTR, $left.ast); }
    | func=postfix LPAREN args=exprList RPAREN 
    { $ast = new AST.CallExpr(loc($func.start, $RPAREN), $func.ast, $args.ast); }
    ;

primary returns [AST.Expr ast]
    : INTEGER_CONSTANT { $ast = new AST.AtomExpr(loc($INTEGER_CONSTANT), AST.AtomExpr.Type.INT, $INTEGER_CONSTANT.text); }
    | CHAR_CONSTANT { $ast = new AST.AtomExpr(loc($CHAR_CONSTANT), AST.AtomExpr.Type.CHAR, $CHAR_CONSTANT.text); }
    | STRING_CONSTANT { $ast = new AST.AtomExpr(loc($STRING_CONSTANT), AST.AtomExpr.Type.STR, $STRING_CONSTANT.text); }
    | TRUE { $ast = new AST.AtomExpr(loc($TRUE), AST.AtomExpr.Type.BOOL, "true"); }
    | FALSE { $ast = new AST.AtomExpr(loc($FALSE), AST.AtomExpr.Type.BOOL, "false"); }
    | NULL { $ast = new AST.AtomExpr(loc($NULL), AST.AtomExpr.Type.PTR, "0"); }
    | SIZEOF typeExpr { $ast = new AST.SizeExpr(loc($SIZEOF, $typeExpr.stop), $typeExpr.ast); }
    | LBRACE e=expr COLON t=typeExpr RBRACE 
    { $ast = new AST.CastExpr(loc($LBRACE, $RBRACE), $t.ast, $e.ast); }
    | LPAREN e=expr RPAREN { $expr.ast.relocate(loc($LPAREN, $RPAREN));$ast = $e.ast; }
    | ID { $ast = new AST.NameExpr(loc($ID), $ID.text); }
    ;

// Expression list
exprList
	returns [List<AST.Expr> ast]
	: { $ast = new ArrayList<>(); }
	| expr_more { $ast = $expr_more.ast; }
	;

// One or more expressions
expr_more
	returns [List<AST.Expr> ast]
	: expr { $ast = new ArrayList<>(); $ast.add($expr.ast); }
	(COMMA expr { $ast.add($expr.ast); })*
	;

// Generated from Prev25Parser.g4 by ANTLR 4.13.2

    package compiler.phase.synan;
    import java.util.*;
    import compiler.common.report.*;
    import compiler.phase.lexan.*;
    import compiler.phase.abstr.AST;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"doclint:missing", "all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class Prev25Parser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		WHITESPACE=1, INTEGER_CONSTANT=2, CHAR_CONSTANT=3, STRING_CONSTANT=4, 
		AMPERSAND=5, PIPE=6, EQUAL=7, NOT_EQUAL=8, LESS=9, GREATER=10, LESS_EQUAL=11, 
		GREATER_EQUAL=12, MULTIPLY=13, DIVIDE=14, MODULO=15, PLUS=16, MINUS=17, 
		EXCLAMATION=18, DOT=19, CARET=20, ASSIGN=21, COLON=22, COMMA=23, LBRACE=24, 
		RBRACE=25, LPAREN=26, RPAREN=27, LBRACKET=28, RBRACKET=29, BOOL=30, CHAR=31, 
		DO=32, ELSE=33, END=34, FALSE=35, FUN=36, IF=37, INT=38, IN=39, LET=40, 
		NULL=41, RETURN=42, SIZEOF=43, THEN=44, TRUE=45, TYP=46, VAR=47, VOID=48, 
		WHILE=49, ID=50, COMMENT=51;
	public static final int
		RULE_source = 0, RULE_defs = 1, RULE_def = 2, RULE_typealias = 3, RULE_vardeclaration = 4, 
		RULE_fundeclaration = 5, RULE_fundefinition = 6, RULE_funParams = 7, RULE_paramList = 8, 
		RULE_param = 9, RULE_paramList_CD = 10, RULE_param_CD = 11, RULE_statements = 12, 
		RULE_statements_more = 13, RULE_statement = 14, RULE_whileStatement = 15, 
		RULE_ifThenStatement = 16, RULE_ifThenElseStatement = 17, RULE_letInStatement = 18, 
		RULE_typeExpr = 19, RULE_typeList = 20, RULE_expression = 21, RULE_expr = 22, 
		RULE_logic_or = 23, RULE_logic_and = 24, RULE_comparison = 25, RULE_additive = 26, 
		RULE_multiplicative = 27, RULE_unary = 28, RULE_postfix = 29, RULE_primary = 30, 
		RULE_exprList = 31, RULE_expr_more = 32;
	private static String[] makeRuleNames() {
		return new String[] {
			"source", "defs", "def", "typealias", "vardeclaration", "fundeclaration", 
			"fundefinition", "funParams", "paramList", "param", "paramList_CD", "param_CD", 
			"statements", "statements_more", "statement", "whileStatement", "ifThenStatement", 
			"ifThenElseStatement", "letInStatement", "typeExpr", "typeList", "expression", 
			"expr", "logic_or", "logic_and", "comparison", "additive", "multiplicative", 
			"unary", "postfix", "primary", "exprList", "expr_more"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, "'&'", "'|'", "'=='", "'!='", "'<'", "'>'", 
			"'<='", "'>='", "'*'", "'/'", "'%'", "'+'", "'-'", "'!'", "'.'", "'^'", 
			"'='", "':'", "','", "'{'", "'}'", "'('", "')'", "'['", "']'", "'bool'", 
			"'char'", "'do'", "'else'", "'end'", "'false'", "'fun'", "'if'", "'int'", 
			"'in'", "'let'", "'null'", "'return'", "'sizeof'", "'then'", "'true'", 
			"'typ'", "'var'", "'void'", "'while'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "WHITESPACE", "INTEGER_CONSTANT", "CHAR_CONSTANT", "STRING_CONSTANT", 
			"AMPERSAND", "PIPE", "EQUAL", "NOT_EQUAL", "LESS", "GREATER", "LESS_EQUAL", 
			"GREATER_EQUAL", "MULTIPLY", "DIVIDE", "MODULO", "PLUS", "MINUS", "EXCLAMATION", 
			"DOT", "CARET", "ASSIGN", "COLON", "COMMA", "LBRACE", "RBRACE", "LPAREN", 
			"RPAREN", "LBRACKET", "RBRACKET", "BOOL", "CHAR", "DO", "ELSE", "END", 
			"FALSE", "FUN", "IF", "INT", "IN", "LET", "NULL", "RETURN", "SIZEOF", 
			"THEN", "TRUE", "TYP", "VAR", "VOID", "WHILE", "ID", "COMMENT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Prev25Parser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }


	    private Location loc(Token tok) { return new Location((LexAn.LocLogToken)tok); }
	    private Location loc(Token tok1, Token tok2) { return new Location((LexAn.LocLogToken)tok1, (LexAn.LocLogToken)tok2); }
	    private Location loc(Token tok1, Locatable loc2) { return new Location((LexAn.LocLogToken)tok1, loc2); }
	    private Location loc(Locatable loc1, Token tok2) { return new Location(loc1, (LexAn.LocLogToken)tok2); }
	    private Location loc(Locatable loc1, Locatable loc2) { return new Location(loc1, loc2); }

	public Prev25Parser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SourceContext extends ParserRuleContext {
		public AST.Nodes<AST.FullDefn> ast;
		public DefsContext defs;
		public DefsContext defs() {
			return getRuleContext(DefsContext.class,0);
		}
		public TerminalNode EOF() { return getToken(Prev25Parser.EOF, 0); }
		public SourceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_source; }
	}

	public final SourceContext source() throws RecognitionException {
		SourceContext _localctx = new SourceContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_source);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(66);
			((SourceContext)_localctx).defs = defs(0);
			setState(67);
			match(EOF);
			 ((SourceContext)_localctx).ast =  new AST.Nodes<AST.FullDefn>(((SourceContext)_localctx).defs.ast); 
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DefsContext extends ParserRuleContext {
		public ArrayList<AST.FullDefn> ast;
		public DefsContext d;
		public DefContext def;
		public DefContext def() {
			return getRuleContext(DefContext.class,0);
		}
		public DefsContext defs() {
			return getRuleContext(DefsContext.class,0);
		}
		public DefsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_defs; }
	}

	public final DefsContext defs() throws RecognitionException {
		return defs(0);
	}

	private DefsContext defs(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		DefsContext _localctx = new DefsContext(_ctx, _parentState);
		DefsContext _prevctx = _localctx;
		int _startState = 2;
		enterRecursionRule(_localctx, 2, RULE_defs, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(71);
			((DefsContext)_localctx).def = def();
			 ((DefsContext)_localctx).ast =  new ArrayList<AST.FullDefn>(); _localctx.ast.add(((DefsContext)_localctx).def.ast); 
			}
			_ctx.stop = _input.LT(-1);
			setState(80);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new DefsContext(_parentctx, _parentState);
					_localctx.d = _prevctx;
					pushNewRecursionContext(_localctx, _startState, RULE_defs);
					setState(74);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(75);
					((DefsContext)_localctx).def = def();
					 ((DefsContext)_localctx).ast =  ((DefsContext)_localctx).d.ast; _localctx.ast.add(((DefsContext)_localctx).def.ast); 
					}
					} 
				}
				setState(82);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DefContext extends ParserRuleContext {
		public AST.FullDefn ast;
		public TypealiasContext typealias;
		public VardeclarationContext vardeclaration;
		public FundeclarationContext fundeclaration;
		public FundefinitionContext fundefinition;
		public TypealiasContext typealias() {
			return getRuleContext(TypealiasContext.class,0);
		}
		public VardeclarationContext vardeclaration() {
			return getRuleContext(VardeclarationContext.class,0);
		}
		public FundeclarationContext fundeclaration() {
			return getRuleContext(FundeclarationContext.class,0);
		}
		public FundefinitionContext fundefinition() {
			return getRuleContext(FundefinitionContext.class,0);
		}
		public DefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_def; }
	}

	public final DefContext def() throws RecognitionException {
		DefContext _localctx = new DefContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_def);
		try {
			setState(95);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(83);
				((DefContext)_localctx).typealias = typealias();
				 ((DefContext)_localctx).ast =  ((DefContext)_localctx).typealias.ast; 
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(86);
				((DefContext)_localctx).vardeclaration = vardeclaration();
				 ((DefContext)_localctx).ast =  ((DefContext)_localctx).vardeclaration.ast; 
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(89);
				((DefContext)_localctx).fundeclaration = fundeclaration();
				 ((DefContext)_localctx).ast =  ((DefContext)_localctx).fundeclaration.ast; 
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(92);
				((DefContext)_localctx).fundefinition = fundefinition();
				 ((DefContext)_localctx).ast =  ((DefContext)_localctx).fundefinition.ast;  
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypealiasContext extends ParserRuleContext {
		public AST.TypDefn ast;
		public Token TYP;
		public Token ID;
		public TypeExprContext typeExpr;
		public TerminalNode TYP() { return getToken(Prev25Parser.TYP, 0); }
		public TerminalNode ID() { return getToken(Prev25Parser.ID, 0); }
		public TerminalNode ASSIGN() { return getToken(Prev25Parser.ASSIGN, 0); }
		public TypeExprContext typeExpr() {
			return getRuleContext(TypeExprContext.class,0);
		}
		public TypealiasContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typealias; }
	}

	public final TypealiasContext typealias() throws RecognitionException {
		TypealiasContext _localctx = new TypealiasContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_typealias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(97);
			((TypealiasContext)_localctx).TYP = match(TYP);
			setState(98);
			((TypealiasContext)_localctx).ID = match(ID);
			setState(99);
			match(ASSIGN);
			setState(100);
			((TypealiasContext)_localctx).typeExpr = typeExpr();
			 ((TypealiasContext)_localctx).ast =  new AST.TypDefn(loc(((TypealiasContext)_localctx).TYP, ((TypealiasContext)_localctx).typeExpr.ast), ((TypealiasContext)_localctx).ID.getText(), ((TypealiasContext)_localctx).typeExpr.ast); 
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VardeclarationContext extends ParserRuleContext {
		public AST.VarDefn ast;
		public Token VAR;
		public Token ID;
		public TypeExprContext typeExpr;
		public TerminalNode VAR() { return getToken(Prev25Parser.VAR, 0); }
		public TerminalNode ID() { return getToken(Prev25Parser.ID, 0); }
		public TerminalNode COLON() { return getToken(Prev25Parser.COLON, 0); }
		public TypeExprContext typeExpr() {
			return getRuleContext(TypeExprContext.class,0);
		}
		public VardeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_vardeclaration; }
	}

	public final VardeclarationContext vardeclaration() throws RecognitionException {
		VardeclarationContext _localctx = new VardeclarationContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_vardeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(103);
			((VardeclarationContext)_localctx).VAR = match(VAR);
			setState(104);
			((VardeclarationContext)_localctx).ID = match(ID);
			setState(105);
			match(COLON);
			setState(106);
			((VardeclarationContext)_localctx).typeExpr = typeExpr();
			 ((VardeclarationContext)_localctx).ast =  new AST.VarDefn(loc(((VardeclarationContext)_localctx).VAR, ((VardeclarationContext)_localctx).typeExpr.ast), ((VardeclarationContext)_localctx).ID.getText(), ((VardeclarationContext)_localctx).typeExpr.ast); 
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FundeclarationContext extends ParserRuleContext {
		public AST.ExtFunDefn ast;
		public Token FUN;
		public Token ID;
		public FunParamsContext funParams;
		public TypeExprContext typeExpr;
		public TerminalNode FUN() { return getToken(Prev25Parser.FUN, 0); }
		public TerminalNode ID() { return getToken(Prev25Parser.ID, 0); }
		public TerminalNode LPAREN() { return getToken(Prev25Parser.LPAREN, 0); }
		public FunParamsContext funParams() {
			return getRuleContext(FunParamsContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(Prev25Parser.RPAREN, 0); }
		public TerminalNode COLON() { return getToken(Prev25Parser.COLON, 0); }
		public TypeExprContext typeExpr() {
			return getRuleContext(TypeExprContext.class,0);
		}
		public FundeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fundeclaration; }
	}

	public final FundeclarationContext fundeclaration() throws RecognitionException {
		FundeclarationContext _localctx = new FundeclarationContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_fundeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(109);
			((FundeclarationContext)_localctx).FUN = match(FUN);
			setState(110);
			((FundeclarationContext)_localctx).ID = match(ID);
			setState(111);
			match(LPAREN);
			setState(112);
			((FundeclarationContext)_localctx).funParams = funParams();
			setState(113);
			match(RPAREN);
			setState(114);
			match(COLON);
			setState(115);
			((FundeclarationContext)_localctx).typeExpr = typeExpr();
			 ((FundeclarationContext)_localctx).ast =  new AST.ExtFunDefn(loc(((FundeclarationContext)_localctx).FUN, ((FundeclarationContext)_localctx).typeExpr.ast), ((FundeclarationContext)_localctx).ID.getText(), ((FundeclarationContext)_localctx).funParams.ast, ((FundeclarationContext)_localctx).typeExpr.ast); 
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FundefinitionContext extends ParserRuleContext {
		public AST.DefFunDefn ast;
		public Token FUN;
		public Token ID;
		public FunParamsContext funParams;
		public TypeExprContext typeExpr;
		public StatementsContext statements;
		public TerminalNode FUN() { return getToken(Prev25Parser.FUN, 0); }
		public TerminalNode ID() { return getToken(Prev25Parser.ID, 0); }
		public TerminalNode LPAREN() { return getToken(Prev25Parser.LPAREN, 0); }
		public FunParamsContext funParams() {
			return getRuleContext(FunParamsContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(Prev25Parser.RPAREN, 0); }
		public TerminalNode COLON() { return getToken(Prev25Parser.COLON, 0); }
		public TypeExprContext typeExpr() {
			return getRuleContext(TypeExprContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(Prev25Parser.ASSIGN, 0); }
		public StatementsContext statements() {
			return getRuleContext(StatementsContext.class,0);
		}
		public FundefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fundefinition; }
	}

	public final FundefinitionContext fundefinition() throws RecognitionException {
		FundefinitionContext _localctx = new FundefinitionContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_fundefinition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(118);
			((FundefinitionContext)_localctx).FUN = match(FUN);
			setState(119);
			((FundefinitionContext)_localctx).ID = match(ID);
			setState(120);
			match(LPAREN);
			setState(121);
			((FundefinitionContext)_localctx).funParams = funParams();
			setState(122);
			match(RPAREN);
			setState(123);
			match(COLON);
			setState(124);
			((FundefinitionContext)_localctx).typeExpr = typeExpr();
			setState(125);
			match(ASSIGN);
			setState(126);
			((FundefinitionContext)_localctx).statements = statements();
			 ((FundefinitionContext)_localctx).ast =  new AST.DefFunDefn(loc(((FundefinitionContext)_localctx).FUN, (((FundefinitionContext)_localctx).statements!=null?(((FundefinitionContext)_localctx).statements.stop):null)), ((FundefinitionContext)_localctx).ID.getText(), ((FundefinitionContext)_localctx).funParams.ast, ((FundefinitionContext)_localctx).typeExpr.ast, ((FundefinitionContext)_localctx).statements.ast); 
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunParamsContext extends ParserRuleContext {
		public List<AST.ParDefn> ast;
		public ParamListContext paramList;
		public ParamListContext paramList() {
			return getRuleContext(ParamListContext.class,0);
		}
		public FunParamsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funParams; }
	}

	public final FunParamsContext funParams() throws RecognitionException {
		FunParamsContext _localctx = new FunParamsContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_funParams);
		try {
			setState(133);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(129);
				((FunParamsContext)_localctx).paramList = paramList();
				 ((FunParamsContext)_localctx).ast =  ((FunParamsContext)_localctx).paramList.ast; 
				}
				break;
			case RPAREN:
				enterOuterAlt(_localctx, 2);
				{
				 ((FunParamsContext)_localctx).ast =  new ArrayList<>(); 
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParamListContext extends ParserRuleContext {
		public List<AST.ParDefn> ast;
		public ParamContext param;
		public List<ParamContext> param() {
			return getRuleContexts(ParamContext.class);
		}
		public ParamContext param(int i) {
			return getRuleContext(ParamContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(Prev25Parser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(Prev25Parser.COMMA, i);
		}
		public ParamListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_paramList; }
	}

	public final ParamListContext paramList() throws RecognitionException {
		ParamListContext _localctx = new ParamListContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_paramList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(135);
			((ParamListContext)_localctx).param = param();
			 ((ParamListContext)_localctx).ast =  new ArrayList<>(); _localctx.ast.add(((ParamListContext)_localctx).param.ast); 
			setState(143);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(137);
				match(COMMA);
				setState(138);
				((ParamListContext)_localctx).param = param();
				 _localctx.ast.add(((ParamListContext)_localctx).param.ast); 
				}
				}
				setState(145);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParamContext extends ParserRuleContext {
		public AST.ParDefn ast;
		public Token ID;
		public TypeExprContext typeExpr;
		public TerminalNode ID() { return getToken(Prev25Parser.ID, 0); }
		public TerminalNode COLON() { return getToken(Prev25Parser.COLON, 0); }
		public TypeExprContext typeExpr() {
			return getRuleContext(TypeExprContext.class,0);
		}
		public ParamContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_param; }
	}

	public final ParamContext param() throws RecognitionException {
		ParamContext _localctx = new ParamContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_param);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(146);
			((ParamContext)_localctx).ID = match(ID);
			setState(147);
			match(COLON);
			setState(148);
			((ParamContext)_localctx).typeExpr = typeExpr();
			 ((ParamContext)_localctx).ast =  new AST.ParDefn(loc(((ParamContext)_localctx).ID, (((ParamContext)_localctx).typeExpr!=null?(((ParamContext)_localctx).typeExpr.stop):null)), ((ParamContext)_localctx).ID.getText(), ((ParamContext)_localctx).typeExpr.ast); 
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParamList_CDContext extends ParserRuleContext {
		public List<AST.CompDefn> ast;
		public Param_CDContext param_CD;
		public List<Param_CDContext> param_CD() {
			return getRuleContexts(Param_CDContext.class);
		}
		public Param_CDContext param_CD(int i) {
			return getRuleContext(Param_CDContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(Prev25Parser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(Prev25Parser.COMMA, i);
		}
		public ParamList_CDContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_paramList_CD; }
	}

	public final ParamList_CDContext paramList_CD() throws RecognitionException {
		ParamList_CDContext _localctx = new ParamList_CDContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_paramList_CD);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(151);
			((ParamList_CDContext)_localctx).param_CD = param_CD();
			 ((ParamList_CDContext)_localctx).ast =  new ArrayList<>(); _localctx.ast.add(((ParamList_CDContext)_localctx).param_CD.ast); 
			setState(159);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(153);
				match(COMMA);
				setState(154);
				((ParamList_CDContext)_localctx).param_CD = param_CD();
				 _localctx.ast.add(((ParamList_CDContext)_localctx).param_CD.ast); 
				}
				}
				setState(161);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Param_CDContext extends ParserRuleContext {
		public AST.CompDefn ast;
		public Token ID;
		public TypeExprContext typeExpr;
		public TerminalNode ID() { return getToken(Prev25Parser.ID, 0); }
		public TerminalNode COLON() { return getToken(Prev25Parser.COLON, 0); }
		public TypeExprContext typeExpr() {
			return getRuleContext(TypeExprContext.class,0);
		}
		public Param_CDContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_param_CD; }
	}

	public final Param_CDContext param_CD() throws RecognitionException {
		Param_CDContext _localctx = new Param_CDContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_param_CD);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(162);
			((Param_CDContext)_localctx).ID = match(ID);
			setState(163);
			match(COLON);
			setState(164);
			((Param_CDContext)_localctx).typeExpr = typeExpr();
			 ((Param_CDContext)_localctx).ast =  new AST.CompDefn(loc(((Param_CDContext)_localctx).ID, (((Param_CDContext)_localctx).typeExpr!=null?(((Param_CDContext)_localctx).typeExpr.stop):null)), ((Param_CDContext)_localctx).ID.getText(), ((Param_CDContext)_localctx).typeExpr.ast); 
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementsContext extends ParserRuleContext {
		public List<AST.Stmt> ast;
		public Statements_moreContext statements_more;
		public Statements_moreContext statements_more() {
			return getRuleContext(Statements_moreContext.class,0);
		}
		public StatementsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statements; }
	}

	public final StatementsContext statements() throws RecognitionException {
		StatementsContext _localctx = new StatementsContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_statements);
		try {
			setState(171);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				 ((StatementsContext)_localctx).ast =  new ArrayList<>(); 
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(168);
				((StatementsContext)_localctx).statements_more = statements_more();
				 ((StatementsContext)_localctx).ast =  ((StatementsContext)_localctx).statements_more.ast; 
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Statements_moreContext extends ParserRuleContext {
		public List<AST.Stmt> ast;
		public StatementContext statement;
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(Prev25Parser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(Prev25Parser.COMMA, i);
		}
		public Statements_moreContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statements_more; }
	}

	public final Statements_moreContext statements_more() throws RecognitionException {
		Statements_moreContext _localctx = new Statements_moreContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_statements_more);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(173);
			((Statements_moreContext)_localctx).statement = statement();
			 ((Statements_moreContext)_localctx).ast =  new ArrayList<>(); _localctx.ast.add(((Statements_moreContext)_localctx).statement.ast); 
			setState(181);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(175);
					match(COMMA);
					setState(176);
					((Statements_moreContext)_localctx).statement = statement();
					 _localctx.ast.add(((Statements_moreContext)_localctx).statement.ast); 
					}
					} 
				}
				setState(183);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementContext extends ParserRuleContext {
		public AST.Stmt ast;
		public ExpressionContext expression;
		public ExpressionContext lhs;
		public ExpressionContext rhs;
		public Token RETURN;
		public WhileStatementContext whileStatement;
		public IfThenStatementContext ifThenStatement;
		public IfThenElseStatementContext ifThenElseStatement;
		public LetInStatementContext letInStatement;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode ASSIGN() { return getToken(Prev25Parser.ASSIGN, 0); }
		public TerminalNode RETURN() { return getToken(Prev25Parser.RETURN, 0); }
		public WhileStatementContext whileStatement() {
			return getRuleContext(WhileStatementContext.class,0);
		}
		public IfThenStatementContext ifThenStatement() {
			return getRuleContext(IfThenStatementContext.class,0);
		}
		public IfThenElseStatementContext ifThenElseStatement() {
			return getRuleContext(IfThenElseStatementContext.class,0);
		}
		public LetInStatementContext letInStatement() {
			return getRuleContext(LetInStatementContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_statement);
		try {
			setState(208);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(184);
				((StatementContext)_localctx).expression = expression();
				 ((StatementContext)_localctx).ast =  new AST.ExprStmt(loc((((StatementContext)_localctx).expression!=null?(((StatementContext)_localctx).expression.start):null), (((StatementContext)_localctx).expression!=null?(((StatementContext)_localctx).expression.stop):null)), ((StatementContext)_localctx).expression.ast); 
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(187);
				((StatementContext)_localctx).lhs = expression();
				setState(188);
				match(ASSIGN);
				setState(189);
				((StatementContext)_localctx).rhs = expression();
				 ((StatementContext)_localctx).ast =  new AST.AssignStmt(loc((((StatementContext)_localctx).lhs!=null?(((StatementContext)_localctx).lhs.start):null), (((StatementContext)_localctx).rhs!=null?(((StatementContext)_localctx).rhs.stop):null)), ((StatementContext)_localctx).lhs.ast, ((StatementContext)_localctx).rhs.ast); 
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(192);
				((StatementContext)_localctx).RETURN = match(RETURN);
				setState(193);
				((StatementContext)_localctx).expression = expression();
				 ((StatementContext)_localctx).ast =  new AST.ReturnStmt(loc(((StatementContext)_localctx).RETURN, (((StatementContext)_localctx).expression!=null?(((StatementContext)_localctx).expression.stop):null)), ((StatementContext)_localctx).expression.ast); 
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(196);
				((StatementContext)_localctx).whileStatement = whileStatement();
				 ((StatementContext)_localctx).ast =  ((StatementContext)_localctx).whileStatement.ast; 
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(199);
				((StatementContext)_localctx).ifThenStatement = ifThenStatement();
				 ((StatementContext)_localctx).ast =  ((StatementContext)_localctx).ifThenStatement.ast; 
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(202);
				((StatementContext)_localctx).ifThenElseStatement = ifThenElseStatement();
				 ((StatementContext)_localctx).ast =  ((StatementContext)_localctx).ifThenElseStatement.ast; 
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(205);
				((StatementContext)_localctx).letInStatement = letInStatement();
				 ((StatementContext)_localctx).ast =  ((StatementContext)_localctx).letInStatement.ast; 
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WhileStatementContext extends ParserRuleContext {
		public AST.WhileStmt ast;
		public Token WHILE;
		public ExpressionContext expression;
		public StatementsContext statements;
		public Token END;
		public TerminalNode WHILE() { return getToken(Prev25Parser.WHILE, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode DO() { return getToken(Prev25Parser.DO, 0); }
		public StatementsContext statements() {
			return getRuleContext(StatementsContext.class,0);
		}
		public TerminalNode END() { return getToken(Prev25Parser.END, 0); }
		public WhileStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whileStatement; }
	}

	public final WhileStatementContext whileStatement() throws RecognitionException {
		WhileStatementContext _localctx = new WhileStatementContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_whileStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(210);
			((WhileStatementContext)_localctx).WHILE = match(WHILE);
			setState(211);
			((WhileStatementContext)_localctx).expression = expression();
			setState(212);
			match(DO);
			setState(213);
			((WhileStatementContext)_localctx).statements = statements();
			setState(214);
			((WhileStatementContext)_localctx).END = match(END);
			 ((WhileStatementContext)_localctx).ast =  new AST.WhileStmt(loc(((WhileStatementContext)_localctx).WHILE, ((WhileStatementContext)_localctx).END), ((WhileStatementContext)_localctx).expression.ast, ((WhileStatementContext)_localctx).statements.ast); 
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IfThenStatementContext extends ParserRuleContext {
		public AST.IfThenStmt ast;
		public Token IF;
		public ExpressionContext expression;
		public StatementsContext statements;
		public Token END;
		public TerminalNode IF() { return getToken(Prev25Parser.IF, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode THEN() { return getToken(Prev25Parser.THEN, 0); }
		public StatementsContext statements() {
			return getRuleContext(StatementsContext.class,0);
		}
		public TerminalNode END() { return getToken(Prev25Parser.END, 0); }
		public IfThenStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ifThenStatement; }
	}

	public final IfThenStatementContext ifThenStatement() throws RecognitionException {
		IfThenStatementContext _localctx = new IfThenStatementContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_ifThenStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(217);
			((IfThenStatementContext)_localctx).IF = match(IF);
			setState(218);
			((IfThenStatementContext)_localctx).expression = expression();
			setState(219);
			match(THEN);
			setState(220);
			((IfThenStatementContext)_localctx).statements = statements();
			setState(221);
			((IfThenStatementContext)_localctx).END = match(END);
			 ((IfThenStatementContext)_localctx).ast =  new AST.IfThenStmt(loc(((IfThenStatementContext)_localctx).IF, ((IfThenStatementContext)_localctx).END), ((IfThenStatementContext)_localctx).expression.ast, ((IfThenStatementContext)_localctx).statements.ast); 
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IfThenElseStatementContext extends ParserRuleContext {
		public AST.IfThenElseStmt ast;
		public Token IF;
		public ExpressionContext expression;
		public StatementsContext ls;
		public StatementsContext rs;
		public Token END;
		public TerminalNode IF() { return getToken(Prev25Parser.IF, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode THEN() { return getToken(Prev25Parser.THEN, 0); }
		public TerminalNode ELSE() { return getToken(Prev25Parser.ELSE, 0); }
		public TerminalNode END() { return getToken(Prev25Parser.END, 0); }
		public List<StatementsContext> statements() {
			return getRuleContexts(StatementsContext.class);
		}
		public StatementsContext statements(int i) {
			return getRuleContext(StatementsContext.class,i);
		}
		public IfThenElseStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ifThenElseStatement; }
	}

	public final IfThenElseStatementContext ifThenElseStatement() throws RecognitionException {
		IfThenElseStatementContext _localctx = new IfThenElseStatementContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_ifThenElseStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(224);
			((IfThenElseStatementContext)_localctx).IF = match(IF);
			setState(225);
			((IfThenElseStatementContext)_localctx).expression = expression();
			setState(226);
			match(THEN);
			setState(227);
			((IfThenElseStatementContext)_localctx).ls = statements();
			setState(228);
			match(ELSE);
			setState(229);
			((IfThenElseStatementContext)_localctx).rs = statements();
			setState(230);
			((IfThenElseStatementContext)_localctx).END = match(END);
			 ((IfThenElseStatementContext)_localctx).ast =  new AST.IfThenElseStmt(loc(((IfThenElseStatementContext)_localctx).IF, ((IfThenElseStatementContext)_localctx).END), ((IfThenElseStatementContext)_localctx).expression.ast, ((IfThenElseStatementContext)_localctx).ls.ast, ((IfThenElseStatementContext)_localctx).rs.ast); 
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LetInStatementContext extends ParserRuleContext {
		public AST.LetStmt ast;
		public Token LET;
		public DefsContext defs;
		public Statements_moreContext statements_more;
		public Token END;
		public TerminalNode LET() { return getToken(Prev25Parser.LET, 0); }
		public TerminalNode IN() { return getToken(Prev25Parser.IN, 0); }
		public Statements_moreContext statements_more() {
			return getRuleContext(Statements_moreContext.class,0);
		}
		public TerminalNode END() { return getToken(Prev25Parser.END, 0); }
		public List<DefsContext> defs() {
			return getRuleContexts(DefsContext.class);
		}
		public DefsContext defs(int i) {
			return getRuleContext(DefsContext.class,i);
		}
		public LetInStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_letInStatement; }
	}

	public final LetInStatementContext letInStatement() throws RecognitionException {
		LetInStatementContext _localctx = new LetInStatementContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_letInStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(233);
			((LetInStatementContext)_localctx).LET = match(LET);
			setState(235); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(234);
				((LetInStatementContext)_localctx).defs = defs(0);
				}
				}
				setState(237); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 211174952009728L) != 0) );
			setState(239);
			match(IN);
			setState(240);
			((LetInStatementContext)_localctx).statements_more = statements_more();
			setState(241);
			((LetInStatementContext)_localctx).END = match(END);
			 ((LetInStatementContext)_localctx).ast =  new AST.LetStmt(loc(((LetInStatementContext)_localctx).LET, ((LetInStatementContext)_localctx).END), ((LetInStatementContext)_localctx).defs.ast, ((LetInStatementContext)_localctx).statements_more.ast); 
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeExprContext extends ParserRuleContext {
		public AST.Type ast;
		public Token INT;
		public Token CHAR;
		public Token BOOL;
		public Token VOID;
		public Token ID;
		public Token LBRACKET;
		public Token INTEGER_CONSTANT;
		public TypeExprContext typeExpr;
		public Token CARET;
		public Token LESS;
		public ParamList_CDContext plCD;
		public ParamList_CDContext paramList_CD;
		public Token GREATER;
		public Token LBRACE;
		public Token RBRACE;
		public Token LPAREN;
		public TypeListContext typeList;
		public TerminalNode INT() { return getToken(Prev25Parser.INT, 0); }
		public TerminalNode CHAR() { return getToken(Prev25Parser.CHAR, 0); }
		public TerminalNode BOOL() { return getToken(Prev25Parser.BOOL, 0); }
		public TerminalNode VOID() { return getToken(Prev25Parser.VOID, 0); }
		public TerminalNode ID() { return getToken(Prev25Parser.ID, 0); }
		public TerminalNode LBRACKET() { return getToken(Prev25Parser.LBRACKET, 0); }
		public TerminalNode INTEGER_CONSTANT() { return getToken(Prev25Parser.INTEGER_CONSTANT, 0); }
		public TerminalNode RBRACKET() { return getToken(Prev25Parser.RBRACKET, 0); }
		public TypeExprContext typeExpr() {
			return getRuleContext(TypeExprContext.class,0);
		}
		public TerminalNode CARET() { return getToken(Prev25Parser.CARET, 0); }
		public TerminalNode LESS() { return getToken(Prev25Parser.LESS, 0); }
		public TerminalNode GREATER() { return getToken(Prev25Parser.GREATER, 0); }
		public ParamList_CDContext paramList_CD() {
			return getRuleContext(ParamList_CDContext.class,0);
		}
		public TerminalNode LBRACE() { return getToken(Prev25Parser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(Prev25Parser.RBRACE, 0); }
		public TerminalNode LPAREN() { return getToken(Prev25Parser.LPAREN, 0); }
		public TypeListContext typeList() {
			return getRuleContext(TypeListContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(Prev25Parser.RPAREN, 0); }
		public TerminalNode COLON() { return getToken(Prev25Parser.COLON, 0); }
		public TypeExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeExpr; }
	}

	public final TypeExprContext typeExpr() throws RecognitionException {
		TypeExprContext _localctx = new TypeExprContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_typeExpr);
		try {
			setState(281);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INT:
				enterOuterAlt(_localctx, 1);
				{
				setState(244);
				((TypeExprContext)_localctx).INT = match(INT);
				 ((TypeExprContext)_localctx).ast =  new AST.AtomType(loc(((TypeExprContext)_localctx).INT), AST.AtomType.Type.INT); 
				}
				break;
			case CHAR:
				enterOuterAlt(_localctx, 2);
				{
				setState(246);
				((TypeExprContext)_localctx).CHAR = match(CHAR);
				 ((TypeExprContext)_localctx).ast =  new AST.AtomType(loc(((TypeExprContext)_localctx).CHAR), AST.AtomType.Type.CHAR); 
				}
				break;
			case BOOL:
				enterOuterAlt(_localctx, 3);
				{
				setState(248);
				((TypeExprContext)_localctx).BOOL = match(BOOL);
				 ((TypeExprContext)_localctx).ast =  new AST.AtomType(loc(((TypeExprContext)_localctx).BOOL), AST.AtomType.Type.BOOL); 
				}
				break;
			case VOID:
				enterOuterAlt(_localctx, 4);
				{
				setState(250);
				((TypeExprContext)_localctx).VOID = match(VOID);
				 ((TypeExprContext)_localctx).ast =  new AST.AtomType(loc(((TypeExprContext)_localctx).VOID), AST.AtomType.Type.VOID); 
				}
				break;
			case ID:
				enterOuterAlt(_localctx, 5);
				{
				setState(252);
				((TypeExprContext)_localctx).ID = match(ID);
				 ((TypeExprContext)_localctx).ast =  new AST.NameType(loc(((TypeExprContext)_localctx).ID), (((TypeExprContext)_localctx).ID!=null?((TypeExprContext)_localctx).ID.getText():null)); 
				}
				break;
			case LBRACKET:
				enterOuterAlt(_localctx, 6);
				{
				setState(254);
				((TypeExprContext)_localctx).LBRACKET = match(LBRACKET);
				setState(255);
				((TypeExprContext)_localctx).INTEGER_CONSTANT = match(INTEGER_CONSTANT);
				setState(256);
				match(RBRACKET);
				setState(257);
				((TypeExprContext)_localctx).typeExpr = typeExpr();
				 ((TypeExprContext)_localctx).ast =  new AST.ArrType(loc(((TypeExprContext)_localctx).LBRACKET, (((TypeExprContext)_localctx).typeExpr!=null?(((TypeExprContext)_localctx).typeExpr.stop):null)), ((TypeExprContext)_localctx).typeExpr.ast, (((TypeExprContext)_localctx).INTEGER_CONSTANT!=null?((TypeExprContext)_localctx).INTEGER_CONSTANT.getText():null)); 
				}
				break;
			case CARET:
				enterOuterAlt(_localctx, 7);
				{
				setState(260);
				((TypeExprContext)_localctx).CARET = match(CARET);
				setState(261);
				((TypeExprContext)_localctx).typeExpr = typeExpr();
				 ((TypeExprContext)_localctx).ast =  new AST.PtrType(loc(((TypeExprContext)_localctx).CARET, (((TypeExprContext)_localctx).typeExpr!=null?(((TypeExprContext)_localctx).typeExpr.stop):null)), ((TypeExprContext)_localctx).typeExpr.ast); 
				}
				break;
			case LESS:
				enterOuterAlt(_localctx, 8);
				{
				setState(264);
				((TypeExprContext)_localctx).LESS = match(LESS);
				setState(265);
				((TypeExprContext)_localctx).plCD = ((TypeExprContext)_localctx).paramList_CD = paramList_CD();
				setState(266);
				((TypeExprContext)_localctx).GREATER = match(GREATER);

				            ((TypeExprContext)_localctx).ast =  new AST.StrType(loc(((TypeExprContext)_localctx).LESS, ((TypeExprContext)_localctx).GREATER), ((TypeExprContext)_localctx).paramList_CD.ast);
				        
				}
				break;
			case LBRACE:
				enterOuterAlt(_localctx, 9);
				{
				setState(269);
				((TypeExprContext)_localctx).LBRACE = match(LBRACE);
				setState(270);
				((TypeExprContext)_localctx).plCD = ((TypeExprContext)_localctx).paramList_CD = paramList_CD();
				setState(271);
				((TypeExprContext)_localctx).RBRACE = match(RBRACE);

							((TypeExprContext)_localctx).ast =  new AST.UniType(loc(((TypeExprContext)_localctx).LBRACE, ((TypeExprContext)_localctx).RBRACE), ((TypeExprContext)_localctx).paramList_CD.ast) ;
						
				}
				break;
			case LPAREN:
				enterOuterAlt(_localctx, 10);
				{
				setState(274);
				((TypeExprContext)_localctx).LPAREN = match(LPAREN);
				setState(275);
				((TypeExprContext)_localctx).typeList = typeList();
				setState(276);
				match(RPAREN);
				setState(277);
				match(COLON);
				setState(278);
				((TypeExprContext)_localctx).typeExpr = typeExpr();
				 ((TypeExprContext)_localctx).ast =  new AST.FunType(loc(((TypeExprContext)_localctx).LPAREN, ((TypeExprContext)_localctx).typeExpr.ast), ((TypeExprContext)_localctx).typeList.ast, ((TypeExprContext)_localctx).typeExpr.ast); 
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeListContext extends ParserRuleContext {
		public List<AST.Type> ast;
		public TypeExprContext typeExpr;
		public List<TypeExprContext> typeExpr() {
			return getRuleContexts(TypeExprContext.class);
		}
		public TypeExprContext typeExpr(int i) {
			return getRuleContext(TypeExprContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(Prev25Parser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(Prev25Parser.COMMA, i);
		}
		public TypeListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeList; }
	}

	public final TypeListContext typeList() throws RecognitionException {
		TypeListContext _localctx = new TypeListContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_typeList);
		int _la;
		try {
			setState(295);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case RPAREN:
				enterOuterAlt(_localctx, 1);
				{
				 ((TypeListContext)_localctx).ast =  new ArrayList<>(); 
				}
				break;
			case LESS:
			case CARET:
			case LBRACE:
			case LPAREN:
			case LBRACKET:
			case BOOL:
			case CHAR:
			case INT:
			case VOID:
			case ID:
				enterOuterAlt(_localctx, 2);
				{
				setState(284);
				((TypeListContext)_localctx).typeExpr = typeExpr();
				 ((TypeListContext)_localctx).ast =  new ArrayList<>(); _localctx.ast.add(((TypeListContext)_localctx).typeExpr.ast); 
				setState(292);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(286);
					match(COMMA);
					setState(287);
					((TypeListContext)_localctx).typeExpr = typeExpr();
					 _localctx.ast.add(((TypeListContext)_localctx).typeExpr.ast); 
					}
					}
					setState(294);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionContext extends ParserRuleContext {
		public AST.Expr ast;
		public ExprContext expr;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_expression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(297);
			((ExpressionContext)_localctx).expr = expr();
			 ((ExpressionContext)_localctx).ast =  ((ExpressionContext)_localctx).expr.ast; 
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprContext extends ParserRuleContext {
		public AST.Expr ast;
		public Logic_orContext logic_or;
		public Logic_orContext logic_or() {
			return getRuleContext(Logic_orContext.class,0);
		}
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	}

	public final ExprContext expr() throws RecognitionException {
		ExprContext _localctx = new ExprContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_expr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(300);
			((ExprContext)_localctx).logic_or = logic_or(0);
			 ((ExprContext)_localctx).ast =  ((ExprContext)_localctx).logic_or.ast; 
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Logic_orContext extends ParserRuleContext {
		public AST.Expr ast;
		public Logic_orContext left;
		public Logic_andContext logic_and;
		public Logic_andContext right;
		public Logic_andContext logic_and() {
			return getRuleContext(Logic_andContext.class,0);
		}
		public TerminalNode PIPE() { return getToken(Prev25Parser.PIPE, 0); }
		public Logic_orContext logic_or() {
			return getRuleContext(Logic_orContext.class,0);
		}
		public Logic_orContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logic_or; }
	}

	public final Logic_orContext logic_or() throws RecognitionException {
		return logic_or(0);
	}

	private Logic_orContext logic_or(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Logic_orContext _localctx = new Logic_orContext(_ctx, _parentState);
		Logic_orContext _prevctx = _localctx;
		int _startState = 46;
		enterRecursionRule(_localctx, 46, RULE_logic_or, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(304);
			((Logic_orContext)_localctx).logic_and = logic_and(0);
			 ((Logic_orContext)_localctx).ast =  ((Logic_orContext)_localctx).logic_and.ast; 
			}
			_ctx.stop = _input.LT(-1);
			setState(314);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new Logic_orContext(_parentctx, _parentState);
					_localctx.left = _prevctx;
					pushNewRecursionContext(_localctx, _startState, RULE_logic_or);
					setState(307);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(308);
					match(PIPE);
					setState(309);
					((Logic_orContext)_localctx).right = ((Logic_orContext)_localctx).logic_and = logic_and(0);
					 ((Logic_orContext)_localctx).ast =  new AST.BinExpr(loc((((Logic_orContext)_localctx).left!=null?(((Logic_orContext)_localctx).left.start):null), (((Logic_orContext)_localctx).right!=null?(((Logic_orContext)_localctx).right.stop):null)), AST.BinExpr.Oper.OR, ((Logic_orContext)_localctx).left.ast, ((Logic_orContext)_localctx).right.ast); 
					}
					} 
				}
				setState(316);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Logic_andContext extends ParserRuleContext {
		public AST.Expr ast;
		public Logic_andContext left;
		public ComparisonContext comparison;
		public ComparisonContext right;
		public ComparisonContext comparison() {
			return getRuleContext(ComparisonContext.class,0);
		}
		public TerminalNode AMPERSAND() { return getToken(Prev25Parser.AMPERSAND, 0); }
		public Logic_andContext logic_and() {
			return getRuleContext(Logic_andContext.class,0);
		}
		public Logic_andContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logic_and; }
	}

	public final Logic_andContext logic_and() throws RecognitionException {
		return logic_and(0);
	}

	private Logic_andContext logic_and(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Logic_andContext _localctx = new Logic_andContext(_ctx, _parentState);
		Logic_andContext _prevctx = _localctx;
		int _startState = 48;
		enterRecursionRule(_localctx, 48, RULE_logic_and, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(318);
			((Logic_andContext)_localctx).comparison = comparison();
			 ((Logic_andContext)_localctx).ast =  ((Logic_andContext)_localctx).comparison.ast; 
			}
			_ctx.stop = _input.LT(-1);
			setState(328);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new Logic_andContext(_parentctx, _parentState);
					_localctx.left = _prevctx;
					pushNewRecursionContext(_localctx, _startState, RULE_logic_and);
					setState(321);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(322);
					match(AMPERSAND);
					setState(323);
					((Logic_andContext)_localctx).right = ((Logic_andContext)_localctx).comparison = comparison();
					 ((Logic_andContext)_localctx).ast =  new AST.BinExpr(loc((((Logic_andContext)_localctx).left!=null?(((Logic_andContext)_localctx).left.start):null), (((Logic_andContext)_localctx).right!=null?(((Logic_andContext)_localctx).right.stop):null)), AST.BinExpr.Oper.AND, ((Logic_andContext)_localctx).left.ast, ((Logic_andContext)_localctx).right.ast); 
					}
					} 
				}
				setState(330);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ComparisonContext extends ParserRuleContext {
		public AST.Expr ast;
		public AdditiveContext left;
		public AdditiveContext right;
		public AdditiveContext additive;
		public TerminalNode EQUAL() { return getToken(Prev25Parser.EQUAL, 0); }
		public List<AdditiveContext> additive() {
			return getRuleContexts(AdditiveContext.class);
		}
		public AdditiveContext additive(int i) {
			return getRuleContext(AdditiveContext.class,i);
		}
		public TerminalNode NOT_EQUAL() { return getToken(Prev25Parser.NOT_EQUAL, 0); }
		public TerminalNode LESS() { return getToken(Prev25Parser.LESS, 0); }
		public TerminalNode GREATER() { return getToken(Prev25Parser.GREATER, 0); }
		public TerminalNode LESS_EQUAL() { return getToken(Prev25Parser.LESS_EQUAL, 0); }
		public TerminalNode GREATER_EQUAL() { return getToken(Prev25Parser.GREATER_EQUAL, 0); }
		public ComparisonContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comparison; }
	}

	public final ComparisonContext comparison() throws RecognitionException {
		ComparisonContext _localctx = new ComparisonContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_comparison);
		try {
			setState(364);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(331);
				((ComparisonContext)_localctx).left = additive(0);
				setState(332);
				match(EQUAL);
				setState(333);
				((ComparisonContext)_localctx).right = additive(0);
				 ((ComparisonContext)_localctx).ast =  new AST.BinExpr(loc((((ComparisonContext)_localctx).left!=null?(((ComparisonContext)_localctx).left.start):null), (((ComparisonContext)_localctx).right!=null?(((ComparisonContext)_localctx).right.stop):null)), AST.BinExpr.Oper.EQU, ((ComparisonContext)_localctx).left.ast, ((ComparisonContext)_localctx).right.ast); 
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(336);
				((ComparisonContext)_localctx).left = additive(0);
				setState(337);
				match(NOT_EQUAL);
				setState(338);
				((ComparisonContext)_localctx).right = additive(0);
				 ((ComparisonContext)_localctx).ast =  new AST.BinExpr(loc((((ComparisonContext)_localctx).left!=null?(((ComparisonContext)_localctx).left.start):null), (((ComparisonContext)_localctx).right!=null?(((ComparisonContext)_localctx).right.stop):null)), AST.BinExpr.Oper.NEQ, ((ComparisonContext)_localctx).left.ast, ((ComparisonContext)_localctx).right.ast); 
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(341);
				((ComparisonContext)_localctx).left = additive(0);
				setState(342);
				match(LESS);
				setState(343);
				((ComparisonContext)_localctx).right = additive(0);
				 ((ComparisonContext)_localctx).ast =  new AST.BinExpr(loc((((ComparisonContext)_localctx).left!=null?(((ComparisonContext)_localctx).left.start):null), (((ComparisonContext)_localctx).right!=null?(((ComparisonContext)_localctx).right.stop):null)), AST.BinExpr.Oper.LTH, ((ComparisonContext)_localctx).left.ast, ((ComparisonContext)_localctx).right.ast); 
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(346);
				((ComparisonContext)_localctx).left = additive(0);
				setState(347);
				match(GREATER);
				setState(348);
				((ComparisonContext)_localctx).right = additive(0);
				 ((ComparisonContext)_localctx).ast =  new AST.BinExpr(loc((((ComparisonContext)_localctx).left!=null?(((ComparisonContext)_localctx).left.start):null), (((ComparisonContext)_localctx).right!=null?(((ComparisonContext)_localctx).right.stop):null)), AST.BinExpr.Oper.GTH, ((ComparisonContext)_localctx).left.ast, ((ComparisonContext)_localctx).right.ast); 
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(351);
				((ComparisonContext)_localctx).left = additive(0);
				setState(352);
				match(LESS_EQUAL);
				setState(353);
				((ComparisonContext)_localctx).right = additive(0);
				 ((ComparisonContext)_localctx).ast =  new AST.BinExpr(loc((((ComparisonContext)_localctx).left!=null?(((ComparisonContext)_localctx).left.start):null), (((ComparisonContext)_localctx).right!=null?(((ComparisonContext)_localctx).right.stop):null)), AST.BinExpr.Oper.LEQ, ((ComparisonContext)_localctx).left.ast, ((ComparisonContext)_localctx).right.ast); 
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(356);
				((ComparisonContext)_localctx).left = additive(0);
				setState(357);
				match(GREATER_EQUAL);
				setState(358);
				((ComparisonContext)_localctx).right = additive(0);
				 ((ComparisonContext)_localctx).ast =  new AST.BinExpr(loc((((ComparisonContext)_localctx).left!=null?(((ComparisonContext)_localctx).left.start):null), (((ComparisonContext)_localctx).right!=null?(((ComparisonContext)_localctx).right.stop):null)), AST.BinExpr.Oper.GEQ, ((ComparisonContext)_localctx).left.ast, ((ComparisonContext)_localctx).right.ast); 
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(361);
				((ComparisonContext)_localctx).additive = additive(0);
				 ((ComparisonContext)_localctx).ast =  ((ComparisonContext)_localctx).additive.ast; 
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AdditiveContext extends ParserRuleContext {
		public AST.Expr ast;
		public AdditiveContext left;
		public MultiplicativeContext multiplicative;
		public Token op;
		public MultiplicativeContext right;
		public MultiplicativeContext multiplicative() {
			return getRuleContext(MultiplicativeContext.class,0);
		}
		public AdditiveContext additive() {
			return getRuleContext(AdditiveContext.class,0);
		}
		public TerminalNode PLUS() { return getToken(Prev25Parser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(Prev25Parser.MINUS, 0); }
		public AdditiveContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_additive; }
	}

	public final AdditiveContext additive() throws RecognitionException {
		return additive(0);
	}

	private AdditiveContext additive(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		AdditiveContext _localctx = new AdditiveContext(_ctx, _parentState);
		AdditiveContext _prevctx = _localctx;
		int _startState = 52;
		enterRecursionRule(_localctx, 52, RULE_additive, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(367);
			((AdditiveContext)_localctx).multiplicative = multiplicative(0);
			 ((AdditiveContext)_localctx).ast =  ((AdditiveContext)_localctx).multiplicative.ast; 
			}
			_ctx.stop = _input.LT(-1);
			setState(382);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(380);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
					case 1:
						{
						_localctx = new AdditiveContext(_parentctx, _parentState);
						_localctx.left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_additive);
						setState(370);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(371);
						((AdditiveContext)_localctx).op = match(PLUS);
						setState(372);
						((AdditiveContext)_localctx).right = ((AdditiveContext)_localctx).multiplicative = multiplicative(0);
						 ((AdditiveContext)_localctx).ast =  new AST.BinExpr(loc((((AdditiveContext)_localctx).left!=null?(((AdditiveContext)_localctx).left.start):null), (((AdditiveContext)_localctx).right!=null?(((AdditiveContext)_localctx).right.stop):null)), AST.BinExpr.Oper.ADD, ((AdditiveContext)_localctx).left.ast, ((AdditiveContext)_localctx).right.ast); 
						}
						break;
					case 2:
						{
						_localctx = new AdditiveContext(_parentctx, _parentState);
						_localctx.left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_additive);
						setState(375);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(376);
						((AdditiveContext)_localctx).op = match(MINUS);
						setState(377);
						((AdditiveContext)_localctx).right = ((AdditiveContext)_localctx).multiplicative = multiplicative(0);
						 ((AdditiveContext)_localctx).ast =  new AST.BinExpr(loc((((AdditiveContext)_localctx).left!=null?(((AdditiveContext)_localctx).left.start):null), (((AdditiveContext)_localctx).right!=null?(((AdditiveContext)_localctx).right.stop):null)), AST.BinExpr.Oper.SUB, ((AdditiveContext)_localctx).left.ast, ((AdditiveContext)_localctx).right.ast); 
						}
						break;
					}
					} 
				}
				setState(384);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MultiplicativeContext extends ParserRuleContext {
		public AST.Expr ast;
		public MultiplicativeContext left;
		public UnaryContext unary;
		public UnaryContext right;
		public UnaryContext unary() {
			return getRuleContext(UnaryContext.class,0);
		}
		public TerminalNode MULTIPLY() { return getToken(Prev25Parser.MULTIPLY, 0); }
		public MultiplicativeContext multiplicative() {
			return getRuleContext(MultiplicativeContext.class,0);
		}
		public TerminalNode DIVIDE() { return getToken(Prev25Parser.DIVIDE, 0); }
		public TerminalNode MODULO() { return getToken(Prev25Parser.MODULO, 0); }
		public MultiplicativeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multiplicative; }
	}

	public final MultiplicativeContext multiplicative() throws RecognitionException {
		return multiplicative(0);
	}

	private MultiplicativeContext multiplicative(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		MultiplicativeContext _localctx = new MultiplicativeContext(_ctx, _parentState);
		MultiplicativeContext _prevctx = _localctx;
		int _startState = 54;
		enterRecursionRule(_localctx, 54, RULE_multiplicative, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(386);
			((MultiplicativeContext)_localctx).unary = unary();
			 ((MultiplicativeContext)_localctx).ast =  ((MultiplicativeContext)_localctx).unary.ast; 
			}
			_ctx.stop = _input.LT(-1);
			setState(406);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(404);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
					case 1:
						{
						_localctx = new MultiplicativeContext(_parentctx, _parentState);
						_localctx.left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_multiplicative);
						setState(389);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(390);
						match(MULTIPLY);
						setState(391);
						((MultiplicativeContext)_localctx).right = ((MultiplicativeContext)_localctx).unary = unary();
						 ((MultiplicativeContext)_localctx).ast =  new AST.BinExpr(loc((((MultiplicativeContext)_localctx).left!=null?(((MultiplicativeContext)_localctx).left.start):null), (((MultiplicativeContext)_localctx).right!=null?(((MultiplicativeContext)_localctx).right.stop):null)), AST.BinExpr.Oper.MUL, ((MultiplicativeContext)_localctx).left.ast, ((MultiplicativeContext)_localctx).right.ast); 
						}
						break;
					case 2:
						{
						_localctx = new MultiplicativeContext(_parentctx, _parentState);
						_localctx.left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_multiplicative);
						setState(394);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(395);
						match(DIVIDE);
						setState(396);
						((MultiplicativeContext)_localctx).right = ((MultiplicativeContext)_localctx).unary = unary();
						 ((MultiplicativeContext)_localctx).ast =  new AST.BinExpr(loc((((MultiplicativeContext)_localctx).left!=null?(((MultiplicativeContext)_localctx).left.start):null), (((MultiplicativeContext)_localctx).right!=null?(((MultiplicativeContext)_localctx).right.stop):null)), AST.BinExpr.Oper.DIV, ((MultiplicativeContext)_localctx).left.ast, ((MultiplicativeContext)_localctx).right.ast); 
						}
						break;
					case 3:
						{
						_localctx = new MultiplicativeContext(_parentctx, _parentState);
						_localctx.left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_multiplicative);
						setState(399);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(400);
						match(MODULO);
						setState(401);
						((MultiplicativeContext)_localctx).right = ((MultiplicativeContext)_localctx).unary = unary();
						 ((MultiplicativeContext)_localctx).ast =  new AST.BinExpr(loc((((MultiplicativeContext)_localctx).left!=null?(((MultiplicativeContext)_localctx).left.start):null), (((MultiplicativeContext)_localctx).right!=null?(((MultiplicativeContext)_localctx).right.stop):null)), AST.BinExpr.Oper.MOD, ((MultiplicativeContext)_localctx).left.ast, ((MultiplicativeContext)_localctx).right.ast); 
						}
						break;
					}
					} 
				}
				setState(408);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class UnaryContext extends ParserRuleContext {
		public AST.Expr ast;
		public Token PLUS;
		public UnaryContext right;
		public Token MINUS;
		public Token op;
		public PostfixContext postfix;
		public TerminalNode PLUS() { return getToken(Prev25Parser.PLUS, 0); }
		public UnaryContext unary() {
			return getRuleContext(UnaryContext.class,0);
		}
		public TerminalNode MINUS() { return getToken(Prev25Parser.MINUS, 0); }
		public TerminalNode EXCLAMATION() { return getToken(Prev25Parser.EXCLAMATION, 0); }
		public TerminalNode CARET() { return getToken(Prev25Parser.CARET, 0); }
		public PostfixContext postfix() {
			return getRuleContext(PostfixContext.class,0);
		}
		public UnaryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unary; }
	}

	public final UnaryContext unary() throws RecognitionException {
		UnaryContext _localctx = new UnaryContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_unary);
		try {
			setState(428);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PLUS:
				enterOuterAlt(_localctx, 1);
				{
				setState(409);
				((UnaryContext)_localctx).PLUS = match(PLUS);
				setState(410);
				((UnaryContext)_localctx).right = unary();
				 ((UnaryContext)_localctx).ast =  new AST.PfxExpr(loc(((UnaryContext)_localctx).PLUS, (((UnaryContext)_localctx).right!=null?(((UnaryContext)_localctx).right.stop):null)), AST.PfxExpr.Oper.ADD, ((UnaryContext)_localctx).right.ast); 
				}
				break;
			case MINUS:
				enterOuterAlt(_localctx, 2);
				{
				setState(413);
				((UnaryContext)_localctx).MINUS = match(MINUS);
				setState(414);
				((UnaryContext)_localctx).right = unary();
				 ((UnaryContext)_localctx).ast =  new AST.PfxExpr(loc(((UnaryContext)_localctx).MINUS, (((UnaryContext)_localctx).right!=null?(((UnaryContext)_localctx).right.stop):null)), AST.PfxExpr.Oper.SUB, ((UnaryContext)_localctx).right.ast); 
				}
				break;
			case EXCLAMATION:
				enterOuterAlt(_localctx, 3);
				{
				setState(417);
				((UnaryContext)_localctx).op = match(EXCLAMATION);
				setState(418);
				((UnaryContext)_localctx).right = unary();
				 ((UnaryContext)_localctx).ast =  new AST.PfxExpr(loc(((UnaryContext)_localctx).op, (((UnaryContext)_localctx).right!=null?(((UnaryContext)_localctx).right.stop):null)), AST.PfxExpr.Oper.NOT, ((UnaryContext)_localctx).right.ast); 
				}
				break;
			case CARET:
				enterOuterAlt(_localctx, 4);
				{
				setState(421);
				((UnaryContext)_localctx).op = match(CARET);
				setState(422);
				((UnaryContext)_localctx).right = unary();
				 ((UnaryContext)_localctx).ast =  new AST.PfxExpr(loc(((UnaryContext)_localctx).op, (((UnaryContext)_localctx).right!=null?(((UnaryContext)_localctx).right.stop):null)), AST.PfxExpr.Oper.PTR, ((UnaryContext)_localctx).right.ast); 
				}
				break;
			case INTEGER_CONSTANT:
			case CHAR_CONSTANT:
			case STRING_CONSTANT:
			case LBRACE:
			case LPAREN:
			case FALSE:
			case NULL:
			case SIZEOF:
			case TRUE:
			case ID:
				enterOuterAlt(_localctx, 5);
				{
				setState(425);
				((UnaryContext)_localctx).postfix = postfix(0);
				 ((UnaryContext)_localctx).ast =  ((UnaryContext)_localctx).postfix.ast; 
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PostfixContext extends ParserRuleContext {
		public AST.Expr ast;
		public PostfixContext left;
		public PostfixContext func;
		public PrimaryContext primary;
		public ExprContext index;
		public Token RBRACKET;
		public Token ID;
		public Token CARET;
		public ExprListContext args;
		public Token RPAREN;
		public PrimaryContext primary() {
			return getRuleContext(PrimaryContext.class,0);
		}
		public TerminalNode LBRACKET() { return getToken(Prev25Parser.LBRACKET, 0); }
		public TerminalNode RBRACKET() { return getToken(Prev25Parser.RBRACKET, 0); }
		public PostfixContext postfix() {
			return getRuleContext(PostfixContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode DOT() { return getToken(Prev25Parser.DOT, 0); }
		public TerminalNode ID() { return getToken(Prev25Parser.ID, 0); }
		public TerminalNode CARET() { return getToken(Prev25Parser.CARET, 0); }
		public TerminalNode LPAREN() { return getToken(Prev25Parser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(Prev25Parser.RPAREN, 0); }
		public ExprListContext exprList() {
			return getRuleContext(ExprListContext.class,0);
		}
		public PostfixContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_postfix; }
	}

	public final PostfixContext postfix() throws RecognitionException {
		return postfix(0);
	}

	private PostfixContext postfix(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		PostfixContext _localctx = new PostfixContext(_ctx, _parentState);
		PostfixContext _prevctx = _localctx;
		int _startState = 58;
		enterRecursionRule(_localctx, 58, RULE_postfix, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(431);
			((PostfixContext)_localctx).primary = primary();
			 ((PostfixContext)_localctx).ast =  ((PostfixContext)_localctx).primary.ast; 
			}
			_ctx.stop = _input.LT(-1);
			setState(455);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(453);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
					case 1:
						{
						_localctx = new PostfixContext(_parentctx, _parentState);
						_localctx.left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_postfix);
						setState(434);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(435);
						match(LBRACKET);
						setState(436);
						((PostfixContext)_localctx).index = expr();
						setState(437);
						((PostfixContext)_localctx).RBRACKET = match(RBRACKET);
						 ((PostfixContext)_localctx).ast =  new AST.ArrExpr(loc((((PostfixContext)_localctx).left!=null?(((PostfixContext)_localctx).left.start):null), ((PostfixContext)_localctx).RBRACKET), ((PostfixContext)_localctx).left.ast, ((PostfixContext)_localctx).index.ast); 
						}
						break;
					case 2:
						{
						_localctx = new PostfixContext(_parentctx, _parentState);
						_localctx.left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_postfix);
						setState(440);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(441);
						match(DOT);
						setState(442);
						((PostfixContext)_localctx).ID = match(ID);
						 ((PostfixContext)_localctx).ast =  new AST.CompExpr(loc((((PostfixContext)_localctx).left!=null?(((PostfixContext)_localctx).left.start):null), ((PostfixContext)_localctx).ID), ((PostfixContext)_localctx).left.ast, (((PostfixContext)_localctx).ID!=null?((PostfixContext)_localctx).ID.getText():null)); 
						}
						break;
					case 3:
						{
						_localctx = new PostfixContext(_parentctx, _parentState);
						_localctx.left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_postfix);
						setState(444);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(445);
						((PostfixContext)_localctx).CARET = match(CARET);
						 ((PostfixContext)_localctx).ast =  new AST.SfxExpr(loc((((PostfixContext)_localctx).left!=null?(((PostfixContext)_localctx).left.start):null), ((PostfixContext)_localctx).CARET), AST.SfxExpr.Oper.PTR, ((PostfixContext)_localctx).left.ast); 
						}
						break;
					case 4:
						{
						_localctx = new PostfixContext(_parentctx, _parentState);
						_localctx.func = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_postfix);
						setState(447);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(448);
						match(LPAREN);
						setState(449);
						((PostfixContext)_localctx).args = exprList();
						setState(450);
						((PostfixContext)_localctx).RPAREN = match(RPAREN);
						 ((PostfixContext)_localctx).ast =  new AST.CallExpr(loc((((PostfixContext)_localctx).func!=null?(((PostfixContext)_localctx).func.start):null), ((PostfixContext)_localctx).RPAREN), ((PostfixContext)_localctx).func.ast, ((PostfixContext)_localctx).args.ast); 
						}
						break;
					}
					} 
				}
				setState(457);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrimaryContext extends ParserRuleContext {
		public AST.Expr ast;
		public Token INTEGER_CONSTANT;
		public Token CHAR_CONSTANT;
		public Token STRING_CONSTANT;
		public Token TRUE;
		public Token FALSE;
		public Token NULL;
		public Token SIZEOF;
		public TypeExprContext typeExpr;
		public Token LBRACE;
		public ExprContext e;
		public TypeExprContext t;
		public Token RBRACE;
		public Token LPAREN;
		public ExprContext expr;
		public Token RPAREN;
		public Token ID;
		public TerminalNode INTEGER_CONSTANT() { return getToken(Prev25Parser.INTEGER_CONSTANT, 0); }
		public TerminalNode CHAR_CONSTANT() { return getToken(Prev25Parser.CHAR_CONSTANT, 0); }
		public TerminalNode STRING_CONSTANT() { return getToken(Prev25Parser.STRING_CONSTANT, 0); }
		public TerminalNode TRUE() { return getToken(Prev25Parser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(Prev25Parser.FALSE, 0); }
		public TerminalNode NULL() { return getToken(Prev25Parser.NULL, 0); }
		public TerminalNode SIZEOF() { return getToken(Prev25Parser.SIZEOF, 0); }
		public TypeExprContext typeExpr() {
			return getRuleContext(TypeExprContext.class,0);
		}
		public TerminalNode LBRACE() { return getToken(Prev25Parser.LBRACE, 0); }
		public TerminalNode COLON() { return getToken(Prev25Parser.COLON, 0); }
		public TerminalNode RBRACE() { return getToken(Prev25Parser.RBRACE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(Prev25Parser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(Prev25Parser.RPAREN, 0); }
		public TerminalNode ID() { return getToken(Prev25Parser.ID, 0); }
		public PrimaryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primary; }
	}

	public final PrimaryContext primary() throws RecognitionException {
		PrimaryContext _localctx = new PrimaryContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_primary);
		try {
			setState(488);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INTEGER_CONSTANT:
				enterOuterAlt(_localctx, 1);
				{
				setState(458);
				((PrimaryContext)_localctx).INTEGER_CONSTANT = match(INTEGER_CONSTANT);
				 ((PrimaryContext)_localctx).ast =  new AST.AtomExpr(loc(((PrimaryContext)_localctx).INTEGER_CONSTANT), AST.AtomExpr.Type.INT, (((PrimaryContext)_localctx).INTEGER_CONSTANT!=null?((PrimaryContext)_localctx).INTEGER_CONSTANT.getText():null)); 
				}
				break;
			case CHAR_CONSTANT:
				enterOuterAlt(_localctx, 2);
				{
				setState(460);
				((PrimaryContext)_localctx).CHAR_CONSTANT = match(CHAR_CONSTANT);
				 ((PrimaryContext)_localctx).ast =  new AST.AtomExpr(loc(((PrimaryContext)_localctx).CHAR_CONSTANT), AST.AtomExpr.Type.CHAR, (((PrimaryContext)_localctx).CHAR_CONSTANT!=null?((PrimaryContext)_localctx).CHAR_CONSTANT.getText():null)); 
				}
				break;
			case STRING_CONSTANT:
				enterOuterAlt(_localctx, 3);
				{
				setState(462);
				((PrimaryContext)_localctx).STRING_CONSTANT = match(STRING_CONSTANT);
				 ((PrimaryContext)_localctx).ast =  new AST.AtomExpr(loc(((PrimaryContext)_localctx).STRING_CONSTANT), AST.AtomExpr.Type.STR, (((PrimaryContext)_localctx).STRING_CONSTANT!=null?((PrimaryContext)_localctx).STRING_CONSTANT.getText():null)); 
				}
				break;
			case TRUE:
				enterOuterAlt(_localctx, 4);
				{
				setState(464);
				((PrimaryContext)_localctx).TRUE = match(TRUE);
				 ((PrimaryContext)_localctx).ast =  new AST.AtomExpr(loc(((PrimaryContext)_localctx).TRUE), AST.AtomExpr.Type.BOOL, "true"); 
				}
				break;
			case FALSE:
				enterOuterAlt(_localctx, 5);
				{
				setState(466);
				((PrimaryContext)_localctx).FALSE = match(FALSE);
				 ((PrimaryContext)_localctx).ast =  new AST.AtomExpr(loc(((PrimaryContext)_localctx).FALSE), AST.AtomExpr.Type.BOOL, "false"); 
				}
				break;
			case NULL:
				enterOuterAlt(_localctx, 6);
				{
				setState(468);
				((PrimaryContext)_localctx).NULL = match(NULL);
				 ((PrimaryContext)_localctx).ast =  new AST.AtomExpr(loc(((PrimaryContext)_localctx).NULL), AST.AtomExpr.Type.PTR, "0"); 
				}
				break;
			case SIZEOF:
				enterOuterAlt(_localctx, 7);
				{
				setState(470);
				((PrimaryContext)_localctx).SIZEOF = match(SIZEOF);
				setState(471);
				((PrimaryContext)_localctx).typeExpr = typeExpr();
				 ((PrimaryContext)_localctx).ast =  new AST.SizeExpr(loc(((PrimaryContext)_localctx).SIZEOF, (((PrimaryContext)_localctx).typeExpr!=null?(((PrimaryContext)_localctx).typeExpr.stop):null)), ((PrimaryContext)_localctx).typeExpr.ast); 
				}
				break;
			case LBRACE:
				enterOuterAlt(_localctx, 8);
				{
				setState(474);
				((PrimaryContext)_localctx).LBRACE = match(LBRACE);
				setState(475);
				((PrimaryContext)_localctx).e = expr();
				setState(476);
				match(COLON);
				setState(477);
				((PrimaryContext)_localctx).t = typeExpr();
				setState(478);
				((PrimaryContext)_localctx).RBRACE = match(RBRACE);
				 ((PrimaryContext)_localctx).ast =  new AST.CastExpr(loc(((PrimaryContext)_localctx).LBRACE, ((PrimaryContext)_localctx).RBRACE), ((PrimaryContext)_localctx).t.ast, ((PrimaryContext)_localctx).e.ast); 
				}
				break;
			case LPAREN:
				enterOuterAlt(_localctx, 9);
				{
				setState(481);
				((PrimaryContext)_localctx).LPAREN = match(LPAREN);
				setState(482);
				((PrimaryContext)_localctx).e = ((PrimaryContext)_localctx).expr = expr();
				setState(483);
				((PrimaryContext)_localctx).RPAREN = match(RPAREN);
				 ((PrimaryContext)_localctx).expr.ast.relocate(loc(((PrimaryContext)_localctx).LPAREN, ((PrimaryContext)_localctx).RPAREN));((PrimaryContext)_localctx).ast =  ((PrimaryContext)_localctx).e.ast; 
				}
				break;
			case ID:
				enterOuterAlt(_localctx, 10);
				{
				setState(486);
				((PrimaryContext)_localctx).ID = match(ID);
				 ((PrimaryContext)_localctx).ast =  new AST.NameExpr(loc(((PrimaryContext)_localctx).ID), (((PrimaryContext)_localctx).ID!=null?((PrimaryContext)_localctx).ID.getText():null)); 
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprListContext extends ParserRuleContext {
		public List<AST.Expr> ast;
		public Expr_moreContext expr_more;
		public Expr_moreContext expr_more() {
			return getRuleContext(Expr_moreContext.class,0);
		}
		public ExprListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprList; }
	}

	public final ExprListContext exprList() throws RecognitionException {
		ExprListContext _localctx = new ExprListContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_exprList);
		try {
			setState(494);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case RPAREN:
				enterOuterAlt(_localctx, 1);
				{
				 ((ExprListContext)_localctx).ast =  new ArrayList<>(); 
				}
				break;
			case INTEGER_CONSTANT:
			case CHAR_CONSTANT:
			case STRING_CONSTANT:
			case PLUS:
			case MINUS:
			case EXCLAMATION:
			case CARET:
			case LBRACE:
			case LPAREN:
			case FALSE:
			case NULL:
			case SIZEOF:
			case TRUE:
			case ID:
				enterOuterAlt(_localctx, 2);
				{
				setState(491);
				((ExprListContext)_localctx).expr_more = expr_more();
				 ((ExprListContext)_localctx).ast =  ((ExprListContext)_localctx).expr_more.ast; 
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Expr_moreContext extends ParserRuleContext {
		public List<AST.Expr> ast;
		public ExprContext expr;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(Prev25Parser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(Prev25Parser.COMMA, i);
		}
		public Expr_moreContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr_more; }
	}

	public final Expr_moreContext expr_more() throws RecognitionException {
		Expr_moreContext _localctx = new Expr_moreContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_expr_more);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(496);
			((Expr_moreContext)_localctx).expr = expr();
			 ((Expr_moreContext)_localctx).ast =  new ArrayList<>(); _localctx.ast.add(((Expr_moreContext)_localctx).expr.ast); 
			setState(504);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(498);
				match(COMMA);
				setState(499);
				((Expr_moreContext)_localctx).expr = expr();
				 _localctx.ast.add(((Expr_moreContext)_localctx).expr.ast); 
				}
				}
				setState(506);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 1:
			return defs_sempred((DefsContext)_localctx, predIndex);
		case 23:
			return logic_or_sempred((Logic_orContext)_localctx, predIndex);
		case 24:
			return logic_and_sempred((Logic_andContext)_localctx, predIndex);
		case 26:
			return additive_sempred((AdditiveContext)_localctx, predIndex);
		case 27:
			return multiplicative_sempred((MultiplicativeContext)_localctx, predIndex);
		case 29:
			return postfix_sempred((PostfixContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean defs_sempred(DefsContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean logic_or_sempred(Logic_orContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean logic_and_sempred(Logic_andContext _localctx, int predIndex) {
		switch (predIndex) {
		case 2:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean additive_sempred(AdditiveContext _localctx, int predIndex) {
		switch (predIndex) {
		case 3:
			return precpred(_ctx, 3);
		case 4:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean multiplicative_sempred(MultiplicativeContext _localctx, int predIndex) {
		switch (predIndex) {
		case 5:
			return precpred(_ctx, 4);
		case 6:
			return precpred(_ctx, 3);
		case 7:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean postfix_sempred(PostfixContext _localctx, int predIndex) {
		switch (predIndex) {
		case 8:
			return precpred(_ctx, 4);
		case 9:
			return precpred(_ctx, 3);
		case 10:
			return precpred(_ctx, 2);
		case 11:
			return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u00013\u01fc\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007\u001b"+
		"\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007\u001e"+
		"\u0002\u001f\u0007\u001f\u0002 \u0007 \u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0005\u0001O\b\u0001\n\u0001\f\u0001"+
		"R\t\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0003\u0002`\b\u0002\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0003\u0007\u0086\b\u0007\u0001\b"+
		"\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0005\b\u008e\b\b\n\b\f\b\u0091"+
		"\t\b\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0005\n\u009e\b\n\n\n\f\n\u00a1\t\n\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0003\f\u00ac\b\f\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0005"+
		"\r\u00b4\b\r\n\r\f\r\u00b7\t\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0003\u000e\u00d1\b\u000e\u0001\u000f\u0001"+
		"\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001"+
		"\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001"+
		"\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0004"+
		"\u0012\u00ec\b\u0012\u000b\u0012\f\u0012\u00ed\u0001\u0012\u0001\u0012"+
		"\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0003\u0013\u011a\b\u0013"+
		"\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014"+
		"\u0001\u0014\u0005\u0014\u0123\b\u0014\n\u0014\f\u0014\u0126\t\u0014\u0003"+
		"\u0014\u0128\b\u0014\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0016\u0001"+
		"\u0016\u0001\u0016\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001"+
		"\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0005\u0017\u0139"+
		"\b\u0017\n\u0017\f\u0017\u013c\t\u0017\u0001\u0018\u0001\u0018\u0001\u0018"+
		"\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018"+
		"\u0005\u0018\u0147\b\u0018\n\u0018\f\u0018\u014a\t\u0018\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0003\u0019\u016d\b\u0019\u0001\u001a\u0001\u001a\u0001"+
		"\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001"+
		"\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0005"+
		"\u001a\u017d\b\u001a\n\u001a\f\u001a\u0180\t\u001a\u0001\u001b\u0001\u001b"+
		"\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b"+
		"\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b"+
		"\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0005\u001b"+
		"\u0195\b\u001b\n\u001b\f\u001b\u0198\t\u001b\u0001\u001c\u0001\u001c\u0001"+
		"\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001"+
		"\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001"+
		"\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0003\u001c\u01ad"+
		"\b\u001c\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001"+
		"\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001"+
		"\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001"+
		"\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0005"+
		"\u001d\u01c6\b\u001d\n\u001d\f\u001d\u01c9\t\u001d\u0001\u001e\u0001\u001e"+
		"\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e"+
		"\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e"+
		"\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e"+
		"\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e"+
		"\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0003\u001e\u01e9\b\u001e"+
		"\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0003\u001f\u01ef\b\u001f"+
		"\u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0005 \u01f7\b \n \f \u01fa"+
		"\t \u0001 \u0000\u0006\u0002.046:!\u0000\u0002\u0004\u0006\b\n\f\u000e"+
		"\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,.02468:<>@\u0000"+
		"\u0000\u0215\u0000B\u0001\u0000\u0000\u0000\u0002F\u0001\u0000\u0000\u0000"+
		"\u0004_\u0001\u0000\u0000\u0000\u0006a\u0001\u0000\u0000\u0000\bg\u0001"+
		"\u0000\u0000\u0000\nm\u0001\u0000\u0000\u0000\fv\u0001\u0000\u0000\u0000"+
		"\u000e\u0085\u0001\u0000\u0000\u0000\u0010\u0087\u0001\u0000\u0000\u0000"+
		"\u0012\u0092\u0001\u0000\u0000\u0000\u0014\u0097\u0001\u0000\u0000\u0000"+
		"\u0016\u00a2\u0001\u0000\u0000\u0000\u0018\u00ab\u0001\u0000\u0000\u0000"+
		"\u001a\u00ad\u0001\u0000\u0000\u0000\u001c\u00d0\u0001\u0000\u0000\u0000"+
		"\u001e\u00d2\u0001\u0000\u0000\u0000 \u00d9\u0001\u0000\u0000\u0000\""+
		"\u00e0\u0001\u0000\u0000\u0000$\u00e9\u0001\u0000\u0000\u0000&\u0119\u0001"+
		"\u0000\u0000\u0000(\u0127\u0001\u0000\u0000\u0000*\u0129\u0001\u0000\u0000"+
		"\u0000,\u012c\u0001\u0000\u0000\u0000.\u012f\u0001\u0000\u0000\u00000"+
		"\u013d\u0001\u0000\u0000\u00002\u016c\u0001\u0000\u0000\u00004\u016e\u0001"+
		"\u0000\u0000\u00006\u0181\u0001\u0000\u0000\u00008\u01ac\u0001\u0000\u0000"+
		"\u0000:\u01ae\u0001\u0000\u0000\u0000<\u01e8\u0001\u0000\u0000\u0000>"+
		"\u01ee\u0001\u0000\u0000\u0000@\u01f0\u0001\u0000\u0000\u0000BC\u0003"+
		"\u0002\u0001\u0000CD\u0005\u0000\u0000\u0001DE\u0006\u0000\uffff\uffff"+
		"\u0000E\u0001\u0001\u0000\u0000\u0000FG\u0006\u0001\uffff\uffff\u0000"+
		"GH\u0003\u0004\u0002\u0000HI\u0006\u0001\uffff\uffff\u0000IP\u0001\u0000"+
		"\u0000\u0000JK\n\u0001\u0000\u0000KL\u0003\u0004\u0002\u0000LM\u0006\u0001"+
		"\uffff\uffff\u0000MO\u0001\u0000\u0000\u0000NJ\u0001\u0000\u0000\u0000"+
		"OR\u0001\u0000\u0000\u0000PN\u0001\u0000\u0000\u0000PQ\u0001\u0000\u0000"+
		"\u0000Q\u0003\u0001\u0000\u0000\u0000RP\u0001\u0000\u0000\u0000ST\u0003"+
		"\u0006\u0003\u0000TU\u0006\u0002\uffff\uffff\u0000U`\u0001\u0000\u0000"+
		"\u0000VW\u0003\b\u0004\u0000WX\u0006\u0002\uffff\uffff\u0000X`\u0001\u0000"+
		"\u0000\u0000YZ\u0003\n\u0005\u0000Z[\u0006\u0002\uffff\uffff\u0000[`\u0001"+
		"\u0000\u0000\u0000\\]\u0003\f\u0006\u0000]^\u0006\u0002\uffff\uffff\u0000"+
		"^`\u0001\u0000\u0000\u0000_S\u0001\u0000\u0000\u0000_V\u0001\u0000\u0000"+
		"\u0000_Y\u0001\u0000\u0000\u0000_\\\u0001\u0000\u0000\u0000`\u0005\u0001"+
		"\u0000\u0000\u0000ab\u0005.\u0000\u0000bc\u00052\u0000\u0000cd\u0005\u0015"+
		"\u0000\u0000de\u0003&\u0013\u0000ef\u0006\u0003\uffff\uffff\u0000f\u0007"+
		"\u0001\u0000\u0000\u0000gh\u0005/\u0000\u0000hi\u00052\u0000\u0000ij\u0005"+
		"\u0016\u0000\u0000jk\u0003&\u0013\u0000kl\u0006\u0004\uffff\uffff\u0000"+
		"l\t\u0001\u0000\u0000\u0000mn\u0005$\u0000\u0000no\u00052\u0000\u0000"+
		"op\u0005\u001a\u0000\u0000pq\u0003\u000e\u0007\u0000qr\u0005\u001b\u0000"+
		"\u0000rs\u0005\u0016\u0000\u0000st\u0003&\u0013\u0000tu\u0006\u0005\uffff"+
		"\uffff\u0000u\u000b\u0001\u0000\u0000\u0000vw\u0005$\u0000\u0000wx\u0005"+
		"2\u0000\u0000xy\u0005\u001a\u0000\u0000yz\u0003\u000e\u0007\u0000z{\u0005"+
		"\u001b\u0000\u0000{|\u0005\u0016\u0000\u0000|}\u0003&\u0013\u0000}~\u0005"+
		"\u0015\u0000\u0000~\u007f\u0003\u0018\f\u0000\u007f\u0080\u0006\u0006"+
		"\uffff\uffff\u0000\u0080\r\u0001\u0000\u0000\u0000\u0081\u0082\u0003\u0010"+
		"\b\u0000\u0082\u0083\u0006\u0007\uffff\uffff\u0000\u0083\u0086\u0001\u0000"+
		"\u0000\u0000\u0084\u0086\u0006\u0007\uffff\uffff\u0000\u0085\u0081\u0001"+
		"\u0000\u0000\u0000\u0085\u0084\u0001\u0000\u0000\u0000\u0086\u000f\u0001"+
		"\u0000\u0000\u0000\u0087\u0088\u0003\u0012\t\u0000\u0088\u008f\u0006\b"+
		"\uffff\uffff\u0000\u0089\u008a\u0005\u0017\u0000\u0000\u008a\u008b\u0003"+
		"\u0012\t\u0000\u008b\u008c\u0006\b\uffff\uffff\u0000\u008c\u008e\u0001"+
		"\u0000\u0000\u0000\u008d\u0089\u0001\u0000\u0000\u0000\u008e\u0091\u0001"+
		"\u0000\u0000\u0000\u008f\u008d\u0001\u0000\u0000\u0000\u008f\u0090\u0001"+
		"\u0000\u0000\u0000\u0090\u0011\u0001\u0000\u0000\u0000\u0091\u008f\u0001"+
		"\u0000\u0000\u0000\u0092\u0093\u00052\u0000\u0000\u0093\u0094\u0005\u0016"+
		"\u0000\u0000\u0094\u0095\u0003&\u0013\u0000\u0095\u0096\u0006\t\uffff"+
		"\uffff\u0000\u0096\u0013\u0001\u0000\u0000\u0000\u0097\u0098\u0003\u0016"+
		"\u000b\u0000\u0098\u009f\u0006\n\uffff\uffff\u0000\u0099\u009a\u0005\u0017"+
		"\u0000\u0000\u009a\u009b\u0003\u0016\u000b\u0000\u009b\u009c\u0006\n\uffff"+
		"\uffff\u0000\u009c\u009e\u0001\u0000\u0000\u0000\u009d\u0099\u0001\u0000"+
		"\u0000\u0000\u009e\u00a1\u0001\u0000\u0000\u0000\u009f\u009d\u0001\u0000"+
		"\u0000\u0000\u009f\u00a0\u0001\u0000\u0000\u0000\u00a0\u0015\u0001\u0000"+
		"\u0000\u0000\u00a1\u009f\u0001\u0000\u0000\u0000\u00a2\u00a3\u00052\u0000"+
		"\u0000\u00a3\u00a4\u0005\u0016\u0000\u0000\u00a4\u00a5\u0003&\u0013\u0000"+
		"\u00a5\u00a6\u0006\u000b\uffff\uffff\u0000\u00a6\u0017\u0001\u0000\u0000"+
		"\u0000\u00a7\u00ac\u0006\f\uffff\uffff\u0000\u00a8\u00a9\u0003\u001a\r"+
		"\u0000\u00a9\u00aa\u0006\f\uffff\uffff\u0000\u00aa\u00ac\u0001\u0000\u0000"+
		"\u0000\u00ab\u00a7\u0001\u0000\u0000\u0000\u00ab\u00a8\u0001\u0000\u0000"+
		"\u0000\u00ac\u0019\u0001\u0000\u0000\u0000\u00ad\u00ae\u0003\u001c\u000e"+
		"\u0000\u00ae\u00b5\u0006\r\uffff\uffff\u0000\u00af\u00b0\u0005\u0017\u0000"+
		"\u0000\u00b0\u00b1\u0003\u001c\u000e\u0000\u00b1\u00b2\u0006\r\uffff\uffff"+
		"\u0000\u00b2\u00b4\u0001\u0000\u0000\u0000\u00b3\u00af\u0001\u0000\u0000"+
		"\u0000\u00b4\u00b7\u0001\u0000\u0000\u0000\u00b5\u00b3\u0001\u0000\u0000"+
		"\u0000\u00b5\u00b6\u0001\u0000\u0000\u0000\u00b6\u001b\u0001\u0000\u0000"+
		"\u0000\u00b7\u00b5\u0001\u0000\u0000\u0000\u00b8\u00b9\u0003*\u0015\u0000"+
		"\u00b9\u00ba\u0006\u000e\uffff\uffff\u0000\u00ba\u00d1\u0001\u0000\u0000"+
		"\u0000\u00bb\u00bc\u0003*\u0015\u0000\u00bc\u00bd\u0005\u0015\u0000\u0000"+
		"\u00bd\u00be\u0003*\u0015\u0000\u00be\u00bf\u0006\u000e\uffff\uffff\u0000"+
		"\u00bf\u00d1\u0001\u0000\u0000\u0000\u00c0\u00c1\u0005*\u0000\u0000\u00c1"+
		"\u00c2\u0003*\u0015\u0000\u00c2\u00c3\u0006\u000e\uffff\uffff\u0000\u00c3"+
		"\u00d1\u0001\u0000\u0000\u0000\u00c4\u00c5\u0003\u001e\u000f\u0000\u00c5"+
		"\u00c6\u0006\u000e\uffff\uffff\u0000\u00c6\u00d1\u0001\u0000\u0000\u0000"+
		"\u00c7\u00c8\u0003 \u0010\u0000\u00c8\u00c9\u0006\u000e\uffff\uffff\u0000"+
		"\u00c9\u00d1\u0001\u0000\u0000\u0000\u00ca\u00cb\u0003\"\u0011\u0000\u00cb"+
		"\u00cc\u0006\u000e\uffff\uffff\u0000\u00cc\u00d1\u0001\u0000\u0000\u0000"+
		"\u00cd\u00ce\u0003$\u0012\u0000\u00ce\u00cf\u0006\u000e\uffff\uffff\u0000"+
		"\u00cf\u00d1\u0001\u0000\u0000\u0000\u00d0\u00b8\u0001\u0000\u0000\u0000"+
		"\u00d0\u00bb\u0001\u0000\u0000\u0000\u00d0\u00c0\u0001\u0000\u0000\u0000"+
		"\u00d0\u00c4\u0001\u0000\u0000\u0000\u00d0\u00c7\u0001\u0000\u0000\u0000"+
		"\u00d0\u00ca\u0001\u0000\u0000\u0000\u00d0\u00cd\u0001\u0000\u0000\u0000"+
		"\u00d1\u001d\u0001\u0000\u0000\u0000\u00d2\u00d3\u00051\u0000\u0000\u00d3"+
		"\u00d4\u0003*\u0015\u0000\u00d4\u00d5\u0005 \u0000\u0000\u00d5\u00d6\u0003"+
		"\u0018\f\u0000\u00d6\u00d7\u0005\"\u0000\u0000\u00d7\u00d8\u0006\u000f"+
		"\uffff\uffff\u0000\u00d8\u001f\u0001\u0000\u0000\u0000\u00d9\u00da\u0005"+
		"%\u0000\u0000\u00da\u00db\u0003*\u0015\u0000\u00db\u00dc\u0005,\u0000"+
		"\u0000\u00dc\u00dd\u0003\u0018\f\u0000\u00dd\u00de\u0005\"\u0000\u0000"+
		"\u00de\u00df\u0006\u0010\uffff\uffff\u0000\u00df!\u0001\u0000\u0000\u0000"+
		"\u00e0\u00e1\u0005%\u0000\u0000\u00e1\u00e2\u0003*\u0015\u0000\u00e2\u00e3"+
		"\u0005,\u0000\u0000\u00e3\u00e4\u0003\u0018\f\u0000\u00e4\u00e5\u0005"+
		"!\u0000\u0000\u00e5\u00e6\u0003\u0018\f\u0000\u00e6\u00e7\u0005\"\u0000"+
		"\u0000\u00e7\u00e8\u0006\u0011\uffff\uffff\u0000\u00e8#\u0001\u0000\u0000"+
		"\u0000\u00e9\u00eb\u0005(\u0000\u0000\u00ea\u00ec\u0003\u0002\u0001\u0000"+
		"\u00eb\u00ea\u0001\u0000\u0000\u0000\u00ec\u00ed\u0001\u0000\u0000\u0000"+
		"\u00ed\u00eb\u0001\u0000\u0000\u0000\u00ed\u00ee\u0001\u0000\u0000\u0000"+
		"\u00ee\u00ef\u0001\u0000\u0000\u0000\u00ef\u00f0\u0005\'\u0000\u0000\u00f0"+
		"\u00f1\u0003\u001a\r\u0000\u00f1\u00f2\u0005\"\u0000\u0000\u00f2\u00f3"+
		"\u0006\u0012\uffff\uffff\u0000\u00f3%\u0001\u0000\u0000\u0000\u00f4\u00f5"+
		"\u0005&\u0000\u0000\u00f5\u011a\u0006\u0013\uffff\uffff\u0000\u00f6\u00f7"+
		"\u0005\u001f\u0000\u0000\u00f7\u011a\u0006\u0013\uffff\uffff\u0000\u00f8"+
		"\u00f9\u0005\u001e\u0000\u0000\u00f9\u011a\u0006\u0013\uffff\uffff\u0000"+
		"\u00fa\u00fb\u00050\u0000\u0000\u00fb\u011a\u0006\u0013\uffff\uffff\u0000"+
		"\u00fc\u00fd\u00052\u0000\u0000\u00fd\u011a\u0006\u0013\uffff\uffff\u0000"+
		"\u00fe\u00ff\u0005\u001c\u0000\u0000\u00ff\u0100\u0005\u0002\u0000\u0000"+
		"\u0100\u0101\u0005\u001d\u0000\u0000\u0101\u0102\u0003&\u0013\u0000\u0102"+
		"\u0103\u0006\u0013\uffff\uffff\u0000\u0103\u011a\u0001\u0000\u0000\u0000"+
		"\u0104\u0105\u0005\u0014\u0000\u0000\u0105\u0106\u0003&\u0013\u0000\u0106"+
		"\u0107\u0006\u0013\uffff\uffff\u0000\u0107\u011a\u0001\u0000\u0000\u0000"+
		"\u0108\u0109\u0005\t\u0000\u0000\u0109\u010a\u0003\u0014\n\u0000\u010a"+
		"\u010b\u0005\n\u0000\u0000\u010b\u010c\u0006\u0013\uffff\uffff\u0000\u010c"+
		"\u011a\u0001\u0000\u0000\u0000\u010d\u010e\u0005\u0018\u0000\u0000\u010e"+
		"\u010f\u0003\u0014\n\u0000\u010f\u0110\u0005\u0019\u0000\u0000\u0110\u0111"+
		"\u0006\u0013\uffff\uffff\u0000\u0111\u011a\u0001\u0000\u0000\u0000\u0112"+
		"\u0113\u0005\u001a\u0000\u0000\u0113\u0114\u0003(\u0014\u0000\u0114\u0115"+
		"\u0005\u001b\u0000\u0000\u0115\u0116\u0005\u0016\u0000\u0000\u0116\u0117"+
		"\u0003&\u0013\u0000\u0117\u0118\u0006\u0013\uffff\uffff\u0000\u0118\u011a"+
		"\u0001\u0000\u0000\u0000\u0119\u00f4\u0001\u0000\u0000\u0000\u0119\u00f6"+
		"\u0001\u0000\u0000\u0000\u0119\u00f8\u0001\u0000\u0000\u0000\u0119\u00fa"+
		"\u0001\u0000\u0000\u0000\u0119\u00fc\u0001\u0000\u0000\u0000\u0119\u00fe"+
		"\u0001\u0000\u0000\u0000\u0119\u0104\u0001\u0000\u0000\u0000\u0119\u0108"+
		"\u0001\u0000\u0000\u0000\u0119\u010d\u0001\u0000\u0000\u0000\u0119\u0112"+
		"\u0001\u0000\u0000\u0000\u011a\'\u0001\u0000\u0000\u0000\u011b\u0128\u0006"+
		"\u0014\uffff\uffff\u0000\u011c\u011d\u0003&\u0013\u0000\u011d\u0124\u0006"+
		"\u0014\uffff\uffff\u0000\u011e\u011f\u0005\u0017\u0000\u0000\u011f\u0120"+
		"\u0003&\u0013\u0000\u0120\u0121\u0006\u0014\uffff\uffff\u0000\u0121\u0123"+
		"\u0001\u0000\u0000\u0000\u0122\u011e\u0001\u0000\u0000\u0000\u0123\u0126"+
		"\u0001\u0000\u0000\u0000\u0124\u0122\u0001\u0000\u0000\u0000\u0124\u0125"+
		"\u0001\u0000\u0000\u0000\u0125\u0128\u0001\u0000\u0000\u0000\u0126\u0124"+
		"\u0001\u0000\u0000\u0000\u0127\u011b\u0001\u0000\u0000\u0000\u0127\u011c"+
		"\u0001\u0000\u0000\u0000\u0128)\u0001\u0000\u0000\u0000\u0129\u012a\u0003"+
		",\u0016\u0000\u012a\u012b\u0006\u0015\uffff\uffff\u0000\u012b+\u0001\u0000"+
		"\u0000\u0000\u012c\u012d\u0003.\u0017\u0000\u012d\u012e\u0006\u0016\uffff"+
		"\uffff\u0000\u012e-\u0001\u0000\u0000\u0000\u012f\u0130\u0006\u0017\uffff"+
		"\uffff\u0000\u0130\u0131\u00030\u0018\u0000\u0131\u0132\u0006\u0017\uffff"+
		"\uffff\u0000\u0132\u013a\u0001\u0000\u0000\u0000\u0133\u0134\n\u0002\u0000"+
		"\u0000\u0134\u0135\u0005\u0006\u0000\u0000\u0135\u0136\u00030\u0018\u0000"+
		"\u0136\u0137\u0006\u0017\uffff\uffff\u0000\u0137\u0139\u0001\u0000\u0000"+
		"\u0000\u0138\u0133\u0001\u0000\u0000\u0000\u0139\u013c\u0001\u0000\u0000"+
		"\u0000\u013a\u0138\u0001\u0000\u0000\u0000\u013a\u013b\u0001\u0000\u0000"+
		"\u0000\u013b/\u0001\u0000\u0000\u0000\u013c\u013a\u0001\u0000\u0000\u0000"+
		"\u013d\u013e\u0006\u0018\uffff\uffff\u0000\u013e\u013f\u00032\u0019\u0000"+
		"\u013f\u0140\u0006\u0018\uffff\uffff\u0000\u0140\u0148\u0001\u0000\u0000"+
		"\u0000\u0141\u0142\n\u0002\u0000\u0000\u0142\u0143\u0005\u0005\u0000\u0000"+
		"\u0143\u0144\u00032\u0019\u0000\u0144\u0145\u0006\u0018\uffff\uffff\u0000"+
		"\u0145\u0147\u0001\u0000\u0000\u0000\u0146\u0141\u0001\u0000\u0000\u0000"+
		"\u0147\u014a\u0001\u0000\u0000\u0000\u0148\u0146\u0001\u0000\u0000\u0000"+
		"\u0148\u0149\u0001\u0000\u0000\u0000\u01491\u0001\u0000\u0000\u0000\u014a"+
		"\u0148\u0001\u0000\u0000\u0000\u014b\u014c\u00034\u001a\u0000\u014c\u014d"+
		"\u0005\u0007\u0000\u0000\u014d\u014e\u00034\u001a\u0000\u014e\u014f\u0006"+
		"\u0019\uffff\uffff\u0000\u014f\u016d\u0001\u0000\u0000\u0000\u0150\u0151"+
		"\u00034\u001a\u0000\u0151\u0152\u0005\b\u0000\u0000\u0152\u0153\u0003"+
		"4\u001a\u0000\u0153\u0154\u0006\u0019\uffff\uffff\u0000\u0154\u016d\u0001"+
		"\u0000\u0000\u0000\u0155\u0156\u00034\u001a\u0000\u0156\u0157\u0005\t"+
		"\u0000\u0000\u0157\u0158\u00034\u001a\u0000\u0158\u0159\u0006\u0019\uffff"+
		"\uffff\u0000\u0159\u016d\u0001\u0000\u0000\u0000\u015a\u015b\u00034\u001a"+
		"\u0000\u015b\u015c\u0005\n\u0000\u0000\u015c\u015d\u00034\u001a\u0000"+
		"\u015d\u015e\u0006\u0019\uffff\uffff\u0000\u015e\u016d\u0001\u0000\u0000"+
		"\u0000\u015f\u0160\u00034\u001a\u0000\u0160\u0161\u0005\u000b\u0000\u0000"+
		"\u0161\u0162\u00034\u001a\u0000\u0162\u0163\u0006\u0019\uffff\uffff\u0000"+
		"\u0163\u016d\u0001\u0000\u0000\u0000\u0164\u0165\u00034\u001a\u0000\u0165"+
		"\u0166\u0005\f\u0000\u0000\u0166\u0167\u00034\u001a\u0000\u0167\u0168"+
		"\u0006\u0019\uffff\uffff\u0000\u0168\u016d\u0001\u0000\u0000\u0000\u0169"+
		"\u016a\u00034\u001a\u0000\u016a\u016b\u0006\u0019\uffff\uffff\u0000\u016b"+
		"\u016d\u0001\u0000\u0000\u0000\u016c\u014b\u0001\u0000\u0000\u0000\u016c"+
		"\u0150\u0001\u0000\u0000\u0000\u016c\u0155\u0001\u0000\u0000\u0000\u016c"+
		"\u015a\u0001\u0000\u0000\u0000\u016c\u015f\u0001\u0000\u0000\u0000\u016c"+
		"\u0164\u0001\u0000\u0000\u0000\u016c\u0169\u0001\u0000\u0000\u0000\u016d"+
		"3\u0001\u0000\u0000\u0000\u016e\u016f\u0006\u001a\uffff\uffff\u0000\u016f"+
		"\u0170\u00036\u001b\u0000\u0170\u0171\u0006\u001a\uffff\uffff\u0000\u0171"+
		"\u017e\u0001\u0000\u0000\u0000\u0172\u0173\n\u0003\u0000\u0000\u0173\u0174"+
		"\u0005\u0010\u0000\u0000\u0174\u0175\u00036\u001b\u0000\u0175\u0176\u0006"+
		"\u001a\uffff\uffff\u0000\u0176\u017d\u0001\u0000\u0000\u0000\u0177\u0178"+
		"\n\u0002\u0000\u0000\u0178\u0179\u0005\u0011\u0000\u0000\u0179\u017a\u0003"+
		"6\u001b\u0000\u017a\u017b\u0006\u001a\uffff\uffff\u0000\u017b\u017d\u0001"+
		"\u0000\u0000\u0000\u017c\u0172\u0001\u0000\u0000\u0000\u017c\u0177\u0001"+
		"\u0000\u0000\u0000\u017d\u0180\u0001\u0000\u0000\u0000\u017e\u017c\u0001"+
		"\u0000\u0000\u0000\u017e\u017f\u0001\u0000\u0000\u0000\u017f5\u0001\u0000"+
		"\u0000\u0000\u0180\u017e\u0001\u0000\u0000\u0000\u0181\u0182\u0006\u001b"+
		"\uffff\uffff\u0000\u0182\u0183\u00038\u001c\u0000\u0183\u0184\u0006\u001b"+
		"\uffff\uffff\u0000\u0184\u0196\u0001\u0000\u0000\u0000\u0185\u0186\n\u0004"+
		"\u0000\u0000\u0186\u0187\u0005\r\u0000\u0000\u0187\u0188\u00038\u001c"+
		"\u0000\u0188\u0189\u0006\u001b\uffff\uffff\u0000\u0189\u0195\u0001\u0000"+
		"\u0000\u0000\u018a\u018b\n\u0003\u0000\u0000\u018b\u018c\u0005\u000e\u0000"+
		"\u0000\u018c\u018d\u00038\u001c\u0000\u018d\u018e\u0006\u001b\uffff\uffff"+
		"\u0000\u018e\u0195\u0001\u0000\u0000\u0000\u018f\u0190\n\u0002\u0000\u0000"+
		"\u0190\u0191\u0005\u000f\u0000\u0000\u0191\u0192\u00038\u001c\u0000\u0192"+
		"\u0193\u0006\u001b\uffff\uffff\u0000\u0193\u0195\u0001\u0000\u0000\u0000"+
		"\u0194\u0185\u0001\u0000\u0000\u0000\u0194\u018a\u0001\u0000\u0000\u0000"+
		"\u0194\u018f\u0001\u0000\u0000\u0000\u0195\u0198\u0001\u0000\u0000\u0000"+
		"\u0196\u0194\u0001\u0000\u0000\u0000\u0196\u0197\u0001\u0000\u0000\u0000"+
		"\u01977\u0001\u0000\u0000\u0000\u0198\u0196\u0001\u0000\u0000\u0000\u0199"+
		"\u019a\u0005\u0010\u0000\u0000\u019a\u019b\u00038\u001c\u0000\u019b\u019c"+
		"\u0006\u001c\uffff\uffff\u0000\u019c\u01ad\u0001\u0000\u0000\u0000\u019d"+
		"\u019e\u0005\u0011\u0000\u0000\u019e\u019f\u00038\u001c\u0000\u019f\u01a0"+
		"\u0006\u001c\uffff\uffff\u0000\u01a0\u01ad\u0001\u0000\u0000\u0000\u01a1"+
		"\u01a2\u0005\u0012\u0000\u0000\u01a2\u01a3\u00038\u001c\u0000\u01a3\u01a4"+
		"\u0006\u001c\uffff\uffff\u0000\u01a4\u01ad\u0001\u0000\u0000\u0000\u01a5"+
		"\u01a6\u0005\u0014\u0000\u0000\u01a6\u01a7\u00038\u001c\u0000\u01a7\u01a8"+
		"\u0006\u001c\uffff\uffff\u0000\u01a8\u01ad\u0001\u0000\u0000\u0000\u01a9"+
		"\u01aa\u0003:\u001d\u0000\u01aa\u01ab\u0006\u001c\uffff\uffff\u0000\u01ab"+
		"\u01ad\u0001\u0000\u0000\u0000\u01ac\u0199\u0001\u0000\u0000\u0000\u01ac"+
		"\u019d\u0001\u0000\u0000\u0000\u01ac\u01a1\u0001\u0000\u0000\u0000\u01ac"+
		"\u01a5\u0001\u0000\u0000\u0000\u01ac\u01a9\u0001\u0000\u0000\u0000\u01ad"+
		"9\u0001\u0000\u0000\u0000\u01ae\u01af\u0006\u001d\uffff\uffff\u0000\u01af"+
		"\u01b0\u0003<\u001e\u0000\u01b0\u01b1\u0006\u001d\uffff\uffff\u0000\u01b1"+
		"\u01c7\u0001\u0000\u0000\u0000\u01b2\u01b3\n\u0004\u0000\u0000\u01b3\u01b4"+
		"\u0005\u001c\u0000\u0000\u01b4\u01b5\u0003,\u0016\u0000\u01b5\u01b6\u0005"+
		"\u001d\u0000\u0000\u01b6\u01b7\u0006\u001d\uffff\uffff\u0000\u01b7\u01c6"+
		"\u0001\u0000\u0000\u0000\u01b8\u01b9\n\u0003\u0000\u0000\u01b9\u01ba\u0005"+
		"\u0013\u0000\u0000\u01ba\u01bb\u00052\u0000\u0000\u01bb\u01c6\u0006\u001d"+
		"\uffff\uffff\u0000\u01bc\u01bd\n\u0002\u0000\u0000\u01bd\u01be\u0005\u0014"+
		"\u0000\u0000\u01be\u01c6\u0006\u001d\uffff\uffff\u0000\u01bf\u01c0\n\u0001"+
		"\u0000\u0000\u01c0\u01c1\u0005\u001a\u0000\u0000\u01c1\u01c2\u0003>\u001f"+
		"\u0000\u01c2\u01c3\u0005\u001b\u0000\u0000\u01c3\u01c4\u0006\u001d\uffff"+
		"\uffff\u0000\u01c4\u01c6\u0001\u0000\u0000\u0000\u01c5\u01b2\u0001\u0000"+
		"\u0000\u0000\u01c5\u01b8\u0001\u0000\u0000\u0000\u01c5\u01bc\u0001\u0000"+
		"\u0000\u0000\u01c5\u01bf\u0001\u0000\u0000\u0000\u01c6\u01c9\u0001\u0000"+
		"\u0000\u0000\u01c7\u01c5\u0001\u0000\u0000\u0000\u01c7\u01c8\u0001\u0000"+
		"\u0000\u0000\u01c8;\u0001\u0000\u0000\u0000\u01c9\u01c7\u0001\u0000\u0000"+
		"\u0000\u01ca\u01cb\u0005\u0002\u0000\u0000\u01cb\u01e9\u0006\u001e\uffff"+
		"\uffff\u0000\u01cc\u01cd\u0005\u0003\u0000\u0000\u01cd\u01e9\u0006\u001e"+
		"\uffff\uffff\u0000\u01ce\u01cf\u0005\u0004\u0000\u0000\u01cf\u01e9\u0006"+
		"\u001e\uffff\uffff\u0000\u01d0\u01d1\u0005-\u0000\u0000\u01d1\u01e9\u0006"+
		"\u001e\uffff\uffff\u0000\u01d2\u01d3\u0005#\u0000\u0000\u01d3\u01e9\u0006"+
		"\u001e\uffff\uffff\u0000\u01d4\u01d5\u0005)\u0000\u0000\u01d5\u01e9\u0006"+
		"\u001e\uffff\uffff\u0000\u01d6\u01d7\u0005+\u0000\u0000\u01d7\u01d8\u0003"+
		"&\u0013\u0000\u01d8\u01d9\u0006\u001e\uffff\uffff\u0000\u01d9\u01e9\u0001"+
		"\u0000\u0000\u0000\u01da\u01db\u0005\u0018\u0000\u0000\u01db\u01dc\u0003"+
		",\u0016\u0000\u01dc\u01dd\u0005\u0016\u0000\u0000\u01dd\u01de\u0003&\u0013"+
		"\u0000\u01de\u01df\u0005\u0019\u0000\u0000\u01df\u01e0\u0006\u001e\uffff"+
		"\uffff\u0000\u01e0\u01e9\u0001\u0000\u0000\u0000\u01e1\u01e2\u0005\u001a"+
		"\u0000\u0000\u01e2\u01e3\u0003,\u0016\u0000\u01e3\u01e4\u0005\u001b\u0000"+
		"\u0000\u01e4\u01e5\u0006\u001e\uffff\uffff\u0000\u01e5\u01e9\u0001\u0000"+
		"\u0000\u0000\u01e6\u01e7\u00052\u0000\u0000\u01e7\u01e9\u0006\u001e\uffff"+
		"\uffff\u0000\u01e8\u01ca\u0001\u0000\u0000\u0000\u01e8\u01cc\u0001\u0000"+
		"\u0000\u0000\u01e8\u01ce\u0001\u0000\u0000\u0000\u01e8\u01d0\u0001\u0000"+
		"\u0000\u0000\u01e8\u01d2\u0001\u0000\u0000\u0000\u01e8\u01d4\u0001\u0000"+
		"\u0000\u0000\u01e8\u01d6\u0001\u0000\u0000\u0000\u01e8\u01da\u0001\u0000"+
		"\u0000\u0000\u01e8\u01e1\u0001\u0000\u0000\u0000\u01e8\u01e6\u0001\u0000"+
		"\u0000\u0000\u01e9=\u0001\u0000\u0000\u0000\u01ea\u01ef\u0006\u001f\uffff"+
		"\uffff\u0000\u01eb\u01ec\u0003@ \u0000\u01ec\u01ed\u0006\u001f\uffff\uffff"+
		"\u0000\u01ed\u01ef\u0001\u0000\u0000\u0000\u01ee\u01ea\u0001\u0000\u0000"+
		"\u0000\u01ee\u01eb\u0001\u0000\u0000\u0000\u01ef?\u0001\u0000\u0000\u0000"+
		"\u01f0\u01f1\u0003,\u0016\u0000\u01f1\u01f8\u0006 \uffff\uffff\u0000\u01f2"+
		"\u01f3\u0005\u0017\u0000\u0000\u01f3\u01f4\u0003,\u0016\u0000\u01f4\u01f5"+
		"\u0006 \uffff\uffff\u0000\u01f5\u01f7\u0001\u0000\u0000\u0000\u01f6\u01f2"+
		"\u0001\u0000\u0000\u0000\u01f7\u01fa\u0001\u0000\u0000\u0000\u01f8\u01f6"+
		"\u0001\u0000\u0000\u0000\u01f8\u01f9\u0001\u0000\u0000\u0000\u01f9A\u0001"+
		"\u0000\u0000\u0000\u01fa\u01f8\u0001\u0000\u0000\u0000\u0019P_\u0085\u008f"+
		"\u009f\u00ab\u00b5\u00d0\u00ed\u0119\u0124\u0127\u013a\u0148\u016c\u017c"+
		"\u017e\u0194\u0196\u01ac\u01c5\u01c7\u01e8\u01ee\u01f8";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
lexer grammar Prev25Lexer;

@header {
    package compiler.phase.lexan;
    import compiler.common.report.*;
}

@members {
    @Override
    public void notifyListeners(LexerNoViableAltException e) {
        int start = _tokenStartCharIndex+1;
        int stop = _input.index()+1;
        String tokenText = _input.getText(Interval.of(start, stop));

        String location = "line " + getLine() + ":" + getCharPositionInLine();
        String msg = "token recognition error at: '" + tokenText + "'";

        throw new Report.Error(location + " " + msg); 
    }
}


// Whitespace
WHITESPACE   : [ \t\n\r]+ -> skip ;

//  Constants
INTEGER_CONSTANT: [0-9]+ ;
CHAR_CONSTANT   : '\'' ( ~['\\] | '\\' [\\'] | '\\0x' [0-9A-Fa-f] [0-9A-Fa-f] ) '\'' ;
STRING_CONSTANT : '"' ( ~["\\] | '\\' [\\"] | '\\0x' [0-9A-Fa-f][0-9A-Fa-f] )+ '"' ;


// signs
AMPERSAND       : '&' ;
PIPE            : '|' ;
EQUAL           : '==' ;
NOT_EQUAL       : '!=' ;
LESS            : '<' ;
GREATER         : '>' ;
LESS_EQUAL      : '<=' ;
GREATER_EQUAL   : '>=' ;
MULTIPLY        : '*' ;
DIVIDE          : '/' ;
MODULO          : '%' ;
PLUS            : '+' ;
MINUS           : '-' ;
EXCLAMATION     : '!' ;
DOT             : '.' ;
CARET           : '^' ;
ASSIGN          : '=' ;
COLON           : ':' ;
COMMA           : ',' ;
LBRACE          : '{' ;
RBRACE          : '}' ;
LPAREN          : '(' ;
RPAREN          : ')' ;
LBRACKET        : '[' ;
RBRACKET        : ']' ;

// Keywords
BOOL        : 'bool' ;
CHAR        : 'char' ;
DO          : 'do' ;
ELSE        : 'else' ;
END         : 'end' ;
FALSE       : 'false' ;
FUN         : 'fun' ;
IF          : 'if' ;
INT         : 'int' ;
IN          : 'in' ;
LET         : 'let' ;
NULL        : 'null' ;
RETURN      : 'return' ;
SIZEOF      : 'sizeof' ;
THEN        : 'then' ;
TRUE        : 'true' ;
TYP         : 'typ' ;
VAR         : 'var' ;
VOID        : 'void' ;
WHILE       : 'while' ;

// Identifiers 
ID : [a-zA-Z_][a-zA-Z_0-9]* ;

// COMMENT : '#' .*? '#' -> skip; not gridy or something !!
COMMENT : '#' ~[\r\n]* -> skip ;
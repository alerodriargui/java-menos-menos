import java_cup.runtime.*;
import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;
import java.util.ArrayList;

      
%%

%class Lexer

%line
%column

%cup

%{  

public ArrayList<ComplexSymbol> tokens = new ArrayList<>();

    /*
     * Create ComplexSymbol without attribute
     */
    private ComplexSymbol symbol(int type) {
        ComplexSymbol cs = new ComplexSymbol(sym.terminalNames[type], type);
        cs.left = yyline + 1;
        cs.right = yycolumn;
        tokens.add(cs);
        return cs;
    }

    /*
     * Create ComplexSymbol with attribute
     */
    private ComplexSymbol symbol(int type, Object value) {
        ComplexSymbol cs = new ComplexSymbol(sym.terminalNames[type], type, value);
        cs.left = yyline + 1;
        cs.right = yycolumn;
        tokens.add(cs);
        return cs;
    }

%}
   

LineTerminator = \r|\n|\r\n
WhiteSpace     = {LineTerminator} | [ \t\f]

NUM = [0-9]+
IDENT = [A-Za-z_][A-Za-z_0-9]*
STRING = \"([^\\\"]|\\.)*\"

%%
   
<YYINITIAL> {

    /** Keywords. */
    "and"             { return symbol(sym.AND); }
    "or"              { return symbol(sym.OR); }
    "not"             { return symbol(sym.NOT); }
    "true"            { return symbol(sym.TRUE); }
    "false"           { return symbol(sym.FALSE); }
    "suptrue"         { return symbol(sym.SUPTRUE); }
    "supfalse"        { return symbol(sym.SUPFALSE); }

    "begin"           { return symbol(sym.BEGIN); }
    "end"             { return symbol(sym.END); }
    "exit"            { return symbol(sym.EXIT); }
    "if"              { return symbol(sym.IF); }
    "then"            { return symbol(sym.THEN); }
    "else"            { return symbol(sym.ELSE); }
    "while"           { return symbol(sym.WHILE); }
    "do"              { return symbol(sym.DO); }

    "print"           { return symbol(sym.PRINT); }
    "readint"         { return symbol(sym.READINT); }
    "length"          { return symbol(sym.LENGTH); }
    "position"        { return symbol(sym.POSITION); }
    "readstr"         { return symbol(sym.READSTR); }
    "concatenate"     { return symbol(sym.CONCATENATE); }
    "substring"       { return symbol(sym.SUBSTRING); }

    ":="              {return symbol(sym.ASSIGN); }
    "="               { return symbol(sym.EQ); }
    "<"               { return symbol(sym.LT); }
    "<="              { return symbol(sym.LE); }
    ">"               { return symbol(sym.GT); }
    ">="              { return symbol(sym.GE); }
    "<>"              { return symbol(sym.NE); }

    "=="              { return symbol(sym.STREQ); }
    "!="              { return symbol(sym.STRNOTEQ); }

    ";"                { return symbol(sym.SEMI); }
    ","                { return symbol(sym.COMMA); }
    "("                { return symbol(sym.LPAREN); }
    ")"                { return symbol(sym.RPAREN); }
    "+"                { return symbol(sym.PLUS); }
    "-"                { return symbol(sym.MINUS); }
    "*"                { return symbol(sym.TIMES); }
    "%"                { return symbol(sym.MODE);  }
    "/"                { return symbol(sym.DIVIDE); }

    "++"               { return symbol(sym.INCREMENT); }   
    "--"               { return symbol(sym.DECREMENT); }
    
    {NUM}      { return symbol(sym.NUM, new Integer(yytext())); }
    {IDENT}       { return symbol(sym.IDENT, new String(yytext()));}
    {STRING}      { return symbol(sym.STRING, new String(yytext())); }

    {WhiteSpace}       { /* do nothing */ }   
    <<EOF>> { return symbol(sym.EOF); }
}


/* error */ 
[^]                    { throw new Error("Illegal character <"+yytext()+">"); }
%{
import java_cup.runtime.Symbol;
%}

%%
%%

// Definiciones de tokens
[0-9]+      { return new Symbol(sym.NUM, Integer.parseInt(yytext())); }
"+"        { return new Symbol(sym.PLUS); }
"-"        { return new Symbol(sym.MINUS); }
"*"        { return new Symbol(sym.TIMES); }
"/"        { return new Symbol(sym.DIVIDE); }
[ \t\n]    { /* Ignorar espacios en blanco */ }
.          { System.err.println("Unrecognized character: " + yytext()); }

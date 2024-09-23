
import java_cup.runtime.Symbol;
import java_cup.runtime.Scanner;


%%



%%
[0-9]+    { return new Symbol(sym.NUM, new Integer(yytext())); }
[ \t\n]   { /* ignorar espacios en blanco */ }
"+"       { return new Symbol(sym.PLUS); }
"-"       { return new Symbol(sym.MINUS); }
"*"       { return new Symbol(sym.TIMES); }
"/"       { return new Symbol(sym.DIVIDE); }
.         { System.err.println("Carácter no reconocido: " + yytext()); }

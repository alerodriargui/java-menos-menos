import java.io.FileReader;
import java_cup.runtime.Symbol;

public class Main {
    public static void main(String[] args) {
        try {
            Yylex lexer = new Yylex(new FileReader(args[0]));
            parser p = new parser(lexer);
            p.parse(); // o cualquier otro método para iniciar el parseo
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

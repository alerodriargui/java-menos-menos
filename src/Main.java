import java.io.FileReader;

import java_cup.runtime.Symbol;

public class Main {
    public static void main(String[] args) throws Exception {
        Lexer lexer = new Lexer(new FileReader(args[0]));
        parser p = new parser(lexer);
        Symbol result = p.parse();
        System.out.println("Resultado: " + result.value);
    }
}

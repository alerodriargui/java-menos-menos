public class instruction3Address {

    private String op;     // Operador (e.g., +, -, =)
    private String arg1;   // Primer argumento
    private String arg2;   // Segundo argumento (puede ser null)
    private String result; // Resultado de la operación

    public instruction3Address(String op, String arg1, String arg2, String result) {
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
    }

    @Override
    public String toString() {
        if (arg2 == null) {
            return result + " = " + op + " " + arg1;
        } else {
            return result + " = " + arg1 + " " + op + " " + arg2;
        }
    }
}

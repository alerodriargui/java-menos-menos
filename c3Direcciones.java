import java.util.ArrayList;

public class c3Direcciones {

    private ArrayList<instruction3Address> instructions = new ArrayList<>();
    private int tempCounter = 0; // Contador para variables temporales

    // Método para generar un nuevo nombre de variable temporal
    public String newTemp() {
        return "t" + (tempCounter++);
    }

    // Método para añadir una instrucción
    public void addInstruction(String op, String arg1, String arg2, String result) {
        instructions.add(new instruction3Address(op, arg1, arg2, result));
    }

    // Método para imprimir todas las instrucciones
    public void printInstructions() {
        for (instruction3Address instr : instructions) {
            System.out.println(instr);
        }

    }
}

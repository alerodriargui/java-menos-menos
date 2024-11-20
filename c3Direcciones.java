import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class c3Direcciones {

    private static String PATH = "output\\c3d.txt";
    private ArrayList<instruction3Address> instructions;
    private int tempCounter; // Contador para variables temporales

    public c3Direcciones() {
        instructions = new ArrayList<>();
        tempCounter = 0;
    }

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
        String result = "-----------------------------------------------\n"
                + "---------------- C3@ code list"
                + " ----------------\n"
                + "-----------------------------------------------\n";
        for (int i = 0; i < instructions.size(); i++) {
            result += instructions.get(i) + "\n";
        }
        try {
            // File Writter
            File file;
            file = new File(PATH);
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(result);
            writer.close();
        } catch (IOException ex) {
            System.out.println("ERROR WRITING C3@");
        }
    }
}

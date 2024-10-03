package core.all;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class SymbolTable {
    private HashMap<String, Object> symbols = new HashMap<>();

    public void put(String name, Object value) {
        symbols.put(name, value);
    }

    public Object get(String name) {
        return symbols.get(name);
    }

    public void showSymbolTable() {
        System.out.println("Tabla de símbolos:");
        for (String key : symbols.keySet()) {
            System.out.println(key + ": " + symbols.get(key));
        }
    }

    public void writeToFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String key : symbols.keySet()) {
                writer.write(key + ": " + symbols.get(key));
                writer.newLine(); // Añade una nueva línea después de cada entrada
            }
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }
}

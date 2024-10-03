package core.symbol_table;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class SymbolTable {
    private HashMap<String, Symbol> symbols = new HashMap<>();

    // Método para añadir un símbolo completo
    public void put(Symbol symbol) {
        symbols.put(symbol.getName(), symbol);
    }

    // Sobrecarga para facilidad de uso al añadir símbolos
    public void put(String name, Object value, SymbolType type) {
        Symbol symbol = new Symbol(name, value, type);
        put(symbol);
    }

    // Obtener el símbolo completo
    public Symbol get(String name) {
        return symbols.get(name);
    }

    // Mostrar la tabla de símbolos
    public void showSymbolTable() {
        System.out.println("Tabla de símbolos:");
        for (String key : symbols.keySet()) {
            Symbol symbol = symbols.get(key);
            System.out.println(key + " (" + symbol.getType() + "): " + symbol.getValue());
        }
    }

    // Escribir la tabla de símbolos en un archivo
    public void writeToFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String key : symbols.keySet()) {
                Symbol symbol = symbols.get(key);
                writer.write(key + " (" + symbol.getType() + "): " + symbol.getValue());
                writer.newLine(); // Añade una nueva línea después de cada entrada
            }
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }
}

package core.symbol;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class SymbolTable {
    private static SymbolTable instance = null;

    private HashMap<String, Symbol> symbols = new HashMap<>();
    private SymbolTable() {}  // Constructor privado para evitar instanciación externa

    public static SymbolTable getInstance() {
        if (instance == null) {
            instance = new SymbolTable();
        }
        return instance;
    }
    // Método para añadir un símbolo completo
    public void put(Symbol symbol) {
        // Check if the symbol already exists and is a constant
        Symbol existingSymbol = symbols.get(symbol.getName());
        //System.out.println("Añadiendo símbolo: " + symbol.getName() + " (" + symbol.getType() + "): " + symbol.getValue() + " (constante: " + symbol.isConstant() + ")");
        if (existingSymbol != null && existingSymbol.isConstant()) {
            throw new IllegalArgumentException("Cannot reassign a constant: " + symbol.getName());
        }
        if(existingSymbol != null){
            //System.out.println("Modificando el valor de " + symbol.getName() + " de " + existingSymbol.getValue() + " a " + symbol.getValue());
        }
        symbols.put(symbol.getName(), symbol);
    }

    // Sobrecarga para facilidad de uso al añadir símbolos
    public void put(String name, Object value, SymbolType type, boolean isConstant, List<String> params) {
        Symbol symbol = new Symbol(name, value, type, isConstant, params);
        if(isConstant){
            //System.out.println("Constante: " + name + " = " + value);
        }
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

    public void setValue(String name, Object value) {
        Symbol symbol = symbols.get(name);
        if (symbol != null) {
            symbol.setValue(value);
        } else {
            throw new IllegalArgumentException("Symbol not found: " + name);
        }
    }

    // Devuelve una instancia de SymbolTable
    public HashMap<String, Symbol> getSymbols() {
        return this.symbols;
    }
}

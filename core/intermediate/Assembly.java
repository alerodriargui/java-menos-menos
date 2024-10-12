package core.intermediate;

import core.symbol.SymbolTable;

public class Assembly {
    private TresDirCode threeAddressCode;
    private SymbolTable symbolTable;

    public Assembly(TresDirCode threeAddressCode, SymbolTable symbolTable) {
        this.threeAddressCode = threeAddressCode;
        this.symbolTable = symbolTable;
    }

    public void generateAssembly() {
        // Lógica para convertir el código de tres direcciones en ensamblador
        for (String instruction : threeAddressCode.getInstructions()) {
            // Aquí puedes transformar cada instrucción en ensamblador
            System.out.println("Translating to assembly: " + instruction);
        }
    }
}
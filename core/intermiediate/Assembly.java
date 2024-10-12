package core.intermediate;

public class Assembly {
    private ThreeAddressCode threeAddressCode;
    private SymbolTable symbolTable;

    public Assembly(ThreeAddressCode threeAddressCode, SymbolTable symbolTable) {
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

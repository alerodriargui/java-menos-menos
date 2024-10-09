package core.symbol;

public class Symbol {
    private String name;
    private Object value;
    private SymbolType type;

    public Symbol(String name, Object value, SymbolType type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public SymbolType getType() {
        return type;
    }

    @Override
    public String toString() {
        return value.toString();
    }

}

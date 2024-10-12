package core.symbol;

public class Symbol {
    private String name;
    private Object value;
    private SymbolType type;
    private boolean isConstant;

    public Symbol(String name, Object value, SymbolType type, boolean isConstant) {
        this.name = name;
        this.value = value;
        this.type = type;
        this.isConstant = isConstant;
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

    public boolean isConstant() {
        return isConstant;
    }

    public void setValue(Object value) {
        if (isConstant) {
            throw new UnsupportedOperationException("Cannot modify the value of a constant.");
        }
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

}

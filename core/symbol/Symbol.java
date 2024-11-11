package core.symbol;

import java.util.List;

public class Symbol {
    private String name;
    private Object value;
    private SymbolType type;
    private boolean isConstant;
    private List<String> params;   // Parámetros de la función (si es una función)


    public Symbol(String name, Object value, SymbolType type, boolean isConstant, List<String> params) {
        this.name = name;
        this.value = value;
        this.type = type;
        this.isConstant = isConstant;
        this.params = params;
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
        //System.out.println("Modificando el valor de " + name + " de " + this.value + " a " + value);
        if (isConstant) {
            throw new UnsupportedOperationException("Cannot modify the value of a constant.");
        }
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

}

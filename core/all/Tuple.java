package core.all;

import java.util.Arrays;
import java.util.List;

public class Tuple {
    private List<Object> elements;

    public Tuple(Object... elements) {
        this.elements = Arrays.asList(elements); // Acepta un número variable de argumentos
    }

    public Object get(int index) {
        return elements.get(index);
    }

    public int size() {
        return elements.size();
    }

    @Override
    public String toString() {
        return "(" + String.join(", ", elements.stream().map(Object::toString).toArray(String[]::new)) + ")";
    }
}
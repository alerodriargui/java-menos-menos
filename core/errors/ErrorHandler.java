package core.errors;

import java.util.ArrayList;
import java.util.List;

public class ErrorHandler {
    private List<Error> errors;

    public ErrorHandler() {
        errors = new ArrayList<Error>();
    }

    public void addError(String message, int line, int column) {
        errors.add(new Error(message, line, column));
    }

    public void printErrors() {
        for (Error error : errors) {
            System.err.println(error);
        }
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}

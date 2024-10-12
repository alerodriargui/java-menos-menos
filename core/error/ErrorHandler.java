package core.error;

public class ErrorHandler {
    private List<Error> errors;

    public ErrorHandler() {
        errors = new ArrayList<>();
    }

    public void addError(String message, int line, int column) {
        errors.add(new Error(message, line, column));
    }

    public void printErrors() {
        for (Error error : errors) {
            System.out.println(error);
        }
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}

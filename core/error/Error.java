package core.error;

public class Error {
    private String message;
    private int line;
    private int column;

    public Error(String message, int line, int column) {
        this.message = message;
        this.line = line;
        this.column = column;
    }

    public String getMessage() {
        return message;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return "Error{" +
                "message='" + message + '\'' +
                ", line=" + line +
                ", column=" + column +
                '}';
    }
}

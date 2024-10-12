package core.intermediate;

import java.util.ArrayList;
import java.util.List;

public class TresDirCode {
    private List<String> instructions;

    public TresDirCode() {
        this.instructions = new ArrayList<>();
    }

    public void addInstruction(String instruction) {
        instructions.add(instruction);
    }

    public List<String> getInstructions() {
        return instructions;
    }

    @Override
    public String toString() {
        return String.join("\n", instructions);
    }
}

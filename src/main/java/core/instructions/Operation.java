package main.java.core.instructions;

public enum Operation {
    ADD("ADD", InstructionType.R),
    SUB("SUB", InstructionType.R),
    DIV("DIV", InstructionType.R),
    MUL("MUL", InstructionType.R);

    public final String representation;
    public final InstructionType instructionType;

    private Operation(String representation, InstructionType instructionType) {
        this.representation = representation;
        this.instructionType = instructionType;
    }
}

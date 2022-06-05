package main.java.instructions;

public enum Operation {
    ADD("ADD", InstructionType.R),
    SUB("SUB", InstructionType.R),
    MUL("MUL", InstructionType.R),
    DIV("DIV", InstructionType.R);

    public final String representation;
    public final InstructionType relatedInstructionType;

    private Operation(
        String representation, 
        InstructionType relatedInstructionType
    ) {
        this.representation = representation;
        this.relatedInstructionType = relatedInstructionType;
    }
}
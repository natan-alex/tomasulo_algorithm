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

    public boolean isAddOrSub() {
        return this == ADD || this == SUB;
    }

    public boolean isMulOrDiv() {
        return this == MUL || this == DIV;
    }
}
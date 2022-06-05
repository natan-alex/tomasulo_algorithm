package main.java.instructions;

import java.util.Objects;

import main.java.components.registers.FPRegister;

public class RTypeInstruction extends Instruction {
    public final Operation operation;
    public final FPRegister destination;
    public final FPRegister firstOperand;
    public final FPRegister secondOperand;

    public RTypeInstruction(
        Operation operation,
        FPRegister destination, 
        FPRegister firstOperand,
        FPRegister secondOperand
    ) {
        super(InstructionType.R);

        this.operation = requireRTypeInstructionRelatedOperation(operation);
        this.destination = Objects.requireNonNull(destination);
        this.firstOperand = Objects.requireNonNull(firstOperand);
        this.secondOperand = Objects.requireNonNull(secondOperand);
    }

    private static Operation requireRTypeInstructionRelatedOperation(Operation operation) {
        if (operation.relatedInstructionType != InstructionType.R) {
            throw new IllegalArgumentException("Only R type instruction related operations allowed.");
        }

        return operation;
    }
}

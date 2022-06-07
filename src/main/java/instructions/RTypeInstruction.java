package main.java.instructions;

import java.util.Objects;

import main.java.components.registers.FPRegister;

public class RTypeInstruction {
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
        this.operation = operation;
        this.destination = Objects.requireNonNull(destination);
        this.firstOperand = Objects.requireNonNull(firstOperand);
        this.secondOperand = Objects.requireNonNull(secondOperand);
    }

    @Override
    public String toString() {
        return new StringBuilder()
            .append(operation.representation)
            .append(" ")
            .append(destination.name)
            .append(" ")
            .append(firstOperand.name)
            .append(" ")
            .append(secondOperand.name)
            .toString();
    }
}

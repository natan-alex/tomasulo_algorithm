package main.java.instructions;

import java.util.Objects;

import main.java.components.registers.FPRegister;

public class RTypeInstruction {
    private final Operation operation;
    private final FPRegister destination;
    private final FPRegister firstOperand;
    private final FPRegister secondOperand;

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

    public Operation getOperation() {
        return operation;
    }

    public FPRegister getDestination() {
        return destination;
    }

    public FPRegister getSecondOperand() {
        return secondOperand;
    }

    public FPRegister getFirstOperand() {
        return firstOperand;
    }

    @Override
    public String toString() {
        return new StringBuilder()
            .append(operation.representation)
            .append(" ")
            .append(destination.getName())
            .append(" ")
            .append(firstOperand.getName())
            .append(" ")
            .append(secondOperand.getName())
            .toString();
    }
}
package main.java.instructions;

import java.util.Objects;

import main.java.components.registers.FPRegister;

public class RTypeInstruction extends BaseInstruction {
    private final FPRegister destinationRegister;
    private final FPRegister firstOperandRegister;
    private final FPRegister secondOperandRegister;

    public RTypeInstruction(
            Operation operation,
            FPRegister destinationRegister,
            FPRegister firstOperandRegister,
            FPRegister secondOperandRegister) {
        super(operation);

        if (operation == null || operation.isLoadOrStore()) {
            throw new IllegalArgumentException("Invalid operation for R type instruction");
        }

        this.destinationRegister = Objects.requireNonNull(destinationRegister);
        this.firstOperandRegister = Objects.requireNonNull(firstOperandRegister);
        this.secondOperandRegister = Objects.requireNonNull(secondOperandRegister);
    }

    public Operation getOperation() {
        return operation;
    }

    public FPRegister getDestinationRegister() {
        return destinationRegister;
    }

    public FPRegister getFirstOperandRegister() {
        return firstOperandRegister;
    }

    public FPRegister getSecondOperandRegister() {
        return secondOperandRegister;
    }

    @Override
    public String toString() {
        return operation + " " +
                destinationRegister.getName() + " " +
                firstOperandRegister.getName() + " " +
                secondOperandRegister.getName();
    }

}
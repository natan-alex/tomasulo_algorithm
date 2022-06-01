package main.java.core;

import java.util.Objects;

import main.java.events.Observable;

public class RTypeInstruction extends Observable {
    public final Operation operation;
    public final FPRegister destinationRegister;
    public final FPRegister firstOperand;
    public final FPRegister secondOperand;

    public RTypeInstruction(
        Operation operation,
        FPRegister destinationRegister, 
        FPRegister firstOperand,
        FPRegister secondOperand
    ) {
        this.operation = operation;
        this.destinationRegister = Objects.requireNonNull(destinationRegister);
        this.firstOperand = Objects.requireNonNull(firstOperand);
        this.secondOperand = Objects.requireNonNull(secondOperand);
    }

    @Override
    protected void taskCompleted() {
        System.out.println("completed!");
    }
}
package main.java.core.instructions;

import java.util.Objects;

import main.java.core.components.FPRegister;
import main.java.events.Observable;

public class RTypeInstruction extends Observable {
    public final RTypeInstruction.Op operation;
    public final FPRegister destinationRegister;
    public final FPRegister firstOperand;
    public final FPRegister secondOperand;

    public RTypeInstruction(
        RTypeInstruction.Op operation,
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

    public static enum Op {
        ADD("ADD"),
        SUB("SUB"),
        DIV("DIV"),
        MUL("MUL");

        public final String representation;

        private Op(String representation) {
            this.representation = representation;
        }
    }
}
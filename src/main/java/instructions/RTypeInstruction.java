package main.java.instructions;

import java.util.Objects;

import main.java.components.registers.FPRegister;

public class RTypeInstruction {
    public final Operation operation;
    public final FPRegister destination;
    public final FPRegister firstOperand;
    public final FPRegister secondOperand;

    public static enum Operation {
        ADD("ADD"),
        SUB("SUB");

        public final String representation;

        private Operation(String representation) {
            this.representation = representation;
        }
    }

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
}

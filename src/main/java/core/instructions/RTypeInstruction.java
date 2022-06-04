package main.java.core.instructions;

import java.util.Objects;

import main.java.core.components.FPRegister;
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
    ) throws Exception {
        this.operation = requireRTypeInstructionOperation(operation);
        this.destinationRegister = Objects.requireNonNull(destinationRegister);
        this.firstOperand = Objects.requireNonNull(firstOperand);
        this.secondOperand = Objects.requireNonNull(secondOperand);
    }

    private static Operation requireRTypeInstructionOperation(Operation op) throws Exception {
        if (op.instructionType != InstructionType.R) {
            throw new Exception("Invalid operation: should be one operation that matches R type instructions");
        }

        return op;
    }

    @Override
    protected void taskCompleted() {
        System.out.println("completed!");
    }
}
package main.java.instructions;

import java.util.Objects;

import main.java.components.registers.FPRegister;

public class RTypeInstruction extends Instruction {
    public final FPRegister destination;
    public final FPRegister firstOperand;
    public final FPRegister secondOperand;

    public RTypeInstruction(
        Operation operation,
        FPRegister destination, 
        FPRegister firstOperand,
        FPRegister secondOperand
    ) {
        super(InstructionType.R, operation);

        this.destination = Objects.requireNonNull(destination);
        this.firstOperand = Objects.requireNonNull(firstOperand);
        this.secondOperand = Objects.requireNonNull(secondOperand);
    }

    @Override
    public String toStringRepresentation() {
        var builder = new StringBuilder();

        builder
            .append(operation.representation)
            .append(" ")
            .append(destination.name)
            .append(" ")
            .append(firstOperand.name)
            .append(" ")
            .append(secondOperand.name);

        return builder.toString();
    }
}

package main.java.instructions;

import java.util.Objects;

public abstract class BaseInstruction {
    protected final Operation operation;

    public BaseInstruction(Operation operation) {
        this.operation = Objects.requireNonNull(operation);
    }

    public Operation getOperation() {
        return operation;
    }
}

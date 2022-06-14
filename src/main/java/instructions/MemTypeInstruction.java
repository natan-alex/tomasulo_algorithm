package main.java.instructions;

import java.util.Objects;

import main.java.components.registers.AddressRegister;

public class MemTypeInstruction {
    private final Operation operation;
    private final int offset;
    private final AddressRegister baseRegister;

    public MemTypeInstruction(
            Operation operation,
            int offset,
            AddressRegister baseRegister) {
        if (operation == null || !operation.isLoadOrStore()) {
            throw new IllegalArgumentException("Invalid operation for MEM type instruction");
        }

        if (offset < 0) {
            throw new IllegalArgumentException("Offset cannot be negative");
        }

        this.baseRegister = Objects.requireNonNull(baseRegister);
        this.operation = operation;
        this.offset = offset;
    }

    public Operation getOperation() {
        return operation;
    }

    public int getOffset() {
        return offset;
    }

    public AddressRegister getBaseRegister() {
        return baseRegister;
    }

    @Override
    public String toString() {
        return operation + " " +
                offset + " + " +
                baseRegister.getName();
    }

}

package main.java.instructions;

import java.util.Objects;

import main.java.components.registers.AddressRegister;
import main.java.components.registers.FPRegister;

public class MemoryTypeInstruction extends BaseInstruction {
    private final int offset;
    private final AddressRegister baseRegister;
    private final FPRegister destinationRegister;

    public MemoryTypeInstruction(
            Operation operation,
            FPRegister destinationRegister,
            int offset,
            AddressRegister baseRegister) {
        super(operation);

        if (operation == null || !operation.isLoadOrStore()) {
            throw new IllegalArgumentException("Invalid operation for MEM type instruction");
        }

        if (offset < 0) {
            throw new IllegalArgumentException("Offset cannot be negative");
        }

        this.destinationRegister = Objects.requireNonNull(destinationRegister);
        this.baseRegister = Objects.requireNonNull(baseRegister);
        this.offset = offset;
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
                destinationRegister.getName() + " " +
                offset + " + " +
                baseRegister.getName();
    }

    public FPRegister getDestinationRegister() {
        return destinationRegister;
    }
}

package main.java.components.units;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import main.java.instructions.MemoryTypeInstruction;
import main.java.instructions.Operation;

public class MemoryInstructionAndControlInfos {
    private final CountDownLatch countDownLatch;
    private final String baseRegisterName;
    private final int offset;
    private final String destinationRegisterName;
    private final Operation operation;

    public MemoryInstructionAndControlInfos(MemoryTypeInstruction instruction, CountDownLatch countDownLatch) {
        Objects.requireNonNull(instruction);

        this.offset = instruction.getOffset();
        this.operation = instruction.getOperation();
        this.baseRegisterName = instruction.getBaseRegister().getName();
        this.countDownLatch = Objects.requireNonNull(countDownLatch);
        this.destinationRegisterName = Objects.requireNonNull(instruction.getDestinationRegister().getName());
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public String getBaseRegisterName() {
        return baseRegisterName;
    }

    public int getOffset() {
        return offset;
    }

    public String getDestinationRegisterName() {
        return destinationRegisterName;
    }

    public Operation getOperation() {
        return operation;
    }
}

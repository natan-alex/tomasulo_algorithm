package main.java.components.units;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import main.java.instructions.Operation;

public class MemoryUnitBroadcastInfos {
    private final Operation operation;
    private final String destinationRegisterName;
    private final Optional<String> destinationRegisterNewName;
    private Optional<Double> destinationRegisterValue;
    private final String originBufferName;
    private final CountDownLatch countDownLatch;
    private final int address;

    public MemoryUnitBroadcastInfos(
            int address,
            Operation operation,
            String originBufferName,
            String destinationRegisterName,
            Optional<Double> destinationRegisterValue,
            Optional<String> destinationRegisterNewName,
            CountDownLatch countDownLatch) {
        this.address = address;
        this.operation = Objects.requireNonNull(operation);
        this.originBufferName = Objects.requireNonNull(originBufferName);
        this.destinationRegisterName = Objects.requireNonNull(destinationRegisterName);
        this.countDownLatch = Objects.requireNonNull(countDownLatch);
        this.destinationRegisterValue = Objects.requireNonNull(destinationRegisterValue);
        this.destinationRegisterNewName = Objects.requireNonNull(destinationRegisterNewName);
    }

    public String getDestinationRegisterName() {
        return destinationRegisterName;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public int getAddress() {
        return address;
    }

    public Operation getOperation() {
        return operation;
    }

    public Optional<String> getDestinationRegisterNewName() {
        return destinationRegisterNewName;
    }

    public String getOriginBufferName() {
        return originBufferName;
    }

    public Optional<Double> getDestinationRegisterValue() {
        return destinationRegisterValue;
    }

    public void setDestinationRegisterValue(double destinationRegisterValue) {
        this.destinationRegisterValue = Optional.of(destinationRegisterValue);
    }

}

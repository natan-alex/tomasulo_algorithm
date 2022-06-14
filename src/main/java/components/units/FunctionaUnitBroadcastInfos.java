package main.java.components.units;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import main.java.instructions.Operation;

public class FunctionaUnitBroadcastInfos {
    private final String originStationName;
    private final Operation operation;
    private final double firstOperandValue;
    private final double secondOperandValue;
    private final String destinationRegisterName;
    private final CountDownLatch countDownLatch;
    private final String firstOperandName;
    private final String secondOperandName;

    public FunctionaUnitBroadcastInfos(
            String originStationName,
            Operation operation,
            String destinationRegisterName,
            String firstOperandName,
            double firstOperandValue,
            String secondOperandName,
            double secondOperandValue,
            CountDownLatch countDownLatch) {
        this.originStationName = Objects.requireNonNull(originStationName);
        this.operation = Objects.requireNonNull(operation);
        this.firstOperandValue = firstOperandValue;
        this.secondOperandValue = secondOperandValue;
        this.destinationRegisterName = Objects.requireNonNull(destinationRegisterName);
        this.countDownLatch = Objects.requireNonNull(countDownLatch);
        this.firstOperandName = Objects.requireNonNull(firstOperandName);
        this.secondOperandName = Objects.requireNonNull(secondOperandName);
    }

    public String getOriginStationName() {
        return originStationName;
    }

    public double getFirstOperandValue() {
        return firstOperandValue;
    }

    public double getSecondOperandValue() {
        return secondOperandValue;
    }

    public Operation getOperation() {
        return operation;
    }

    public String getDestinationRegisterName() {
        return destinationRegisterName;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public String getFirstOperandName() {
        return firstOperandName;
    }

    public String getSecondOperandName() {
        return secondOperandName;
    }
}

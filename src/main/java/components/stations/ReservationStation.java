package main.java.components.stations;

import java.util.Objects;
import java.util.Optional;

import main.java.instructions.Operation;

public class ReservationStation {
    private final String name;
    private final Operation operation;
    private boolean isBusy;
    private Double firstOperandValue;
    private Double secondOperandValue;
    private String firstStationThatWillProduceValue;
    private String secondStationThatWillProduceValue;
    private Object immediateOrAddress;

    public ReservationStation(String name, Operation operation) {
        this.name = Objects.requireNonNull(name);
        this.operation = operation;
        this.isBusy = false;
        this.firstOperandValue = null;
        this.secondOperandValue = null;
        this.firstStationThatWillProduceValue = null;
        this.secondStationThatWillProduceValue = null;
        this.immediateOrAddress = null;
    }

    public String getName() {
        return name;
    }

    public Operation getOperation() {
        return operation;
    }

    public boolean isBusy() {
        return isBusy;
    }

    public void setBusy(boolean isBusy) {
        this.isBusy = isBusy;
    }

    public Optional<Double> getFirstOperandValue() {
        return Optional.ofNullable(firstOperandValue);
    }

    public void setFirstOperandValue(double firstOperandValue) {
        this.firstOperandValue = firstOperandValue;
        runIfAllOperandsAreAvailable();
    }

    public Optional<Double> getSecondOperandValue() {
        return Optional.ofNullable(secondOperandValue);
    }

    public void setSecondOperandValue(double secondOperandValue) {
        this.secondOperandValue = secondOperandValue;
        runIfAllOperandsAreAvailable();
    }

    public Optional<String> getFirstStationThatWillProduceValue() {
        return Optional.ofNullable(firstStationThatWillProduceValue);
    }

    public void setFirstStationThatWillProduceValue(String firstStationThatWillProduceValue) {
        this.firstStationThatWillProduceValue = firstStationThatWillProduceValue;
    }

    public Optional<String> getSecondStationThatWillProduceValue() {
        return Optional.ofNullable(secondStationThatWillProduceValue);
    }

    public void setSecondStationThatWillProduceValue(String secondStationThatWillProduceValue) {
        this.secondStationThatWillProduceValue = secondStationThatWillProduceValue;
    }

    public Optional<Object> getImmediateOrAddress() {
        return Optional.ofNullable(immediateOrAddress);
    }

    public void setImmediateOrAddress(Object immediateOrAddress) {
        this.immediateOrAddress = immediateOrAddress;
    }

    private void runIfAllOperandsAreAvailable() {
        if (firstOperandValue != null && secondOperandValue != null) {
            // call functional unit
            System.out.println("all operands available, should run!");
        } else {
            // wait more...
        }
    }
}

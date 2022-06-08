package main.java.components.stations;

import java.util.Objects;

import main.java.instructions.Operation;

public class ReservationStation {
    private final String name;
    private final Operation operation;
    private boolean isBusy;
    private double firstOperandValue;
    private double secondOperandValue;
    private String firstStationThatWillProduceValue;
    private String secondStationThatWillProduceValue;
    private Object immediateOrAddress;

    public ReservationStation(String name, Operation operation) {
        this.name = Objects.requireNonNull(name);
        this.operation = operation;
        this.isBusy = false;
        this.firstOperandValue = 0.0;
        this.secondOperandValue = 0.0;
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

    public double getFirstOperandValue() {
        return firstOperandValue;
    }

    public void setFirstOperandValue(double firstOperandValue) {
        this.firstOperandValue = firstOperandValue;
    }

    public double getSecondOperandValue() {
        return secondOperandValue;
    }

    public void setSecondOperandValue(double secondOperandValue) {
        this.secondOperandValue = secondOperandValue;
    }

    public String getFirstStationThatWillProduceValue() {
        return firstStationThatWillProduceValue;
    }

    public void setFirstStationThatWillProduceValue(String firstStationThatWillProduceValue) {
        this.firstStationThatWillProduceValue = firstStationThatWillProduceValue;
    }

    public String getSecondStationThatWillProduceValue() {
        return secondStationThatWillProduceValue;
    }

    public void setSecondStationThatWillProduceValue(String secondStationThatWillProduceValue) {
        this.secondStationThatWillProduceValue = secondStationThatWillProduceValue;
    }

    public Object getImmediateOrAddress() {
        return immediateOrAddress;
    }

    public void setImmediateOrAddress(Object immediateOrAddress) {
        this.immediateOrAddress = immediateOrAddress;
    }
}

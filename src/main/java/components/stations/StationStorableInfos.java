package main.java.components.stations;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import main.java.instructions.Operation;

public class StationStorableInfos {
    private String firstOperandName;
    private String secondOperandName;
    private String destinationRegisterName;
    private String originStationName;
    private Optional<String> firstOperandNewName;
    private Optional<String> secondOperandNewName;
    private Optional<Double> firstOperandValue;
    private Optional<Double> secondOperandValue; 
    private CountDownLatch countDownLatch;
    private Operation operation;

    public String getFirstOperandName() {
        return firstOperandName;
    }

    public void setFirstOperandName(String firstOperandName) {
        this.firstOperandName = firstOperandName;
    }

    public String getSecondOperandName() {
        return secondOperandName;
    }

    public void setSecondOperandName(String secondOperandName) {
        this.secondOperandName = secondOperandName;
    }

    public String getDestinationRegisterName() {
        return destinationRegisterName;
    }

    public void setDestinationRegisterName(String destinationRegisterName) {
        this.destinationRegisterName = destinationRegisterName;
    }

    public String getOriginStationName() {
        return originStationName;
    }

    public void setOriginStationName(String originStationName) {
        this.originStationName = originStationName;
    }

    public Optional<String> getFirstOperandNewName() {
        return firstOperandNewName;
    }

    public void setFirstOperandNewName(Optional<String> firstOperandNewName) {
        this.firstOperandNewName = firstOperandNewName;
    }

    public Optional<String> getSecondOperandNewName() {
        return secondOperandNewName;
    }

    public void setSecondOperandNewName(Optional<String> secondOperandNewName) {
        this.secondOperandNewName = secondOperandNewName;
    }

    public Optional<Double> getFirstOperandValue() {
        return firstOperandValue;
    }

    public void setFirstOperandValue(Optional<Double> firstOperandValue) {
        this.firstOperandValue = firstOperandValue;
    }

    public Optional<Double> getSecondOperandValue() {
        return secondOperandValue;
    }

    public void setSecondOperandValue(Optional<Double> secondOperandValue) {
        this.secondOperandValue = secondOperandValue;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        return "StationStorableInfos [" +
                "\n\toriginStationName=" + originStationName + 
                "\n\tdestinationRegisterName=" + destinationRegisterName + 
                "\n\toperation=" + operation + 
                "\n\tfirstOperandName=" + firstOperandName + 
                "\n\tfirstOperandNewName=" + firstOperandNewName + 
                "\n\tfirstOperandValue=" + firstOperandValue + 
                "\n\tsecondOperandName=" + secondOperandName + 
                "\n\tsecondOperandNewName=" + secondOperandNewName + 
                "\n\tsecondOperandValue=" + secondOperandValue +
                "]";
    }
}

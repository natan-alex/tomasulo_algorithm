package main.java.components.stations;

import java.util.Objects;

import main.java.components.busses.BusObserver;
import main.java.components.units.FunctionalUnit;
import main.java.instructions.Operation;

public abstract class Station<T extends Number> implements BusObserver {
    protected final String name;
    protected boolean isBusy;
    protected Operation operation;
    protected T firstOperandValue;
    protected T secondOperandValue;
    protected String stationThatWillProduceValueForFirstOperand;
    protected String stationThatWillProduceValueForSecondOperand;
    protected Object immediateOrAddress;
    protected FunctionalUnit relatedUnit;

    public Station(String name, FunctionalUnit unit) {
        this.name = Objects.requireNonNull(name);
        this.relatedUnit = Objects.requireNonNull(unit);
        this.isBusy = false;
        this.operation = null;
        this.firstOperandValue = null;
        this.secondOperandValue = null;
        this.immediateOrAddress = null;
    }

    public abstract void dispatchStoredInfosToUnitIfPossibleWith(StationInstructionAndControlInfos infos);

    public String getName() {
        return name;
    }

    public boolean isBusy() {
        return isBusy;
    }

    public void setBusy(boolean isBusy) {
        this.isBusy = isBusy;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Object getImmediateOrAddress() {
        return immediateOrAddress;
    }

    public void setImmediateOrAddress(Object immediateOrAddress) {
        this.immediateOrAddress = immediateOrAddress;
    }

    public T getFirstOperandValue() {
        return firstOperandValue;
    }

    public void setFirstOperandValue(T value) {
        this.firstOperandValue = value;
    }

    public T getSecondOperandValue() {
        return secondOperandValue;
    }

    public void setSecondOperandValue(T value) {
        this.secondOperandValue = value;
    }

    public String getStationThatWillProduceValueForFirstOperand() {
        return stationThatWillProduceValueForFirstOperand;
    }

    public void setStationThatWillProduceValueForFirstOperand(String stationName) {
        this.stationThatWillProduceValueForFirstOperand = stationName;
    }

    public String getStationThatWillProduceValueForSecondOperand() {
        return stationThatWillProduceValueForSecondOperand;
    }

    public void setStationThatWillProduceValueForSecondOperand(String stationName) {
        this.stationThatWillProduceValueForSecondOperand = stationName;
    }
}

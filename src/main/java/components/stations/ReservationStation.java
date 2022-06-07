package main.java.components.stations;

import java.util.Objects;

import main.java.instructions.Operation;

public class ReservationStation {
    public final String name;
    public final Operation operation;
    public boolean isBusy;
    public double firstOperandValue;
    public double secondOperandValue;
    public String firstStationThatWillProduceValue;
    public String secondStationThatWillProduceValue;
    public Object immediateOrAddress;

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
}

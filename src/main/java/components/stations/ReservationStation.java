package main.java.components.stations;

import java.util.Objects;

import main.java.components.registers.FPRegister;
import main.java.instructions.Operation;

public class ReservationStation {
    public final String name;
    public final Operation[] operations;
    public boolean isBusy;
    public Double firstOperandValue;
    public Double secondOperandValue;
    public String firstStationThatWillProduceValue;
    public String secondStationThatWillProduceValue;
    public Object immediateOrAddress;

    public ReservationStation(String name, Operation[] operations) {
        this.name = Objects.requireNonNull(name);
        this.operations = Objects.requireNonNull(operations);
        this.isBusy = false;
        this.firstOperandValue = null;
        this.secondOperandValue = null;
        this.firstStationThatWillProduceValue = null;
        this.secondStationThatWillProduceValue = null;
        this.immediateOrAddress = null;
    }
}

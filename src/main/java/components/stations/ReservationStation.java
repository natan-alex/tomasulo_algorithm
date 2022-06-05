package main.java.components.stations;

import java.util.Objects;

import main.java.components.registers.FPRegister;
import main.java.instructions.Operation;

public class ReservationStation {
    public final String name;
    public final Operation operation;
    public boolean isBusy;
    public FPRegister firstSourceOperand;
    public FPRegister secondSourceOperand;
    public String firstStationThatWillProduceValue;
    public String secondStationThatWillProduceValue;
    public Object immediateOrAddress;

    public ReservationStation(String name, Operation operation) {
        this.name = Objects.requireNonNull(name);
        this.operation = operation;
        this.isBusy = false;
        this.firstSourceOperand = null;
        this.secondSourceOperand = null;
        this.firstStationThatWillProduceValue = null;
        this.secondStationThatWillProduceValue = null;
        this.immediateOrAddress = null;
    }
}

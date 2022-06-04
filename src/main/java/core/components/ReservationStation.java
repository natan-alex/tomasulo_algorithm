package main.java.core.components;

import java.util.Objects;

import main.java.core.instructions.Operation;

public class ReservationStation {
    private final String name;
    private final Operation operation;
    private boolean isBusy;
    private int immediate;
    private String firstStationThatWillProduceValue;
    private String secondStationThatWillProduceValue;
    private int firstSourceOperandValue;
    private int secondSourceOperandValue;

    public ReservationStation(String name, Operation operation) {
        this.name = Objects.requireNonNull(name);
        this.isBusy = false;
        this.operation = operation;
        this.immediate = 0;
        this.firstStationThatWillProduceValue = "";
        this.secondStationThatWillProduceValue = "";
        this.firstSourceOperandValue = 0;
        this.secondSourceOperandValue = 0;
    }
}

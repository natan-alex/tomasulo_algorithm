package main.java.core.components;

import java.util.Objects;

public class OperandBusses {
    private final FPRegister[] registers;
    private final ReservationStation[][] stations;

    public OperandBusses(FPRegister[] registers, ReservationStation[][] stations) {
        this.registers = Objects.requireNonNull(registers);
        this.stations = Objects.requireNonNull(stations);
    }
}

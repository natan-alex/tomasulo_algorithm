package main.java.core.components;

import java.util.Objects;

import main.java.core.InstructionQueue;

public class OperationsBus {
	private InstructionQueue queue;
    private ReservationStation[][] stations;

	public OperationsBus(InstructionQueue queue, ReservationStation[][] stations) {
        this.queue = Objects.requireNonNull(queue);
        this.stations = Objects.requireNonNull(stations);
    }
}

package main.java.core;

import java.util.Objects;

import main.java.config.Config;
import main.java.core.instructions.Operation;
import main.java.core.components.FPRegister;
import main.java.core.components.ReservationStation;
import main.java.core.components.OperandBusses;
import main.java.core.components.OperationsBus;

public class Architecture {
    private static final String REGISTER_PREFIX = "R";

    public final InstructionQueue instructionQueue;
    public final FPRegister[] registers;
    public final ReservationStation[] addStations;
    public final ReservationStation[] mulStations;
    public final OperandBusses operandBusses;
	public final OperationsBus operationsBus;

    public Architecture(Config config) throws Exception {
        Objects.requireNonNull(config);

        instructionQueue = new InstructionQueue(config.instructionQueueLength);
        registers = createAndInitRegisters(config.numberOfFloatingPointRegisters);
        addStations = createAndInitReservationStations(config.numberOfAddStations, Operation.ADD);
        mulStations = createAndInitReservationStations(config.numberOfMulStations, Operation.MUL);
        var allStations = new ReservationStation[][] { addStations, mulStations };

        operandBusses = new OperandBusses(registers, allStations);
        operationsBus = new OperationsBus(instructionQueue, allStations);
    }

    private FPRegister[] createAndInitRegisters(int numberOfRegisters) {
        var registers = new FPRegister[numberOfRegisters];

        for (int i = 0; i < numberOfRegisters; i++) {
            registers[i] = new FPRegister(REGISTER_PREFIX + (i + 1));
        }

        return registers;
    }

    private ReservationStation[] createAndInitReservationStations(int numberOfStations, Operation operation) {
        var stations = new ReservationStation[numberOfStations];

        for (int i = 0; i < numberOfStations; i++) {
            stations[i] = new ReservationStation(operation.representation + (i + 1), operation);
        }

        return stations;
    }
}

package main.java.components;

import java.util.Objects;

import main.java.components.stations.ReservationStation;
import main.java.config.Config;
import main.java.instructions.Instruction;
import main.java.instructions.Operation;

public class Architecture {
    private final ReservationStation[] addAndSubStations;
    private final ReservationStation[] mulAndDivStations;
    private final InstructionQueue instructionQueue;

    public Architecture(Config config) {
        var addAndSubOperations = new Operation[] { Operation.ADD, Operation.SUB };
        var mulAndDivOperations = new Operation[] { Operation.MUL, Operation.DIV };

        addAndSubStations = createAndInitStations(config.numberOfAddStations, addAndSubOperations);
        mulAndDivStations = createAndInitStations(config.numberOfMulStations, mulAndDivOperations);

        instructionQueue = new InstructionQueue(config.instructionQueueLength);
    }

    private static final ReservationStation[] createAndInitStations(int numberOfStations, Operation[] operations) {
        var stations = new ReservationStation[numberOfStations];

        for (int i = 0; i < numberOfStations; i++) {
            stations[i] = new ReservationStation(operations[0].representation, operations);
        }

        return stations;
    }

    public void schedule(Instruction instruction) {
        Objects.requireNonNull(instruction);
        instructionQueue.enqueue(instruction);
    }

    public void startExecution() {

    }
}

package main.java.components;

import java.util.Arrays;
import java.util.Objects;

import main.java.components.registers.RegistrarBuffer;
import main.java.components.registers.RegistrarManager;
import main.java.components.stations.ReservationStation;
import main.java.config.Config;
import main.java.instructions.Instruction;
import main.java.instructions.InstructionType;
import main.java.instructions.Operation;
import main.java.instructions.RTypeInstruction;

public class Architecture {
    private final ReservationStation[] addAndSubStations;
    private final ReservationStation[] mulAndDivStations;
    private final InstructionQueue instructionQueue;
    private final RegistrarManager registrarFile;

    public Architecture(Config config) {
        var addAndSubOperations = new Operation[] { Operation.ADD, Operation.SUB };
        var mulAndDivOperations = new Operation[] { Operation.MUL, Operation.DIV };

        addAndSubStations = createAndInitStations(config.numberOfAddStations, addAndSubOperations);
        mulAndDivStations = createAndInitStations(config.numberOfMulStations, mulAndDivOperations);

        instructionQueue = new InstructionQueue(config.instructionQueueLength);

        registrarFile = new RegistrarBuffer(config.numberOfFloatingPointRegisters);
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
        var nextInstruction = instructionQueue.dispatch();

        while (nextInstruction.isPresent()) {
            tryAllocateInANotBusyStation(nextInstruction.get());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            nextInstruction = instructionQueue.dispatch();
        }
    }

    private ReservationStation[] getCorrespondingStationsFor(Instruction instruction) {
        if (instruction.operation.isMulOrDiv()) {
            return mulAndDivStations;
        }

        return addAndSubStations;
    }

    private boolean tryAllocateInANotBusyStation(Instruction instruction) {
        if (instruction.type == InstructionType.R) {
            return tryAllocateInANotBusyStationForRTypeInstruction((RTypeInstruction) instruction);
        }

        return false;
    }

    private boolean tryAllocateInANotBusyStationForRTypeInstruction(RTypeInstruction instruction) {
        var stations = getCorrespondingStationsFor(instruction);
        var optional = Arrays.stream(stations).filter(s -> !s.isBusy).findFirst();
        
        if (optional.isEmpty()) {
            System.out.println("All busy");
            return false;
        }

        var station = optional.get();
        station.isBusy = true;

        if (registrarFile.isRegisterWaiting(instruction.firstOperand.name)) {
            var stationName = registrarFile.getStationThatWillProduceValueFor(instruction.firstOperand.name);
            station.firstStationThatWillProduceValue = stationName.get();
        } else {
            var value = registrarFile.getValueFromRegister(instruction.firstOperand.name);
            station.firstOperandValue = value.get();
        }

        if (registrarFile.isRegisterWaiting(instruction.secondOperand.name)) {
            var stationName = registrarFile.getStationThatWillProduceValueFor(instruction.secondOperand.name);
            station.secondStationThatWillProduceValue = stationName.get();
        } else {
            var value = registrarFile.getValueFromRegister(instruction.secondOperand.name);
            station.secondOperandValue = value.get();
        }

        return true;
    }

    public void showReservationStations() {
        for (var reservationStation : addAndSubStations) {
            System.out.println(reservationStation.isBusy);
            System.out.println(reservationStation.firstOperandValue);
            System.out.println(reservationStation.firstStationThatWillProduceValue);
            System.out.println(reservationStation.secondOperandValue);
            System.out.println(reservationStation.secondStationThatWillProduceValue);
            System.out.println();
        }
    }
}

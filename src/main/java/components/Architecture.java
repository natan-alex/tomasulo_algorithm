package main.java.components;

import java.util.Objects;

import main.java.components.registers.RegistrarBank;
import main.java.components.registers.ReorderBuffer;
import main.java.components.stations.ReservationStationManager;
import main.java.config.Config;
import main.java.instructions.RTypeInstruction;

public class Architecture {
    private final ReservationStationManager stationsManager;
    private final InstructionQueue instructionQueue;
    private final RegistrarBank registrarBank;
    private final ReorderBuffer reorderBuffer;

    public Architecture(Config config) {
        stationsManager = new ReservationStationManager(config.numberOfAddStations, config.numberOfMulStations);
        instructionQueue = new InstructionQueue(config.instructionQueueLength);

        registrarBank = new RegistrarBank(config.numberOfFloatingPointRegisters);
        registrarBank.setRandomValuesInRegisters();

        reorderBuffer = new ReorderBuffer(registrarBank.getRegistrarNames());
    }

    public void schedule(RTypeInstruction instruction) {
        Objects.requireNonNull(instruction);
        instructionQueue.enqueue(instruction);
    }

    public void startExecution() {
        var nextInstruction = instructionQueue.dispatch();

        while (nextInstruction.isPresent()) {
            tryStoreInANotBusyStation(nextInstruction.get());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            nextInstruction = instructionQueue.dispatch();
        }
    }

    private boolean tryStoreInANotBusyStation(RTypeInstruction instruction) {
        var result = stationsManager.getNotBusyStationForOperation(instruction.operation);

        if (result.isEmpty()) {
            System.out.println("All busy");
            return false;
        }

        var station = result.get();
        station.isBusy = true;

        var firstOperandNewName = reorderBuffer.getRegisterNewName(instruction.firstOperand.name);
        var secondOperandNewName = reorderBuffer.getRegisterNewName(instruction.secondOperand.name);

        if (firstOperandNewName.isPresent()) {
            station.firstStationThatWillProduceValue = firstOperandNewName.get();
        } else {
            station.firstOperandValue = registrarBank.getRegisterValue(instruction.firstOperand.name);
        }

        if (secondOperandNewName.isPresent()) {
            station.secondStationThatWillProduceValue = secondOperandNewName.get();
        } else {
            station.secondOperandValue = registrarBank.getRegisterValue(instruction.secondOperand.name);
        }

        return true;
    }

    public void showReservationStations() {
        stationsManager.showStations();
    }
}

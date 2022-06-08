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
        var result = stationsManager.getNotBusyStationForOperation(instruction.getOperation());

        if (result.isEmpty()) {
            System.out.println("All busy");
            return false;
        }

        var station = result.get();
        station.setBusy(true);

        var firstOperandName = instruction.getFirstOperand().getName();
        var secondOperandName = instruction.getSecondOperand().getName();
        var firstOperandNewName = reorderBuffer.getNewNameForRegister(firstOperandName);
        var secondOperandNewName = reorderBuffer.getNewNameForRegister(secondOperandName);

        if (firstOperandNewName.isPresent()) {
            station.setFirstStationThatWillProduceValue(firstOperandNewName.get());
        } else {
            station.setFirstOperandValue(registrarBank.getRegisterValue(firstOperandName));
        }

        if (secondOperandNewName.isPresent()) {
            station.setSecondStationThatWillProduceValue(secondOperandNewName.get());
        } else {
            station.setSecondOperandValue(registrarBank.getRegisterValue(secondOperandName));
        }

        return true;
    }

    public void showReservationStations() {
        stationsManager.showStations();
    }
}

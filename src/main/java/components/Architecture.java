package main.java.components;

import java.util.Objects;

import main.java.components.registers.RegistrarBuffer;
import main.java.components.stations.ReservationStationManager;
import main.java.config.Config;
import main.java.instructions.RTypeInstruction;

public class Architecture {
    private final ReservationStationManager stationsManager;
    private final InstructionQueue instructionQueue;
    private final RegistrarBuffer registrarBuffer;

    public Architecture(Config config) {
        stationsManager = new ReservationStationManager(config.numberOfAddStations, config.numberOfMulStations);
        instructionQueue = new InstructionQueue(config.instructionQueueLength);

        registrarBuffer = new RegistrarBuffer(config.numberOfFloatingPointRegisters);
        registrarBuffer.setRandomValuesInRegisters();
    }

    public void schedule(RTypeInstruction instruction) {
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

    private boolean tryAllocateInANotBusyStation(RTypeInstruction instruction) {
        var result = stationsManager.getNotBusyStationForOperation(instruction.operation);

        if (result.isEmpty()) {
            System.out.println("All busy");
            return false;
        }

        var station = result.get();

        if (registrarBuffer.isRegisterWaiting(instruction.firstOperand.name)) {
            var stationName = registrarBuffer.getStationThatWillProduceValueFor(instruction.firstOperand.name);
            station.firstStationThatWillProduceValue = stationName.get();
        } else {
            var value = registrarBuffer.getValueFromRegister(instruction.firstOperand.name);
            station.firstOperandValue = value.get();
        }

        if (registrarBuffer.isRegisterWaiting(instruction.secondOperand.name)) {
            var stationName = registrarBuffer.getStationThatWillProduceValueFor(instruction.secondOperand.name);
            station.secondStationThatWillProduceValue = stationName.get();
        } else {
            var value = registrarBuffer.getValueFromRegister(instruction.secondOperand.name);
            station.secondOperandValue = value.get();
        }

        return true;
    }

    public void showReservationStations() {
        stationsManager.showStations();
    }
}

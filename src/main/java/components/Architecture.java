package main.java.components;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import main.java.components.registers.RegistrarBank;
import main.java.components.registers.ReorderBuffer;
import main.java.components.stations.ReservationStation;
import main.java.components.units.AddFunctionalUnit;
import main.java.components.units.FunctionalUnit;
import main.java.config.Config;
import main.java.instructions.Operation;
import main.java.instructions.RTypeInstruction;

public class Architecture {
    private final ReservationStation[] addReservationStations;
    private final ReservationStation[] mulReservationStations;
    private final FunctionalUnit[] fpAdders;
    private final FunctionalUnit[] fpMultipliers;
    private final InstructionQueue instructionQueue;
    private final RegistrarBank registrarBank;
    private final ReorderBuffer reorderBuffer;

    public Architecture(Config config) {
        fpAdders = new AddFunctionalUnit[config.numberOfAddStations];
        addReservationStations = new ReservationStation[config.numberOfAddStations];
        initAddersAndRelatedStations();

        fpMultipliers = null;
        mulReservationStations = null;

        instructionQueue = new InstructionQueue(config.instructionQueueLength);

        registrarBank = new RegistrarBank(config.numberOfFloatingPointRegisters);
        registrarBank.setRandomValuesInRegisters();

        reorderBuffer = new ReorderBuffer(registrarBank.getRegistrarNames());
    }

    private void initAddersAndRelatedStations() {
        for (int i = 0; i < addReservationStations.length; i++) {
            fpAdders[i] = new AddFunctionalUnit();

            addReservationStations[i] = new ReservationStation(
                Operation.ADD.representation + i,
                Operation.ADD,
                fpAdders[i]
            );
        }
    }

    public void schedule(RTypeInstruction instruction) {
        Objects.requireNonNull(instruction);
        instructionQueue.enqueue(instruction);
    }

    public void startExecution() {
        var nextInstruction = instructionQueue.dispatch();

        while (nextInstruction.isPresent()) {
            System.out.println("Trying to execute << " + nextInstruction.get() + " >>");
            tryStoreInAFreeStation(nextInstruction.get());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            nextInstruction = instructionQueue.dispatch();
        }
    }

    public Optional<ReservationStation> getNotBusyStationForOperation(Operation operation) {
        var stationsToLookIn = addReservationStations;

        if (operation.isMulOrDiv()) {
            stationsToLookIn = mulReservationStations;
        }

        return Arrays.stream(stationsToLookIn)
            .filter(s -> !s.isBusy())
            .findFirst();
    }

    private boolean tryStoreInAFreeStation(RTypeInstruction instruction) {
        var result = getNotBusyStationForOperation(instruction.getOperation());

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
        Consumer<ReservationStation> consumer = (reservationStation) -> {
            System.out.println(reservationStation.isBusy());
            System.out.println(reservationStation.getFirstOperandValue());
            System.out.println(reservationStation.getFirstStationThatWillProduceValue());
            System.out.println(reservationStation.getSecondOperandValue());
            System.out.println(reservationStation.getSecondStationThatWillProduceValue());
            System.out.println();
        };
        
        Arrays.stream(addReservationStations).forEach(consumer);
        // Arrays.stream(mulReservationStations).forEach(consumer);
    }
}
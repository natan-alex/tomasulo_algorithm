package main.java.components;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import main.java.config.Config;
import main.java.components.busses.CommonDataBus;
import main.java.components.busses.DataBus;
import main.java.components.registers.RegistrarBank;
import main.java.components.registers.ReorderBuffer;
import main.java.components.stations.ReservationStation;
import main.java.components.stations.Station;
import main.java.components.units.AddFunctionalUnit;
import main.java.components.units.FunctionalUnit;
import main.java.instructions.Operation;
import main.java.instructions.RTypeInstruction;

public class Architecture {
    private final Station[] addReservationStations;
    private final Station[] mulReservationStations;
    private final FunctionalUnit[] fpAdders;
    private final FunctionalUnit[] fpMultipliers;
    private final InstructionQueue instructionQueue;
    private final RegistrarBank registrarBank;
    private final ReorderBuffer reorderBuffer;
    private final DataBus commonDataBus;

    private int numberOfScheduledInstructions;
    private CountDownLatch countDownLatch;

    public Architecture(Config config) {
        commonDataBus = new CommonDataBus();

        fpAdders = new AddFunctionalUnit[config.numberOfAddStations];
        addReservationStations = new ReservationStation[config.numberOfAddStations];
        initAddersAndRelatedStations();

        fpMultipliers = null;
        mulReservationStations = null;

        instructionQueue = new InstructionQueue(config.instructionQueueLength);

        registrarBank = new RegistrarBank(config.numberOfFloatingPointRegisters);
        registrarBank.setRandomValuesInRegisters();

        reorderBuffer = new ReorderBuffer(registrarBank.getRegistrarNames());

        addObserversToCommonDataBus();
    }

    private void addObserversToCommonDataBus() {
        Arrays.stream(addReservationStations).forEach(s -> commonDataBus.addObserver(s));
        commonDataBus.addObserver(registrarBank);
        commonDataBus.addObserver(reorderBuffer);
    }

    private void initAddersAndRelatedStations() {
        for (int i = 0; i < addReservationStations.length; i++) {
            fpAdders[i] = new AddFunctionalUnit(
                    Operation.ADD.getRepresentation() + i,
                    commonDataBus);

            addReservationStations[i] = new ReservationStation(
                    Operation.ADD.getRepresentation() + i,
                    Operation.ADD,
                    fpAdders[i]);
        }
    }

    public void schedule(RTypeInstruction instruction) {
        Objects.requireNonNull(instruction);
        instructionQueue.enqueue(instruction);
        numberOfScheduledInstructions++;
    }

    public void startExecution() {
        countDownLatch = new CountDownLatch(numberOfScheduledInstructions);

        for (var instruction : instructionQueue) {
            System.out.println("LOG from architecture:\n\tExecuting << " + instruction + " >>");

            fetchInstructionOperandValues(instruction);
            storeInAFreeStation(instruction);
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void fetchInstructionOperandValues(RTypeInstruction instruction) {
        var firstOperand = instruction.getFirstOperand();
        var secondOperand = instruction.getSecondOperand();
        var firstOperandValue = registrarBank.getRegisterValue(firstOperand.getName());
        var secondOperandValue = registrarBank.getRegisterValue(secondOperand.getName());
        firstOperand.setValue(firstOperandValue.get());
        secondOperand.setValue(secondOperandValue.get());
    }

    public Optional<Station> getNotBusyStationForOperation(Operation operation) {
        var stationsToLookIn = addReservationStations;

        if (operation.isMulOrDiv()) {
            stationsToLookIn = mulReservationStations;
        }

        return Arrays.stream(stationsToLookIn)
                .filter(s -> !s.isBusy())
                .findFirst();
    }

    private void storeInAFreeStation(RTypeInstruction instruction) {
        var result = getNotBusyStationForOperation(instruction.getOperation());

        if (result.isEmpty()) {
            System.out.println("All busy");
            return;
        }

        var station = result.get();
        var firstOperandName = instruction.getFirstOperand().getName();
        var secondOperandName = instruction.getSecondOperand().getName();
        var firstOperandNewName = reorderBuffer.getNewNameForRegister(firstOperandName);
        var secondOperandNewName = reorderBuffer.getNewNameForRegister(secondOperandName);

        reorderBuffer.setNewNameForRegister(instruction.getDestination().getName(), station.getName());
        station.storeInstructionAndTryDispatch(instruction, firstOperandNewName, secondOperandNewName, countDownLatch);
    }
}
package main.java.main;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import main.java.config.Config;
import main.java.components.InstructionQueue;
import main.java.components.busses.CommonDataBus;
import main.java.components.busses.DataBus;
import main.java.components.registers.BaseRegisterBank;
import main.java.components.registers.BaseReorderBuffer;
import main.java.components.registers.Register;
import main.java.components.registers.RegisterBank;
import main.java.components.registers.ReorderBuffer;
import main.java.components.stations.ReservationStation;
import main.java.components.stations.Station;
import main.java.components.stations.StationStorableInfos;
import main.java.components.units.AddFunctionalUnit;
import main.java.components.units.FunctionalUnit;
import main.java.components.units.MulFunctionalUnit;
import main.java.instructions.Operation;
import main.java.instructions.RTypeInstruction;

public class Architecture {
    private final Station[] addReservationStations;
    private final Station[] mulReservationStations;
    private final FunctionalUnit[] fpAdders;
    private final FunctionalUnit[] fpMultipliers;
    private final InstructionQueue instructionQueue;
    private final BaseRegisterBank<Double> registerBank;
    private final BaseReorderBuffer reorderBuffer;
    private final DataBus commonDataBus;

    private CountDownLatch countDownLatch;

    public Architecture(Config config) {
        instructionQueue = new InstructionQueue();

        commonDataBus = new CommonDataBus();

        registerBank = new RegisterBank(config.numberOfFloatingPointRegisters);
        registerBank.setRandomValuesInRegisters();

        reorderBuffer = new ReorderBuffer(registerBank);

        fpAdders = new AddFunctionalUnit[config.numberOfAddStations];
        addReservationStations = new ReservationStation[config.numberOfAddStations];
        initAddersAndRelatedStations();

        fpMultipliers = new MulFunctionalUnit[config.numberOfMulStations];
        mulReservationStations = new ReservationStation[config.numberOfMulStations];
        initMultipliersAndRelatedStations();

        addObserversToCommonDataBus();
    }

    private void initAddersAndRelatedStations() {
        for (int i = 0; i < addReservationStations.length; i++) {
            fpAdders[i] = new AddFunctionalUnit(
                    Operation.ADD.getRepresentation() + i,
                    commonDataBus);

            addReservationStations[i] = new ReservationStation(
                    Operation.ADD.getRepresentation() + i,
                    fpAdders[i]);
        }
    }

    private void initMultipliersAndRelatedStations() {
        for (int i = 0; i < mulReservationStations.length; i++) {
            fpMultipliers[i] = new MulFunctionalUnit(
                    Operation.MUL.getRepresentation() + i,
                    commonDataBus);

            mulReservationStations[i] = new ReservationStation(
                    Operation.MUL.getRepresentation() + i,
                    fpMultipliers[i]);
        }
    }

    private void addObserversToCommonDataBus() {
        Arrays.stream(addReservationStations).forEach(commonDataBus::addObserver);
        Arrays.stream(mulReservationStations).forEach(commonDataBus::addObserver);
        commonDataBus.addObserver(registerBank);
        commonDataBus.addObserver(reorderBuffer);
    }

    public String[] getRegisterNames() {
        return registerBank.getRegisterNames();
    }

    public void schedule(RTypeInstruction instruction) {
        Objects.requireNonNull(instruction);
        instructionQueue.enqueue(instruction);
    }

    public void startExecution() {
        countDownLatch = new CountDownLatch(instructionQueue.size());

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
        var firstOperandValue = registerBank.getRegisterValue(firstOperand.getName());
        var secondOperandValue = registerBank.getRegisterValue(secondOperand.getName());

        firstOperand.setValue(firstOperandValue.get());
        secondOperand.setValue(secondOperandValue.get());
    }

    private void storeInAFreeStation(RTypeInstruction instruction) {
        var result = getNotBusyStationForOperation(instruction.getOperation());

        if (result.isEmpty()) {
            System.out.println("All busy");
            return;
        }

        var station = result.get();
        var infos = new StationStorableInfos();

        infos.setOriginStationName(station.getName());
        infos.setOperation(instruction.getOperation());
        infos.setDestinationRegisterName(instruction.getDestination().getName());
        infos.setFirstOperandName(instruction.getFirstOperand().getName());
        infos.setFirstOperandValue(instruction.getFirstOperand().getValue());
        infos.setFirstOperandNewName(reorderBuffer.getNewNameForRegister(instruction.getFirstOperand().getName()));
        infos.setSecondOperandName(instruction.getSecondOperand().getName());
        infos.setSecondOperandValue(instruction.getSecondOperand().getValue());
        infos.setSecondOperandNewName(reorderBuffer.getNewNameForRegister(instruction.getSecondOperand().getName()));
        infos.setCountDownLatch(countDownLatch);

        reorderBuffer.renameRegister(instruction.getDestination().getName(), station.getName());
        station.storeInfosAndTryDispatch(infos);
    }

    private Optional<Station> getNotBusyStationForOperation(Operation operation) {
        var stationsToLookIn = addReservationStations;

        if (operation.isMulOrDiv()) {
            stationsToLookIn = mulReservationStations;
        }

        return Arrays.stream(stationsToLookIn)
                .filter(s -> !s.isBusy())
                .findFirst();
    }
}
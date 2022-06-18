package main.java.main;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import main.java.config.Config;
import main.java.components.InstructionQueue;
import main.java.components.buffers.Buffer;
import main.java.components.buffers.LoadBuffer;
import main.java.components.buffers.ReorderBuffer;
import main.java.components.buffers.StoreBuffer;
import main.java.components.busses.BaseOperandBusses;
import main.java.components.busses.BaseOperationsBus;
import main.java.components.busses.CommonDataBus;
import main.java.components.busses.DataBus;
import main.java.components.busses.OperandBusses;
import main.java.components.busses.OperationsBus;
import main.java.components.registers.AddressRegisterBank;
import main.java.components.registers.BaseRegisterBank;
import main.java.components.registers.BaseRegisterBankObserver;
import main.java.components.registers.BaseReorderBuffer;
import main.java.components.registers.FPRegisterBank;
import main.java.components.stations.StationInstructionAndControlInfos;
import main.java.components.stations.ReservationStation;
import main.java.components.stations.Station;
import main.java.components.units.AddFunctionalUnit;
import main.java.components.units.AddressUnit;
import main.java.components.units.BaseAddressUnit;
import main.java.components.units.BaseMemoryUnit;
import main.java.components.units.FunctionalUnit;
import main.java.components.units.MemoryInstructionAndControlInfos;
import main.java.components.units.MemoryUnit;
import main.java.components.units.MulFunctionalUnit;
import main.java.instructions.MemoryTypeInstruction;
import main.java.instructions.Operation;
import main.java.instructions.RTypeInstruction;

public class Architecture {
    private final Station<Double>[] addReservationStations;
    private final Station<Double>[] mulReservationStations;
    private final Station<Double>[] allReservationStations;
    private final FunctionalUnit[] fpAdders;
    private final FunctionalUnit[] fpMultipliers;
    private final Buffer[] loadBuffers;
    private final Buffer[] storeBuffers;
    private final Buffer[] allBuffers;
    private final InstructionQueue instructionQueue;
    private final BaseRegisterBankObserver<Double> fpRegisterBank;
    private final BaseRegisterBank<Integer> addressRegisterBank;
    private final BaseReorderBuffer reorderBuffer;
    private final BaseAddressUnit addressUnit;
    private final DataBus commonDataBus;
    private final BaseOperationsBus<Double> operationsBus;
    private final BaseOperandBusses<Double> operandBusses;
    private final BaseMemoryUnit memoryUnit;

    private CountDownLatch countDownLatch;

    public Architecture(Config config) {
        instructionQueue = new InstructionQueue();

        commonDataBus = new CommonDataBus();

        fpRegisterBank = new FPRegisterBank(config.numberOfFloatingPointRegisters);

        addressRegisterBank = new AddressRegisterBank(config.numberOfAddressRegisters);

        reorderBuffer = new ReorderBuffer(fpRegisterBank);

        loadBuffers = new LoadBuffer[config.numberOfLoadBuffers];
        storeBuffers = new StoreBuffer[config.numberOfStoreBuffers];

        allBuffers = new Buffer[loadBuffers.length + storeBuffers.length];

        memoryUnit = new MemoryUnit(commonDataBus, config.cyclesToPerformALoad, config.cyclesToPerformAStore);
        addressUnit = new AddressUnit(addressRegisterBank, fpRegisterBank, allBuffers, reorderBuffer);

        fpAdders = new AddFunctionalUnit[config.numberOfAddStations];
        addReservationStations = new ReservationStation[config.numberOfAddStations];

        fpMultipliers = new MulFunctionalUnit[config.numberOfMulStations];
        mulReservationStations = new ReservationStation[config.numberOfMulStations];

        allReservationStations = new ReservationStation[addReservationStations.length + mulReservationStations.length];

        operationsBus = new OperationsBus(allReservationStations);
        operandBusses = new OperandBusses(allReservationStations, fpRegisterBank, reorderBuffer);

        setup(config);
    }

    private void setup(Config config) {
        fpRegisterBank.setRandomValuesInRegisters();
        addressRegisterBank.setRandomValuesInRegisters();

        initBuffers();
        initAddersAndRelatedStations(config);
        initMultipliersAndRelatedStations(config);
        addObserversToCommonDataBus();
    }

    private void initAddersAndRelatedStations(Config config) {
        for (int i = 0; i < addReservationStations.length; i++) {
            fpAdders[i] = new AddFunctionalUnit(
                    Operation.ADD.getRepresentation() + i,
                    config.cyclesToPerformAnAdd,
                    config.cyclesToPerformASub,
                    commonDataBus);

            addReservationStations[i] = new ReservationStation(
                    Operation.ADD.getRepresentation() + i,
                    fpAdders[i]);

            allReservationStations[i] = addReservationStations[i];
        }
    }

    private void initMultipliersAndRelatedStations(Config config) {
        for (int i = 0; i < mulReservationStations.length; i++) {
            fpMultipliers[i] = new MulFunctionalUnit(
                    Operation.MUL.getRepresentation() + i,
                    config.cyclesToPerformAMul,
                    config.cyclesToPerformADiv,
                    commonDataBus);

            mulReservationStations[i] = new ReservationStation(
                    Operation.MUL.getRepresentation() + i,
                    fpMultipliers[i]);

            allReservationStations[i + addReservationStations.length] = mulReservationStations[i];
        }
    }

    private void initBuffers() {
        for (int i = 0; i < loadBuffers.length; i++) {
            loadBuffers[i] = new LoadBuffer(Operation.LOAD.getRepresentation() + i, memoryUnit);
            allBuffers[i] = loadBuffers[i];
        }

        for (int i = 0; i < storeBuffers.length; i++) {
            storeBuffers[i] = new StoreBuffer(Operation.STORE.getRepresentation() + i, memoryUnit);
            allBuffers[i + loadBuffers.length] = storeBuffers[i];
        }
    }

    private void addObserversToCommonDataBus() {
        commonDataBus.addObserver(fpRegisterBank);
        commonDataBus.addObserver(reorderBuffer);
        Arrays.stream(allReservationStations).forEach(commonDataBus::addObserver);
        Arrays.stream(allBuffers).forEach(commonDataBus::addObserver);
    }

    public String[] getFPRegisterNames() {
        return fpRegisterBank.getRegisterNames();
    }

    public String[] getAddressRegisterNames() {
        return addressRegisterBank.getRegisterNames();
    }

    public void schedule(RTypeInstruction instruction) {
        Objects.requireNonNull(instruction);
        instructionQueue.enqueue(instruction);
    }

    public void schedule(MemoryTypeInstruction instruction) {
        Objects.requireNonNull(instruction);
        instructionQueue.enqueue(instruction);
    }

    public void startExecution() {
        countDownLatch = new CountDownLatch(instructionQueue.size());

        for (var instruction : instructionQueue) {
            System.out.println("\nLOG from ARCHITECTURE:"
                    + "\n\tExecuting << " + instruction + " >>\n");

            if (instruction instanceof RTypeInstruction) {
                tryDispatchRTypeInstruction((RTypeInstruction) instruction);
            } else {
                tryDispatchMemTypeInstruction((MemoryTypeInstruction) instruction);
            }
        }

        awaitCountDownLatch();
    }

    private void tryDispatchRTypeInstruction(RTypeInstruction instruction) {
        var stationName = operationsBus.tryStoreOperationInAFreeStationAndMarkItBusy(instruction.getOperation());
        var infos = new StationInstructionAndControlInfos(instruction, countDownLatch);
        operandBusses.storeInfosInStation(infos, stationName.get());
    }

    private void tryDispatchMemTypeInstruction(MemoryTypeInstruction instruction) {
        var infos = new MemoryInstructionAndControlInfos(instruction, countDownLatch);
        addressUnit.calculateAddressAndStoreInABuffer(infos);
    }

    private void awaitCountDownLatch() {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
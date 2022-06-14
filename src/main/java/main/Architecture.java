package main.java.main;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

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
        fpRegisterBank.setRandomValuesInRegisters();

        addressRegisterBank = new AddressRegisterBank(5);
        addressRegisterBank.setRandomValuesInRegisters();

        reorderBuffer = new ReorderBuffer(fpRegisterBank);

        loadBuffers = new LoadBuffer[config.numberOfLoadBuffers];
        storeBuffers = new StoreBuffer[config.numberOfStoreBuffers];

        var allBuffers = Stream.concat(Arrays.stream(loadBuffers), Arrays.stream(storeBuffers)).toArray(Buffer[]::new);

        memoryUnit = new MemoryUnit(commonDataBus);
        addressUnit = new AddressUnit(addressRegisterBank, fpRegisterBank, allBuffers, reorderBuffer);

        fpAdders = new AddFunctionalUnit[config.numberOfAddStations];
        addReservationStations = new ReservationStation[config.numberOfAddStations];

        fpMultipliers = new MulFunctionalUnit[config.numberOfMulStations];
        mulReservationStations = new ReservationStation[config.numberOfMulStations];

        allReservationStations = new ReservationStation[addReservationStations.length + mulReservationStations.length];

        initBuffers();
        initAddersAndRelatedStations();
        initMultipliersAndRelatedStations();

        operationsBus = new OperationsBus(allReservationStations);
        operandBusses = new OperandBusses(allReservationStations, fpRegisterBank, reorderBuffer);

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

            allReservationStations[i] = addReservationStations[i];
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

            allReservationStations[i + addReservationStations.length] = mulReservationStations[i];
        }
    }

    private void initBuffers() {
        for (int i = 0; i < loadBuffers.length; i++) {
            loadBuffers[i] = new LoadBuffer(Operation.LOAD.getRepresentation() + i, memoryUnit);
        }

        for (int i = 0; i < storeBuffers.length; i++) {
            storeBuffers[i] = new StoreBuffer(Operation.STORE.getRepresentation() + i, memoryUnit);
        }
    }

    private void addObserversToCommonDataBus() {
        commonDataBus.addObserver(fpRegisterBank);
        commonDataBus.addObserver(reorderBuffer);
        Arrays.stream(addReservationStations).forEach(commonDataBus::addObserver);
        Arrays.stream(mulReservationStations).forEach(commonDataBus::addObserver);
        Arrays.stream(loadBuffers).forEach(commonDataBus::addObserver);
        Arrays.stream(storeBuffers).forEach(commonDataBus::addObserver);
    }

    public String[] getRegisterNames() {
        return fpRegisterBank.getRegisterNames();
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
            System.out.println("LOG from architecture:\n\tExecuting << " + instruction + " >>");

            if (instruction instanceof RTypeInstruction) {
                destructureRTypeInstructionAndTryDispatch((RTypeInstruction) instruction);
            } else {
                destructureMemTypeInstructionAndTryDispatch((MemoryTypeInstruction) instruction);
            }
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void destructureRTypeInstructionAndTryDispatch(RTypeInstruction instruction) {
        var stationName = operationsBus.storeOperationInStationAndMarkItBusy(instruction.getOperation());

        if (stationName.isEmpty()) {
            System.out.println("All reservation stations busy :(");
            return;
        }

        var infos = new StationInstructionAndControlInfos(instruction, countDownLatch);
        reorderBuffer.renameRegister(instruction.getDestinationRegister().getName(), stationName.get());
        operandBusses.storeInfosInStation(infos, stationName.get());
    }

    private void destructureMemTypeInstructionAndTryDispatch(MemoryTypeInstruction instruction) {
        var infos = new MemoryInstructionAndControlInfos(instruction, countDownLatch);
        reorderBuffer.renameRegister(instruction.getDestinationRegister().getName(), memoryUnit.getName());
        addressUnit.calculateAddressAndStoreInABuffer(infos);
    }
}
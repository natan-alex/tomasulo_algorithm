package main.java.components.buffers;

import java.util.Objects;
import java.util.Optional;

import main.java.components.units.BaseMemoryUnit;
import main.java.components.units.FunctionaUnitBroadcastInfos;
import main.java.components.units.MemoryUnitBroadcastInfos;
import main.java.instructions.Operation;

public class StoreBuffer extends Buffer {
    private String componentThatWillProduceValueToStore;
    private MemoryUnitBroadcastInfos previouslyStoreInfos;

    public StoreBuffer(String name, BaseMemoryUnit memoryUnit) {
        super(name, Operation.STORE, memoryUnit);
    }

    @Override
    public void handleCalculatedResult(FunctionaUnitBroadcastInfos infos, double calculatedResult) {
        Objects.requireNonNull(infos);

        if (componentThatWillProduceValueToStore != null &&
                componentThatWillProduceValueToStore.equals(infos.getOriginStationName())) {
            System.out.println("LOG from " + name + " buffer:"
                    + "\n\tUsing broadcasted value << " + calculatedResult + " >> for operand");

            previouslyStoreInfos.setDestinationRegisterValue(calculatedResult);
            memoryUnit.execute(previouslyStoreInfos);
        }
    }

    private void clearBuffer() {
        isBusy = false;
        componentThatWillProduceValueToStore = null;
        previouslyStoreInfos = null;
    }

    @Override
    public void handleGotMemoryData(MemoryUnitBroadcastInfos infos, Optional<Double> memData) {
        Objects.requireNonNull(infos);

        if (infos.getOriginBufferName().equals(name)) {
            clearBuffer();
            return;
        }

        if (componentThatWillProduceValueToStore != null &&
                componentThatWillProduceValueToStore.equals(infos.getOriginBufferName())) {
            var value = memData.orElseThrow();

            System.out.println("LOG from " + name + " buffer:"
                    + "\n\tUsing broadcasted value " + value + " for operand");

            previouslyStoreInfos.setDestinationRegisterValue(value);
            memoryUnit.execute(previouslyStoreInfos);
        }
    }

    @Override
    public void storeInfosAndSendToMemoryUnit(MemoryUnitBroadcastInfos infos) {
        this.previouslyStoreInfos = Objects.requireNonNull(infos);

        isBusy = true;
        address = infos.getAddress();

        if (infos.getDestinationRegisterNewName().isPresent()) {
            componentThatWillProduceValueToStore = infos.getDestinationRegisterNewName().get();

            System.out.println("LOG from " + name + " buffer:"
                    + "\n\tWaiting << " + componentThatWillProduceValueToStore
                    + " >> to produce value that will be stored in memory");
        } else {
            System.out.println("LOG from " + name + " buffer:"
                    + "\n\tAll operands available: << " + infos.getDestinationRegisterValue().get() + " >> "
                    + "\n\tPassing to memory unit");

            memoryUnit.execute(infos);
        }
    }
}

package main.java.components.buffers;

import java.util.Objects;
import java.util.Optional;

import main.java.components.units.BaseMemoryUnit;
import main.java.components.units.FunctionaUnitBroadcastInfos;
import main.java.components.units.MemoryUnitBroadcastInfos;
import main.java.instructions.Operation;

public class StoreBuffer extends Buffer {
    private String componentThatWillProduceValueToStore;
    private MemoryUnitBroadcastInfos previousInfos;

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

            previousInfos.setDestinationRegisterValue(calculatedResult);
            memoryUnit.execute(previousInfos);
        }
    }

    private void clearBuffer() {
        isBusy = false;
        componentThatWillProduceValueToStore = null;
        previousInfos = null;
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
            System.out.println("LOG from " + name + " buffer:"
                    + "\n\tUsing broadcasted value " + memData + " for operand");

            previousInfos.setDestinationRegisterValue(memData.orElseThrow());
            memoryUnit.execute(previousInfos);
        }
    }

    @Override
    public void storeInfosAndSendToMemoryUnit(MemoryUnitBroadcastInfos infos) {
        this.previousInfos = Objects.requireNonNull(infos);

        isBusy = true;
        address = infos.getAddress();

        if (infos.getDestinationRegisterNewName().isPresent()) {
            componentThatWillProduceValueToStore = infos.getDestinationRegisterNewName().get();
        } else {
            System.out.println("LOG from " + name + " buffer:"
                    + "\n\tAll operands available: << " + infos.getDestinationRegisterValue().get() + " >> "
                    + "\n\tPassing to memory unit");

            memoryUnit.execute(infos);
        }
    }
}

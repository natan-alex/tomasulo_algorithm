package main.java.components.buffers;

import java.util.Objects;

import main.java.components.units.BaseMemoryUnit;
import main.java.components.units.FunctionaUnitBroadcastInfos;
import main.java.components.units.MemoryUnitBroadcastInfos;
import main.java.instructions.Operation;

public class StoreBuffer extends Buffer {
    private String componentThatWillProduceValueToStore;
    private MemoryUnitBroadcastInfos previousInfos;
    private double valueToStore;

    public StoreBuffer(String name, BaseMemoryUnit memoryUnit) {
        super(name, Operation.STORE, memoryUnit);
    }

    @Override
    public void handleCalculatedResult(FunctionaUnitBroadcastInfos infos, double calculatedResult) {
        Objects.requireNonNull(infos);

        if (infos.getOriginStationName().equals(componentThatWillProduceValueToStore)) {
            valueToStore = calculatedResult;
            previousInfos.setDestinationRegisterValue(valueToStore);
            memoryUnit.execute(previousInfos);
        }
    }

    private void clearBuffer() {
        isBusy = false;
        valueToStore = 0;
        componentThatWillProduceValueToStore = null;
    }

    @Override
    public void handleGotMemoryData(MemoryUnitBroadcastInfos infos, double memData) {
        Objects.requireNonNull(infos);

        if (infos.getOriginBufferName().equals(name)) {
            clearBuffer();
            return;
        }

        if (infos.getDestinationRegisterNewName().get().equals(componentThatWillProduceValueToStore)) {
            valueToStore = memData;
            previousInfos.setDestinationRegisterValue(valueToStore);
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
            valueToStore = infos.getDestinationRegisterValue().get();
            infos.setDestinationRegisterValue(valueToStore);
            memoryUnit.execute(infos);
        }
    }
}

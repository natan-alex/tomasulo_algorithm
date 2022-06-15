package main.java.components.buffers;

import java.util.Objects;
import java.util.Optional;

import main.java.components.units.BaseMemoryUnit;
import main.java.components.units.FunctionaUnitBroadcastInfos;
import main.java.components.units.MemoryUnitBroadcastInfos;
import main.java.instructions.Operation;

public class LoadBuffer extends Buffer {
    public LoadBuffer(String name, BaseMemoryUnit memoryUnit) {
        super(name, Operation.LOAD, memoryUnit);
    }

    public String getName() {
        return name;
    }

    public int getAddress() {
        return address;
    }

    @Override
    public void storeInfosAndSendToMemoryUnit(MemoryUnitBroadcastInfos infos) {
        Objects.requireNonNull(infos);

        isBusy = true;
        address = infos.getAddress();

        memoryUnit.execute(infos);
    }

    @Override
    public void handleCalculatedResult(FunctionaUnitBroadcastInfos infos, double calculatedResult) {
    }

    @Override
    public void handleGotMemoryData(MemoryUnitBroadcastInfos infos, Optional<Double> memData) {
        Objects.requireNonNull(infos);

        if (infos.getOriginBufferName().equals(name)) {
            isBusy = false;
            address = 0;
        }
    }
}

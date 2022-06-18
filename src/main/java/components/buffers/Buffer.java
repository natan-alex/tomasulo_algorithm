package main.java.components.buffers;

import java.util.Objects;

import main.java.components.busses.BusObserver;
import main.java.components.units.BaseMemoryUnit;
import main.java.components.units.MemoryUnitBroadcastInfos;
import main.java.instructions.Operation;

public abstract class Buffer implements BusObserver {
    protected final String name;
    protected final BaseMemoryUnit memoryUnit;
    protected final Operation operation;
    protected int address;
    protected boolean isBusy;

    public Buffer(String name, Operation operation, BaseMemoryUnit memoryUnit) {
        if (operation == null || !operation.isLoadOrStore()) {
            throw new IllegalArgumentException("Invalid buffer operation");
        }

        this.name = Objects.requireNonNull(name);
        this.memoryUnit = Objects.requireNonNull(memoryUnit);
        this.isBusy = false;
        this.address = 0;
        this.operation = operation;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public boolean isBusy() {
        return isBusy;
    }

    public void setBusy(boolean isBusy) {
        this.isBusy = isBusy;
    }

    public String getName() {
        return name;
    }

    public abstract void storeInfosAndSendToMemoryUnit(MemoryUnitBroadcastInfos infos);

    public Operation getOperation() {
        return operation;
    }
}

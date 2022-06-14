package main.java.components.busses;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import main.java.components.units.FunctionaUnitBroadcastInfos;
import main.java.components.units.MemoryUnitBroadcastInfos;

public abstract class DataBus {
    private final List<BusObserver> observers;

    public DataBus() {
        this.observers = new ArrayList<>();
    }

    public void addObserver(BusObserver observer) {
        observers.add(Objects.requireNonNull(observer));
    }

    public void notifyObserversWith(FunctionaUnitBroadcastInfos infos, double calculatedResult) {
        Objects.requireNonNull(infos);

        for (var observer : observers) {
            observer.handleCalculatedResult(infos, calculatedResult);
        }
    }

    public void notifyObserversWith(MemoryUnitBroadcastInfos infos, double memData) {
        Objects.requireNonNull(infos);

        for (var observer : observers) {
            observer.handleGotMemoryData(infos, memData);
        }
    }
}
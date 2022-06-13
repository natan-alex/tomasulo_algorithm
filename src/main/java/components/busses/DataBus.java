package main.java.components.busses;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import main.java.components.stations.StationStorableInfos;

public abstract class DataBus {
    private final List<BusObserver> observers;

    public DataBus() {
        this.observers = new ArrayList<>();
    }

    public void addObserver(BusObserver observer) {
        observers.add(Objects.requireNonNull(observer));
    }

    public void notifyObserversWith(StationStorableInfos infos, double calculatedResult) {
        Objects.requireNonNull(infos);

        for (var observer : observers) {
            observer.handleCalculatedResult(infos, calculatedResult);
        }
    }
}
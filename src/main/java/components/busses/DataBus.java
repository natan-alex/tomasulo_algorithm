package main.java.components.busses;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class DataBus {
    private final List<BusObserver> observers;

    public DataBus() {
        this.observers = new ArrayList<>();
    }

    public void addObserver(BusObserver observer) {
        observers.add(Objects.requireNonNull(observer));
    }

    public void notifyObserversWith(double value, String destinationRegisterName) {
        Objects.requireNonNull(destinationRegisterName);

        for (var observer : observers) {
            observer.reactToBroadcastedValue(value, destinationRegisterName);
        }
    }
}
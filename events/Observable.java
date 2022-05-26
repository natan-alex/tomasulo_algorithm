package main.java.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Observable {
    protected abstract void taskCompleted();

    private final List<Observer> observers;

    public Observable() {
        observers = new ArrayList<>();
    }

    public void addObserver(Observer observer) {
        Objects.requireNonNull(observer);
        observers.add(observer);
    }

    public void taskHasBeenCompleted() {
        taskCompleted();

        for (var observer : observers) {
            observer.reactToUpdate();
        }
    }
}

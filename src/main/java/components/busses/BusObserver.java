package main.java.components.busses;

public interface BusObserver {
    void reactToBroadcastedValue(double value, String destinationRegisterName);
}

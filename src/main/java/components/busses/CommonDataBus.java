package main.java.components.busses;

import java.util.Objects;

import main.java.components.stations.StationStorableInfos;

public class CommonDataBus extends DataBus {
    @Override
    public synchronized void notifyObserversWith(StationStorableInfos infos, double calculatedResult) {
        Objects.requireNonNull(infos);

        System.out.println("LOG from common data bus:\n\tBroadcasting calculated value << " + calculatedResult + " >>");

        super.notifyObserversWith(infos, calculatedResult);
    }
}

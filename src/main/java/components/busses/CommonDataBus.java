package main.java.components.busses;

import java.util.Objects;

import main.java.components.units.FunctionaUnitBroadcastInfos;

public class CommonDataBus extends DataBus {
    @Override
    public synchronized void notifyObserversWith(FunctionaUnitBroadcastInfos infos, double calculatedResult) {
        Objects.requireNonNull(infos);

        System.out.println("LOG from COMMON DATA BUS:"
                + "\n\tBroadcasting calculated value << " + calculatedResult + " >>");

        super.notifyObserversWith(infos, calculatedResult);
    }
}

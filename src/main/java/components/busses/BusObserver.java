package main.java.components.busses;

import main.java.components.units.FunctionaUnitBroadcastInfos;

public interface BusObserver {
    void handleCalculatedResult(FunctionaUnitBroadcastInfos infos, double calculatedResult);
}

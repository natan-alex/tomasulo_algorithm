package main.java.components.busses;

import main.java.components.stations.StationStorableInfos;

public interface BusObserver {
    void handleCalculatedResult(StationStorableInfos infos, double calculatedResult);
}

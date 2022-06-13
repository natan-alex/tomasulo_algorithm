package main.java.components.stations;

import main.java.components.busses.BusObserver;

public interface Station extends BusObserver {
    void storeInfosAndTryDispatch(StationStorableInfos info);
    
    boolean isBusy();

    String getName();
}

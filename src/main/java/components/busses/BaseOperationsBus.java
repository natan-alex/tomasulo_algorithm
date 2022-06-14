package main.java.components.busses;

import main.java.components.stations.Station;
import main.java.instructions.Operation;

public interface BaseOperationsBus<T extends Number> {
    void storeOperationInStationAndMarkItBusy(Station<T> station, Operation operation);
}

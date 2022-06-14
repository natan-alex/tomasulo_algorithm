package main.java.components.busses;

import java.util.Objects;

import main.java.components.stations.Station;
import main.java.instructions.Operation;

public class OperationsBus implements BaseOperationsBus<Double> {
    public OperationsBus() {
    }

    @Override
    public void storeOperationInStationAndMarkItBusy(Station<Double> station, Operation operation) {
        Objects.requireNonNull(station);
        Objects.requireNonNull(operation);

        station.setOperation(operation);
        station.setBusy(true);
    }
}

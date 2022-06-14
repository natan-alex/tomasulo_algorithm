package main.java.components.busses;

import main.java.components.stations.Station;

public interface BaseOperandBusses<T extends Number> {
    void fetchOperandValuesIntoStation(
            String firstOperandName,
            String secondOperandName,
            Station<T> station);
}

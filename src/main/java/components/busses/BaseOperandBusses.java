package main.java.components.busses;

import main.java.components.stations.StationInstructionAndControlInfos;

public interface BaseOperandBusses<T extends Number> {
    void storeInfosInStation(StationInstructionAndControlInfos infos, String stationName);
}

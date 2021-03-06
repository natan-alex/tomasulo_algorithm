package main.java.components.busses;

import java.util.Optional;

import main.java.components.units.FunctionaUnitBroadcastInfos;
import main.java.components.units.MemoryUnitBroadcastInfos;

public interface BusObserver {
    void handleCalculatedResult(FunctionaUnitBroadcastInfos infos, double calculatedResult);

    void handleGotMemoryData(MemoryUnitBroadcastInfos infos, Optional<Double> memData);
}

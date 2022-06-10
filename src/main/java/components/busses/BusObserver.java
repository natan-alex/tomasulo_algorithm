package main.java.components.busses;

import main.java.instructions.RTypeInstruction;

public interface BusObserver {
    void reactToBroadcastedFinishedInstruction(RTypeInstruction instruction);
}

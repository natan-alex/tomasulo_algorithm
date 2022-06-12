package main.java.components.busses;

import main.java.instructions.RTypeInstruction;

public interface BusObserver {
    void handleFinishedInstruction(RTypeInstruction instruction);
}

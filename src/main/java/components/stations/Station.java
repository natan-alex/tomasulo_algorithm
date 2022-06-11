package main.java.components.stations;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import main.java.components.busses.BusObserver;
import main.java.instructions.RTypeInstruction;

public interface Station extends BusObserver {
    void storeInstructionAndTryDispatch(
            RTypeInstruction instruction,
            Optional<String> firstOperandNewName,
            Optional<String> secondOperandNewName,
            CountDownLatch countDownLatch);
    
    boolean isBusy();

    String getName();
}

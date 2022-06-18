package main.java.components.busses;

import java.util.Optional;

import main.java.instructions.Operation;

public interface BaseOperationsBus<T extends Number> {
    Optional<String> tryStoreOperationInAFreeStationAndMarkItBusy(Operation operation);
}

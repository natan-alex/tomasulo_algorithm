package main.java.components.registers;

import java.util.Optional;

import main.java.components.busses.BusObserver;

public interface BaseReorderBuffer extends BusObserver {
    void renameRegister(String registerName, String newName);

    Optional<String> getNewNameForRegister(String registerName);
}

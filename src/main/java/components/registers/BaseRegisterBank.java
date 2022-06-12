package main.java.components.registers;

import java.util.Optional;

import main.java.components.busses.BusObserver;

public interface BaseRegisterBank<T extends Number> extends BusObserver {
    void setValueForRegister(String registerName, T value);

    Optional<T> getRegisterValue(String registerName);

    void setRandomValuesInRegisters();

    Register<T>[] getAllRegisters();
}

package main.java.components.registers;

import java.util.Optional;

public interface BaseRegisterBank<T extends Number> {
    void setValueForRegister(String registerName, T value);

    Optional<T> getRegisterValue(String registerName);

    void setRandomValuesInRegisters();

    String[] getRegisterNames();
}

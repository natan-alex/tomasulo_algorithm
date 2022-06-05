package main.java.components.registers;

import java.util.Optional;

public interface RegisterManager {
    void setValueForRegister(int index, double value) throws IndexOutOfBoundsException;

    Optional<Double> getValueFromRegister(int index) throws IndexOutOfBoundsException;
}
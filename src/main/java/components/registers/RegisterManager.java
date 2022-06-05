package main.java.components.registers;

import java.util.Optional;

public interface RegisterManager {
    void setValueForRegister(int index, Number value) throws IndexOutOfBoundsException;

    Optional<Number> getValueFromRegister(int index) throws IndexOutOfBoundsException;
}

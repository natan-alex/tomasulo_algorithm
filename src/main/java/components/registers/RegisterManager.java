package main.java.components.registers;

import java.util.Optional;

public interface RegisterManager {
    void setValueForRegister(String registerName, double value);

    void markRegisterAsWaitingResult(String registerName, String reservationStationName);

    Optional<Double> getValueFromRegister(String registerName);

    boolean isRegisterWaiting(String registerName);

    Optional<String> getStationThatWillProduceValueFor(String registerName);
}
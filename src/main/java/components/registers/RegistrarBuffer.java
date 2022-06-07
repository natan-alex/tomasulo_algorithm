package main.java.components.registers;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class RegistrarBuffer {
    public static final String REGISTER_NAME_PREFIX = "F";

    private final Map<FPRegister, Optional<String>> registersAndStations;

    public RegistrarBuffer(int numberOfRegisters) {
        if (numberOfRegisters <= 0) {
            throw new IllegalArgumentException("The number of registers must be positive and greather than 0");
        }

        registersAndStations = createAndInitRegisters(numberOfRegisters);
    }

    public void setRandomValuesInRegisters() {
        var random = new Random();

        for (var register : registersAndStations.keySet()) {
            register.value = random.nextDouble() * 5;
        }
    }

    private static Map<FPRegister, Optional<String>> createAndInitRegisters(int numberOfRegisters) {
        var registers = new HashMap<FPRegister, Optional<String>>(numberOfRegisters);

        for (int i = 0; i < numberOfRegisters; i++) {
            registers.put(
                new FPRegister(REGISTER_NAME_PREFIX + i),
                Optional.<String>empty()
            );
        }

        return registers;
    }

    private Map.Entry<FPRegister, Optional<String>> getRegisterWithNameOrThrow(String name) {
        var register = registersAndStations.entrySet().stream()
            .filter(e -> e.getKey().name.equalsIgnoreCase(name))
            .findFirst();

        return register.orElseThrow();
    }

    public void setValueForRegister(String registerName, double value) {
        var register = getRegisterWithNameOrThrow(registerName);

        register.getKey().value = value;
    }

    public void markRegisterAsWaitingResult(String registerName, String reservationStationName) {
        Objects.requireNonNull(reservationStationName);

        var register = getRegisterWithNameOrThrow(registerName);

        register.setValue(Optional.of(reservationStationName));
    }

    public Optional<Double> getValueFromRegister(String registerName) {
        var register = getRegisterWithNameOrThrow(registerName);

        if (register.getValue().isPresent()) {
            return Optional.empty();
        }

        return Optional.of(register.getKey().value);
    }

    public boolean isRegisterWaiting(String registerName) {
        return getRegisterWithNameOrThrow(registerName)
            .getValue()
            .isPresent();
    }

    public Optional<String> getStationThatWillProduceValueFor(String registerName) {
        return getRegisterWithNameOrThrow(registerName).getValue();
    }
}

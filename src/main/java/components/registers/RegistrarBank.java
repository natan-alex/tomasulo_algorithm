package main.java.components.registers;

import java.util.Arrays;
import java.util.Random;

public class RegistrarBank {
    public static final String REGISTER_NAME_PREFIX = "F";

    private final FPRegister[] registers;

    public RegistrarBank(int numberOfRegisters) {
        if (numberOfRegisters <= 0) {
            throw new IllegalArgumentException("The number of registers must be positive and greather than 0");
        }

        registers = new FPRegister[numberOfRegisters];

        initRegisters();
    }

    private void initRegisters() {
        for (int i = 0; i < registers.length; i++) {
            registers[i] = new FPRegister(REGISTER_NAME_PREFIX + i);
        }
    }

    public void setRandomValuesInRegisters() {
        var random = new Random();

        for (var register : registers) {
            register.setValue((random.nextDouble() + 5) % 20);
        }
    }

    public String[] getRegistrarNames() {
        return Arrays.stream(registers)
                .map(r -> r.getName())
                .toArray(String[]::new);
    }

    private FPRegister getRegisterWithNameOrThrow(String name) {
        var result = Arrays.stream(registers)
                .filter(e -> e.getName().equalsIgnoreCase(name))
                .findFirst();

        return result.orElseThrow();
    }

    public void setValueForRegister(String registerName, double value) {
        var register = getRegisterWithNameOrThrow(registerName);

        register.setValue(value);
    }

    public double getRegisterValue(String registerName) {
        var register = getRegisterWithNameOrThrow(registerName);

        return register.getValue();
    }
}

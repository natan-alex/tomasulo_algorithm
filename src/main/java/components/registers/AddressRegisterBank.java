package main.java.components.registers;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class AddressRegisterBank implements BaseRegisterBank<Integer> {
    public static final String REGISTER_NAME_PREFIX = "R";

    private final Register<Integer>[] registers;

    public AddressRegisterBank(int numberOfRegisters) {
        if (numberOfRegisters <= 0) {
            throw new IllegalArgumentException("The number of registers must be positive and greather than 0");
        }

        registers = new AddressRegister[numberOfRegisters];

        initRegisters();
    }

    private void initRegisters() {
        System.out.println("LOG from address register bank:");
        System.out.print("\tAll registers: ");

        for (int i = 0; i < registers.length; i++) {
            registers[i] = new AddressRegister(REGISTER_NAME_PREFIX + i);

            System.out.print(registers[i].getName() + "  ");
        }

        System.out.println();
    }

    @Override
    public void setRandomValuesInRegisters() {
        System.out.println("LOG from address register bank:");
        System.out.println("\tSetting values for registers:");

        var random = new Random();

        for (var register : registers) {
            var value = random.nextInt(50 - 1) + 1;
            register.setValue(value);
            System.out.println("\t\t" + register.getName() + " -> " + register.getValue().get());
        }
    }

    @Override
    public String[] getRegisterNames() {
        return Arrays.stream(registers)
            .map(r -> r.getName())
            .toArray(String[]::new);
    }

    private Optional<Register<Integer>> getRegisterWithName(String name) {
        return Arrays.stream(registers)
                .filter(e -> e.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public void setValueForRegister(String registerName, Integer value) {
        Objects.requireNonNull(value);

        var optional = getRegisterWithName(registerName);

        var register = optional.orElseThrow();

        register.setValue(value);
    }

    @Override
    public Optional<Integer> getRegisterValue(String registerName) {
        var optional = getRegisterWithName(registerName);

        var register = optional.orElseThrow();

        return register.getValue();
    }
}

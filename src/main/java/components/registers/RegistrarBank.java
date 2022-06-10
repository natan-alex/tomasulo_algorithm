package main.java.components.registers;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import main.java.components.busses.BusObserver;
import main.java.instructions.RTypeInstruction;

public class RegistrarBank implements BusObserver {
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
        System.out.println("LOG from registrar bank:");
        System.out.print("\tAll registers: ");

        for (int i = 0; i < registers.length; i++) {
            registers[i] = new FPRegister(REGISTER_NAME_PREFIX + i);
            System.out.print(registers[i].getName() + "  ");
        }

        System.out.println();
    }

    public void setRandomValuesInRegisters() {
        System.out.println("LOG from registrar bank:");
        System.out.println("\tSetting values for registers:");

        var random = new Random();

        for (var register : registers) {
            register.setValue(5 + 10 * random.nextDouble());
            System.out.println("\t\t" + register.getName() + " -> " + register.getValue().get());
        }
    }

    public String[] getRegistrarNames() {
        return Arrays.stream(registers)
                .map(r -> r.getName())
                .toArray(String[]::new);
    }

    private Optional<FPRegister> getRegisterWithName(String name) {
        return Arrays.stream(registers)
                .filter(e -> e.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public void setValueForRegister(String registerName, double value) {
        var optional = getRegisterWithName(registerName);

        var register = optional.orElseThrow();

        register.setValue(value);
    }

    public Optional<Double> getRegisterValue(String registerName) {
        var optional = getRegisterWithName(registerName);

        var register = optional.orElseThrow();

        return register.getValue();
    }

    @Override
    public void reactToBroadcastedFinishedInstruction(RTypeInstruction instruction) {
        Objects.requireNonNull(instruction);

        var destinationRegister = instruction.getDestination();
        var destinationRegisterValue = destinationRegister.getValue().orElseThrow();

        var optional = getRegisterWithName(destinationRegister.getName());

        if (optional.isPresent()) {
            var register = optional.get();
            register.setValue(destinationRegisterValue);

            System.out.println("LOG from registrar bank:");
            System.out.print("\tUsing broadcasted value << " + destinationRegisterValue + " >>");
            System.out.println(" to set value for << " + register.getName() + " >> .");
        }
    }
}

package main.java.components.registers;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import main.java.components.units.FunctionaUnitBroadcastInfos;
import main.java.components.units.MemoryUnitBroadcastInfos;

public class FPRegisterBank implements BaseRegisterBankObserver<Double> {
    public static final String REGISTER_NAME_PREFIX = "F";

    private final Register<Double>[] registers;

    public FPRegisterBank(int numberOfRegisters) {
        if (numberOfRegisters <= 0) {
            throw new IllegalArgumentException("The number of registers must be positive and greather than 0");
        }

        registers = new FPRegister[numberOfRegisters];

        initRegisters();
    }

    private void initRegisters() {
        System.out.println("LOG from fp register bank:");
        System.out.print("\tAll registers: ");

        for (int i = 0; i < registers.length; i++) {
            registers[i] = new FPRegister(REGISTER_NAME_PREFIX + i);

            System.out.print(registers[i].getName() + "  ");
        }

        System.out.println();
    }

    @Override
    public void setRandomValuesInRegisters() {
        System.out.println("LOG from fp register bank:");
        System.out.println("\tSetting values for registers:");

        var random = new Random();

        for (var register : registers) {
            register.setValue(5 + 10 * random.nextDouble());
            System.out.println("\t\t" + register.getName() + " -> " + register.getValue().get());
        }
    }

    @Override
    public String[] getRegisterNames() {
        return Arrays.stream(registers)
                .map(r -> r.getName())
                .toArray(String[]::new);
    }

    private Optional<Register<Double>> getRegisterWithName(String name) {
        return Arrays.stream(registers)
                .filter(e -> e.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public void setValueForRegister(String registerName, Double value) {
        Objects.requireNonNull(value);

        var optional = getRegisterWithName(registerName);

        var register = optional.orElseThrow();

        register.setValue(value);
    }

    @Override
    public Optional<Double> getRegisterValue(String registerName) {
        var optional = getRegisterWithName(registerName);

        var register = optional.orElseThrow();

        return register.getValue();
    }

    @Override
    public void handleCalculatedResult(FunctionaUnitBroadcastInfos infos, double calculatedResult) {
        Objects.requireNonNull(infos);

        var optional = getRegisterWithName(infos.getDestinationRegisterName());

        if (optional.isPresent()) {
            var register = optional.get();
            register.setValue(calculatedResult);

            System.out.println("LOG from fp register bank:"
                    + "\n\tUsing broadcasted value << " + calculatedResult + " >>"
                    + " from station << " + infos.getOriginStationName() + " >>"
                    + " to set value for << " + register.getName() + " >>");
        }
    }

    @Override
    public void handleGotMemoryData(MemoryUnitBroadcastInfos infos, double memData) {
        Objects.requireNonNull(infos);

        var optional = getRegisterWithName(infos.getDestinationRegisterName());

        if (optional.isPresent()) {
            var register = optional.get();
            register.setValue(memData);

            System.out.println("LOG from fp register bank:"
                    + "\n\tUsing broadcasted value << " + memData + " >>"
                    + " from memory to set value for << " + register.getName() + " >>");
        }
    }
}

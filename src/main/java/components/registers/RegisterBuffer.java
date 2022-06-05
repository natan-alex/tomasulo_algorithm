package main.java.components.registers;

import java.util.Optional;

public class RegisterBuffer implements RegisterManager {
    public static final String REGISTER_NAME_PREFIX = "F";

    private final FPRegister[] registers;

    public RegisterBuffer(int numberOfRegisters) {
        if (numberOfRegisters <= 0) {
            throw new IllegalArgumentException("The number of registers must be positive and greather than 0");
        }

        registers = createAndInitRegisters(numberOfRegisters);
    }

    private static FPRegister[] createAndInitRegisters(int numberOfRegisters) {
        var registers = new FPRegister[numberOfRegisters];

        for (int i = 0; i < numberOfRegisters; i++) {
            registers[i] = new FPRegister(REGISTER_NAME_PREFIX + (i + 1));
        }

        return registers;
    }

    @Override
    public void setValueForRegister(int index, Number value) throws IndexOutOfBoundsException {
        validateIndex(index);
        registers[index].value = value;
    }

    @Override
    public Optional<Number> getValueFromRegister(int index) throws IndexOutOfBoundsException {
        validateIndex(index);
        return Optional.ofNullable(registers[index].value);
    }
    
    private void validateIndex(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index > registers.length) {
            throw new IndexOutOfBoundsException("The index " + index + " is not valid");
        }
    }
}

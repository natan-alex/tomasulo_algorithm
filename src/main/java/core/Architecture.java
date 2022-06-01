package main.java.core;

import java.util.Objects;

import main.java.config.Config;
import main.java.core.components.FPRegister;

public class Architecture {
    private static final String REGISTER_PREFIX = "R";

    public final InstructionQueue instructionQueue;
    public final FPRegister[] registers;

    public Architecture(Config config) throws Exception {
        Objects.requireNonNull(config);

        instructionQueue = new InstructionQueue(config.instructionQueueLength);
        registers = createAndInitRegisters(config.numberOfFloatingPointRegisters);
    }

    private FPRegister[] createAndInitRegisters(int numberOfRegisters) {
        var registers = new FPRegister[numberOfRegisters];

        for (int i = 0; i < numberOfRegisters; i++) {
            registers[i] = new FPRegister(REGISTER_PREFIX + (i + 1));
        }

        return registers;
    }
}

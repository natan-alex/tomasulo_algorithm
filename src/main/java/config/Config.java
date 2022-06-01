package main.java.config;

public class Config {
    public final int instructionQueueLength;
    public final int numberOfFloatingPointRegisters;
    public final int numberOfLoadBuffers;
    public final int numberOfStoreBuffers;
    public final int numberOfAddStations;
    public final int numberOfMulStations;

    public Config(
        int instructionQueueLength, 
        int numberOfFloatingPointRegisters,
        int numberOfLoadBuffers,
        int numberOfStoreBuffers,
        int numberOfAddStations,
        int numberOfMulStations
    ) {
        this.instructionQueueLength = requirePositiveNumber(instructionQueueLength, "instructionQueueLength");
        this.numberOfFloatingPointRegisters = requirePositiveNumber(numberOfFloatingPointRegisters, "numberOfFloatingPointRegisters");
        this.numberOfStoreBuffers = requirePositiveNumber(numberOfStoreBuffers, "numberOfStoreBuffers");
        this.numberOfLoadBuffers = requirePositiveNumber(numberOfLoadBuffers, "numberOfLoadBuffers");
        this.numberOfAddStations = requirePositiveNumber(numberOfAddStations, "numberOfAddStations");
        this.numberOfMulStations = requirePositiveNumber(numberOfMulStations, "numberOfMulStations");
    }

    private static int requirePositiveNumber(int number, String argName) {
        if (number < 0) {
            throw new IllegalArgumentException(argName + " cannot be negative");
        }
        return number;
    }
}

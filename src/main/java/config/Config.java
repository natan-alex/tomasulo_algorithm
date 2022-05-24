package main.java.config;

public class Config {
    public final long instructionQueueLength;
    public final long numberOfFloatingPointRegisters;
    public final long numberOfLoadBuffers;
    public final long numberOfStoreBuffers;
    public final long numberOfAddStations;
    public final long numberOfMulStations;

    public Config(
        long instructionQueueLength, 
        long numberOfFloatingPointRegisters,
        long numberOfLoadBuffers,
        long numberOfStoreBuffers,
        long numberOfAddStations,
        long numberOfMulStations
    ) {
        this.instructionQueueLength = requirePositiveNumber(instructionQueueLength, "instructionQueueLength");
        this.numberOfFloatingPointRegisters = requirePositiveNumber(numberOfFloatingPointRegisters, "numberOfFloatingPointRegisters");
        this.numberOfStoreBuffers = requirePositiveNumber(numberOfStoreBuffers, "numberOfStoreBuffers");
        this.numberOfLoadBuffers = requirePositiveNumber(numberOfLoadBuffers, "numberOfLoadBuffers");
        this.numberOfAddStations = requirePositiveNumber(numberOfAddStations, "numberOfAddStations");
        this.numberOfMulStations = requirePositiveNumber(numberOfMulStations, "numberOfMulStations");
    }

    private static long requirePositiveNumber(long number, String argName) {
        if (number < 0) {
            throw new IllegalArgumentException(argName + " cannot be negative");
        }
        return number;
    }
}

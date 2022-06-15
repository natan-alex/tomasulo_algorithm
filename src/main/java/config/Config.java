package main.java.config;

public class Config {
    public final int numberOfFloatingPointRegisters;
    public final int numberOfLoadBuffers;
    public final int numberOfStoreBuffers;
    public final int numberOfAddStations;
    public final int numberOfMulStations;
    public final int numberOfAddressRegisters;
    public final int cyclesToPerformAnAdd;
    public final int cyclesToPerformASub;
    public final int cyclesToPerformAMul;
    public final int cyclesToPerformADiv;
    public final int cyclesToPerformALoad;
    public final int cyclesToPerformAStore;

    public Config(
            int numberOfFloatingPointRegisters,
            int numberOfAddressRegisters,
            int numberOfLoadBuffers,
            int numberOfStoreBuffers,
            int numberOfAddStations,
            int numberOfMulStations,
            int cyclesToPerformAnAdd,
            int cyclesToPerformASub,
            int cyclesToPerformAMul,
            int cyclesToPerformADiv,
            int cyclesToPerformALoad,
            int cyclesToPerformAStore) {
        this.numberOfFloatingPointRegisters = requirePositiveNumber(numberOfFloatingPointRegisters,
                "numberOfFloatingPointRegisters");
        this.numberOfAddressRegisters = requirePositiveNumber(numberOfAddressRegisters, "numberOfAddressRegisters");
        this.numberOfStoreBuffers = requirePositiveNumber(numberOfStoreBuffers, "numberOfStoreBuffers");
        this.numberOfLoadBuffers = requirePositiveNumber(numberOfLoadBuffers, "numberOfLoadBuffers");
        this.numberOfAddStations = requirePositiveNumber(numberOfAddStations, "numberOfAddStations");
        this.numberOfMulStations = requirePositiveNumber(numberOfMulStations, "numberOfMulStations");
        this.cyclesToPerformAnAdd = requirePositiveNumber(cyclesToPerformAnAdd, "cyclesToPerformAnAdd");
        this.cyclesToPerformASub = requirePositiveNumber(cyclesToPerformASub, "cyclesToPerformASub");
        this.cyclesToPerformAMul = requirePositiveNumber(cyclesToPerformAMul, "cyclesToPerformAMul");
        this.cyclesToPerformADiv = requirePositiveNumber(cyclesToPerformADiv, "cyclesToPerformADiv");
        this.cyclesToPerformALoad = requirePositiveNumber(cyclesToPerformALoad, "cyclesToPerformALoad");
        this.cyclesToPerformAStore = requirePositiveNumber(cyclesToPerformAStore, "cyclesToPerformAStore");
    }

    private static int requirePositiveNumber(int number, String argName) {
        if (number < 0) {
            throw new IllegalArgumentException(argName + " cannot be negative");
        }
        return number;
    }
}

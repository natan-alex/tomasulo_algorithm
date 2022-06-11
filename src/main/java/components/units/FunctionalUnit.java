package main.java.components.units;

import java.util.Objects;

import main.java.components.busses.DataBus;
import main.java.instructions.RTypeInstruction;

public abstract class FunctionalUnit {
    private final String name;
    private DataBus commonDataBus;
    private int cyclesRequiredToPerformOperation;

    public FunctionalUnit(String unitName, int cyclesRequiredToPerformOperation, DataBus commonDataBus) {
        if (cyclesRequiredToPerformOperation <= 0) {
            throw new IllegalArgumentException("Number of cycles must be greather than 0.");
        }

        this.name = Objects.requireNonNull(unitName);
        this.cyclesRequiredToPerformOperation = cyclesRequiredToPerformOperation;
        this.commonDataBus = Objects.requireNonNull(commonDataBus);
    }

    public abstract void calculateResultFor(RTypeInstruction instruction);

    public void execute(RTypeInstruction instruction) {
        Objects.requireNonNull(instruction);

        new Thread(() -> {
            System.out.println("LOG from " + name + ":");
            System.out.println("\tCalculating result...");

            trySleep(cyclesRequiredToPerformOperation * 1000);
            calculateResultFor(instruction);

            var result = instruction.getDestination().getValue().get();
            System.out.println("LOG from " + name + " unit:");
            System.out.println("\tSending result << " + result + " >> to common data bus.");

            commonDataBus.notifyObserversWith(instruction);
            System.out.println("finishing thread " + Thread.currentThread().getName());
        }).start();
    }

    private static void trySleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

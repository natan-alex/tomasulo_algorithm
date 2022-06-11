package main.java.components.units;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import main.java.components.busses.DataBus;
import main.java.instructions.RTypeInstruction;

public abstract class FunctionalUnit {
    private final String name;
    private final DataBus commonDataBus;
    private final int timeToCalculateResult;

    public FunctionalUnit(
        String unitName, 
        int cyclesRequiredToPerformOperation, 
        DataBus commonDataBus
    ) {
        if (cyclesRequiredToPerformOperation <= 0) {
            throw new IllegalArgumentException("Number of cycles must be greather than 0.");
        }

        this.name = Objects.requireNonNull(unitName);
        this.timeToCalculateResult = cyclesRequiredToPerformOperation * 1000;
        this.commonDataBus = Objects.requireNonNull(commonDataBus);
    }

    public abstract void calculateResultFor(RTypeInstruction instruction);

    public void execute(RTypeInstruction instruction, CountDownLatch countDownLatch) {
        Objects.requireNonNull(instruction);
        Objects.requireNonNull(countDownLatch);

        new Thread(() -> {
            System.out.println("LOG from " + name + " unit:\n\tCalculating result for instruction << " + instruction + " >>");

            trySleep(timeToCalculateResult);
            calculateResultFor(instruction);

            var result = instruction.getDestination().getValue().orElseThrow();
            System.out.println("LOG from " + name + " unit:\n\tSending result << " + result + " >> to common data bus");

            commonDataBus.notifyObserversWith(instruction);
            countDownLatch.countDown();
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

package main.java.components.units;

import java.util.Objects;

import main.java.components.busses.DataBus;

public abstract class FunctionalUnit {
    private final String name;
    private final DataBus commonDataBus;
    private final int timeToCalculateResult;

    private Thread thread;

    public FunctionalUnit(
            String unitName,
            int cyclesRequiredToPerformOperation,
            DataBus commonDataBus) {
        if (cyclesRequiredToPerformOperation <= 0) {
            throw new IllegalArgumentException("Number of cycles must be greather than 0.");
        }

        this.name = Objects.requireNonNull(unitName);
        this.timeToCalculateResult = cyclesRequiredToPerformOperation * 1000;
        this.commonDataBus = Objects.requireNonNull(commonDataBus);
    }

    public abstract double calculateResultFor(FunctionaUnitBroadcastInfos infos);

    private void waitIfAlive() {
        if (thread != null && thread.isAlive()) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void execute(FunctionaUnitBroadcastInfos infos) {
        Objects.requireNonNull(infos);

        waitIfAlive();

        thread = new Thread(() -> {
            System.out.println("LOG from " + name + " unit:\n\tCalculating result for << " + infos.getOperation() + " "
                    + infos.getFirstOperandName() + " " + infos.getSecondOperandName() + " >>");

            var result = calculateResultFor(infos);
            trySleep(timeToCalculateResult);

            System.out.println("LOG from " + name + " unit:\n\tSending result << " + result + " >> to common data bus");

            infos.getCountDownLatch().countDown();
            commonDataBus.notifyObserversWith(infos, result);
        });

        thread.start();
    }

    private static void trySleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

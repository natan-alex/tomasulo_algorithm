package main.java.components.units;

import java.util.Objects;

import main.java.components.busses.DataBus;
import main.java.instructions.Operation;

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

    public abstract Operation[] getAllowedOperations();

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
            var instruction = infos.getOperation() + " "
                    + infos.getDestinationRegisterName() + " "
                    + infos.getFirstOperandName() + " "
                    + infos.getSecondOperandName();

            System.out.println("LOG from " + name + " UNIT:"
                    + "\n\tCalculating result for instruction << " + instruction + " >>");

            var result = calculateResultFor(infos);
            trySleep(timeToCalculateResult);

            System.out.println("LOG from " + name + " UNIT:"
                    + "\n\tSending result << " + result + " >> to common data bus");

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

package main.java.components.units;

import java.util.Objects;
import java.util.Random;

import main.java.components.busses.DataBus;
import main.java.instructions.Operation;

public class MemoryUnit implements BaseMemoryUnit {
    public static final String NAME = "Memory unit";
    private final DataBus commonDataBus;
    private final int timeToCalculateResult;

    private Thread thread;

    public MemoryUnit(DataBus commonDataBus) {
        // if (cyclesRequiredToPerformOperation <= 0) {
        // throw new IllegalArgumentException("Number of cycles must be greather than
        // 0.");
        // }

        this.timeToCalculateResult = 3 * 1000;
        this.commonDataBus = Objects.requireNonNull(commonDataBus);
    }

    private void waitIfAlive() {
        if (thread != null && thread.isAlive()) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void execute(MemoryUnitBroadcastInfos infos) {
        Objects.requireNonNull(infos);

        waitIfAlive();

        thread = new Thread(() -> {
            if (infos.getOperation() == Operation.STORE) {
                runStoreOperationCode(infos);
            } else {
                runLoadOperationCode(infos);
            }
        });

        thread.start();
    }

    private void runStoreOperationCode(MemoryUnitBroadcastInfos infos) {
        System.out.println("LOG from memory unit:"
                + "\n\tStoring value in memory in address << " + infos.getAddress() + " >>");

        trySleep(timeToCalculateResult);

        System.out.println("LOG from memory unit:"
                + "\n\tValue was stored in memory, telling common data bus");

        infos.getCountDownLatch().countDown();
        commonDataBus.notifyObserversWith(infos, 0);
    }

    private void runLoadOperationCode(MemoryUnitBroadcastInfos infos) {
        System.out.println("LOG from memory unit:"
                + "\n\tGetting value from memory in address << " + infos.getAddress() + " >>");

        var result = getRandomValueInRange(1, 50);
        trySleep(timeToCalculateResult);

        System.out.println("LOG from memory unit:"
                + "\n\tSending result << " + result + " >> to common data bus");

        infos.getCountDownLatch().countDown();
        commonDataBus.notifyObserversWith(infos, result);
    }

    private double getRandomValueInRange(double min, double max) {
        return min + new Random().nextDouble() * ((max - min) + 1);
    }

    private static void trySleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

package main.java.components.units;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import main.java.components.busses.DataBus;
import main.java.instructions.Operation;

public class MemoryUnit implements BaseMemoryUnit {
    public static final String NAME = "MEMORY UNIT";
    private final DataBus commonDataBus;
    private final int timeToPerformALoad;
    private final int timeToPerformAStore;

    public MemoryUnit(DataBus commonDataBus, int cyclesToPerformALoad, int cyclesToPerformAStore) {
        if (cyclesToPerformALoad <= 0 || cyclesToPerformAStore <= 0) {
            throw new IllegalArgumentException("Number of cycles must be greather than 0.");
        }

        this.timeToPerformALoad = cyclesToPerformALoad * 1000;
        this.timeToPerformAStore = cyclesToPerformAStore * 1000;
        this.commonDataBus = Objects.requireNonNull(commonDataBus);
    }

    @Override
    public void execute(MemoryUnitBroadcastInfos infos) {
        Objects.requireNonNull(infos);

        new Thread(() -> {
            if (infos.getOperation() == Operation.STORE) {
                executeStoreOperationRelatedCode(infos);
            } else {
                executeLoadOperationRelatedCode(infos);
            }
        }).start();
    }

    private void executeStoreOperationRelatedCode(MemoryUnitBroadcastInfos infos) {
        System.out.println("LOG from " + NAME + ":"
                + "\n\tStoring value << " + infos.getDestinationRegisterValue().get() + " >>"
                + " in memory address << " + infos.getAddress() + " >>");

        trySleep(timeToPerformAStore);

        System.out.println("LOG from " + NAME + ":"
                + "\n\tValue << " + infos.getDestinationRegisterValue().get() + " >>"
                + " got from << " + infos.getDestinationRegisterName() + " >>"
                + " was stored in memory, telling common data bus");

        infos.getCountDownLatch().countDown();
        commonDataBus.notifyObserversWith(infos, Optional.empty());
    }

    private void executeLoadOperationRelatedCode(MemoryUnitBroadcastInfos infos) {
        System.out.println("LOG from " + NAME + ":"
                + "\n\tGetting value from memory in address << " + infos.getAddress() + " >>"
                + " to store in register << " + infos.getDestinationRegisterName() + " >>");

        var result = 1 + new Random().nextDouble() * 50;

        trySleep(timeToPerformALoad);

        System.out.println("LOG from " + NAME + ":"
                + "\n\tSending result << " + result + " >> to common data bus");

        infos.getCountDownLatch().countDown();
        commonDataBus.notifyObserversWith(infos, Optional.of(result));
    }

    private static void trySleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

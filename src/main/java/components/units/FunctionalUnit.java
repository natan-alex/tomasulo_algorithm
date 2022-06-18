package main.java.components.units;

import java.util.Objects;

import main.java.components.busses.DataBus;
import main.java.instructions.Operation;

public abstract class FunctionalUnit {
    private final String name;
    private final DataBus commonDataBus;

    public FunctionalUnit(
            String unitName,
            DataBus commonDataBus) {
        this.name = Objects.requireNonNull(unitName);
        this.commonDataBus = Objects.requireNonNull(commonDataBus);
    }

    public abstract double calculateResultFor(FunctionaUnitBroadcastInfos infos);

    public abstract Operation[] getAllowedOperations();

    protected abstract int getCyclesToPerformOperation(Operation operation);

    public void execute(FunctionaUnitBroadcastInfos infos) {
        Objects.requireNonNull(infos);

        new Thread(() -> {
            var instruction = infos.getOperation() + " "
                    + infos.getDestinationRegisterName() + " "
                    + infos.getFirstOperandName() + " "
                    + infos.getSecondOperandName();

            System.out.println("LOG from " + name + " UNIT:"
                    + "\n\tCalculating result for instruction << " + instruction + " >>");

            var result = calculateResultFor(infos);
            var timeToPerformOperation = getCyclesToPerformOperation(infos.getOperation()) * 1000;
            trySleep(timeToPerformOperation);

            System.out.println("LOG from " + name + " UNIT:"
                    + "\n\tSending result << " + result + " >> to common data bus");

            infos.getCountDownLatch().countDown();
            commonDataBus.notifyObserversWith(infos, result);
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

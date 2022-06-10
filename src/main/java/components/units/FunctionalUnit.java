package main.java.components.units;

import java.util.Objects;

import main.java.components.busses.DataBus;

public abstract class FunctionalUnit {
    private Thread thread;

    private DataBus commonDataBus;
    private int cyclesRequiredToPerformOperation;

    public FunctionalUnit(int cyclesRequiredToPerformOperation, DataBus commonDataBus) {
        if (cyclesRequiredToPerformOperation <= 0) {
            throw new IllegalArgumentException("Number of cycles must be greather than 0.");
        }

        this.cyclesRequiredToPerformOperation = cyclesRequiredToPerformOperation;
        this.commonDataBus = Objects.requireNonNull(commonDataBus);
    }

    public abstract double calculateResult(double firstOperand, double secondOperand);

    protected abstract String getUnitName();

    private void trySleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void waitThreadIfAlive() {
        if (thread != null && thread.isAlive()) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void execute(double firstOperand, double secondOperand, String destinationRegisterName) {
        Objects.requireNonNull(destinationRegisterName);

        waitThreadIfAlive();

        thread = new Thread(() -> {
            System.out.println("LOG from " + getUnitName() + ":");
            System.out.println("\tCalculating result...");
            trySleep(cyclesRequiredToPerformOperation * 1000);

            var result = calculateResult(firstOperand, secondOperand);
            System.out.println("LOG from " + getUnitName() + ":");
            System.out.println("\tSending result << " + result + " >> to common data bus.");

            commonDataBus.notifyObserversWith(result, destinationRegisterName);
        });

        thread.run();
    }
}

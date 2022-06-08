package main.java.components.units;

public class AddFunctionalUnit implements FunctionalUnit {
    private static final int NUMBER_OF_CYCLES = 2;
    private final Thread thread;
    private double firstOperand;
    private double secondOperand;

    public AddFunctionalUnit() {
        this.thread = new Thread(() -> {
            try {
                Thread.sleep(NUMBER_OF_CYCLES * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            var result = this.firstOperand + this.secondOperand;
            // send to common data bus
            System.out.println("from thread in add unit: " + result);
        });
    }

    @Override
    public void execute(double firstOperand, double secondOperand) {
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
        thread.run();
    }
}
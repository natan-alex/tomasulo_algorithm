package main.java.components.units;

public class AddFunctionalUnit implements FunctionalUnit {
    private final Thread thread;
    private double firstOperand;
    private double secondOperand;

    public AddFunctionalUnit() {
        this.thread = new Thread(() -> {
            var result = this.firstOperand + this.secondOperand;
            // send to common data bus
        });
    }

    @Override
    public void execute(double firstOperand, double secondOperand) {
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
        thread.run();
    }
}
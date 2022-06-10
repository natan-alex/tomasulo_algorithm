package main.java.components.units;

import main.java.components.busses.DataBus;

public class AddFunctionalUnit extends FunctionalUnit {
    public AddFunctionalUnit(DataBus dataBus) {
        super(2, dataBus);
    }

    @Override
    public double calculateResult(double firstOperand, double secondOperand) {
        return firstOperand + secondOperand;
    }

    @Override
    protected String getUnitName() {
        return "ADD unit";
    }
}
package main.java.components.units;

import java.util.Objects;

import main.java.components.busses.DataBus;
import main.java.instructions.Operation;

public class MulFunctionalUnit extends FunctionalUnit {
    private static final Operation[] ALLOWED_OPERATIONS = new Operation[] { Operation.MUL, Operation.DIV };

    public MulFunctionalUnit(
            String unitName,
            DataBus dataBus) {
        super(unitName, 4, dataBus);
    }

    @Override
    public double calculateResultFor(FunctionaUnitBroadcastInfos infos) {
        Objects.requireNonNull(infos);

        var operation = infos.getOperation();
        var firstOperandValue = infos.getFirstOperandValue();
        var secondOperandValue = infos.getSecondOperandValue();

        if (operation == Operation.MUL) {
            return firstOperandValue * secondOperandValue;
        } else if (operation == Operation.DIV) {
            return firstOperandValue / secondOperandValue;
        }

        var message = "Illegal operation " + operation.getRepresentation() + " for a MUL unit.";
        throw new IllegalArgumentException(message);
    }

    @Override
    public Operation[] getAllowedOperations() {
        return ALLOWED_OPERATIONS;
    }
}

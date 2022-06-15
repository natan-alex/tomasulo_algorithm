package main.java.components.units;

import java.util.Objects;

import main.java.components.busses.DataBus;
import main.java.instructions.Operation;

public class AddFunctionalUnit extends FunctionalUnit {
    private static final Operation[] ALLOWED_OPERATIONS = new Operation[] { Operation.ADD, Operation.SUB };

    private final int cyclesToPerformAnAdd;
    private final int cyclesToPerformASub;

    public AddFunctionalUnit(
            String unitName,
            int cyclesToPerformAnAdd,
            int cyclesToPerformASub,
            DataBus dataBus) {
        super(unitName, dataBus);
        this.cyclesToPerformAnAdd = cyclesToPerformAnAdd;
        this.cyclesToPerformASub = cyclesToPerformASub;
    }

    @Override
    public double calculateResultFor(FunctionaUnitBroadcastInfos infos) {
        Objects.requireNonNull(infos);

        var operation = infos.getOperation();
        var firstOperandValue = infos.getFirstOperandValue();
        var secondOperandValue = infos.getSecondOperandValue();

        if (operation == Operation.ADD) {
            return firstOperandValue + secondOperandValue;
        } else if (operation == Operation.SUB) {
            return firstOperandValue - secondOperandValue;
        }

        var message = "Illegal operation " + operation.getRepresentation() + " for an ADD unit.";
        throw new IllegalArgumentException(message);
    }

    @Override
    public Operation[] getAllowedOperations() {
        return ALLOWED_OPERATIONS;
    }

    @Override
    protected int getCyclesToPerformOperation(Operation operation) {
        if (operation == null || !operation.isAddOrSub()) {
            throw new IllegalArgumentException("Illegal operation for add unit");
        }

        return operation == Operation.ADD ? cyclesToPerformAnAdd : cyclesToPerformASub;
    }
}

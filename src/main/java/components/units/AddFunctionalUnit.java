package main.java.components.units;

import java.util.Objects;

import main.java.components.busses.DataBus;
import main.java.components.stations.StationStorableInfos;
import main.java.instructions.Operation;

public class AddFunctionalUnit extends FunctionalUnit {
    public AddFunctionalUnit(
        String unitName, 
        DataBus dataBus
    ) {
        super(unitName, 2, dataBus);
    }

    @Override
    public double calculateResultFor(StationStorableInfos infos) {
        Objects.requireNonNull(infos);

        var operation = infos.getOperation();
        var firstOperandValue = infos.getFirstOperandValue().orElseThrow();
        var secondOperandValue = infos.getSecondOperandValue().orElseThrow();

        if (operation == Operation.ADD) {
            return firstOperandValue + secondOperandValue;
        } else if (operation == Operation.SUB) {
            return firstOperandValue - secondOperandValue;
        }

        var message = "Illegal operation " + operation.getRepresentation() + " for an ADD unit.";
        throw new IllegalArgumentException(message);
    }
}

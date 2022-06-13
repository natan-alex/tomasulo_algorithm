package main.java.components.units;

import java.util.Objects;

import main.java.components.busses.DataBus;
import main.java.components.stations.StationStorableInfos;
import main.java.instructions.Operation;
import main.java.instructions.RTypeInstruction;

public class MulFunctionalUnit extends FunctionalUnit {
    public MulFunctionalUnit(
        String unitName, 
        DataBus dataBus
    ) {
        super(unitName, 4, dataBus);
    }

    @Override
    public double calculateResultFor(StationStorableInfos infos) {
        Objects.requireNonNull(infos);

        var operation = infos.getOperation();
        var firstOperandValue = infos.getFirstOperandValue().orElseThrow();
        var secondOperandValue = infos.getSecondOperandValue().orElseThrow();

        if (operation == Operation.MUL) {
            return firstOperandValue * secondOperandValue;
        } else if (operation == Operation.DIV) {
            return firstOperandValue / secondOperandValue;
        }

        var message = "Illegal operation " + operation.getRepresentation() + " for a MUL unit.";
        throw new IllegalArgumentException(message);
    }
}

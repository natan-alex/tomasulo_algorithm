package main.java.components.units;

import java.util.Objects;

import main.java.components.busses.DataBus;
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
    public void calculateResultFor(RTypeInstruction instruction) {
        Objects.requireNonNull(instruction);

        var operation = instruction.getOperation();
        var destinationRegister = instruction.getDestination();
        var firstOperandValue = instruction.getFirstOperand().getValue().orElseThrow();
        var secondOperandValue = instruction.getSecondOperand().getValue().orElseThrow();

        if (operation == Operation.MUL) {
            destinationRegister.setValue(firstOperandValue * secondOperandValue);
        } else if (operation == Operation.DIV) {
            destinationRegister.setValue(firstOperandValue / secondOperandValue);
        } else {
            var message = "Illegal operation " + operation.getRepresentation() + " for a MUL unit.";
            throw new IllegalArgumentException(message);
        }
    }
}
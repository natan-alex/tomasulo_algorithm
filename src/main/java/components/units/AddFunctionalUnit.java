package main.java.components.units;

import java.util.Objects;

import main.java.components.busses.DataBus;
import main.java.instructions.Operation;
import main.java.instructions.RTypeInstruction;

public class AddFunctionalUnit extends FunctionalUnit {
    public AddFunctionalUnit(String unitName, DataBus dataBus) {
        super(unitName, 2, dataBus);
    }

    @Override
    public void calculateResultFor(RTypeInstruction instruction) {
        Objects.requireNonNull(instruction);

        var operation = instruction.getOperation();
        var destinationRegister = instruction.getDestination();
        var firstOperandValue = instruction.getFirstOperand().getValue().orElseThrow();
        var secondOperandValue = instruction.getSecondOperand().getValue().orElseThrow();

        if (operation == Operation.ADD) {
            destinationRegister.setValue(firstOperandValue + secondOperandValue);
        } else if (operation == Operation.SUB) {
            destinationRegister.setValue(firstOperandValue - secondOperandValue);
        } else {
            var message = "Illegal operation " + operation.getRepresentation() + " for an ADD unit.";
            throw new IllegalArgumentException(message);
        }
    }
}
package main.java.components.stations;

import java.util.Objects;
import java.util.Optional;

import main.java.components.busses.BusObserver;
import main.java.components.units.FunctionalUnit;
import main.java.instructions.Operation;
import main.java.instructions.RTypeInstruction;

public class ReservationStation implements BusObserver {
    private final String name;
    private final Operation operation;
    private final FunctionalUnit relatedUnit;
    private boolean isBusy;
    private Double firstOperandValue;
    private Double secondOperandValue;
    private String firstStationThatWillProduceValue;
    private String secondStationThatWillProduceValue;
    private Object immediateOrAddress;

    public ReservationStation(String name, Operation operation, FunctionalUnit relatedUnit) {
        this.name = Objects.requireNonNull(name);
        this.operation = operation;
        this.relatedUnit = Objects.requireNonNull(relatedUnit);
        this.isBusy = false;
        this.firstOperandValue = null;
        this.secondOperandValue = null;
        this.firstStationThatWillProduceValue = null;
        this.secondStationThatWillProduceValue = null;
        this.immediateOrAddress = null;
    }

    public String getName() {
        return name;
    }

    public Operation getOperation() {
        return operation;
    }

    public boolean isBusy() {
        return isBusy;
    }

    public void storeInstruction(
            RTypeInstruction instruction,
            Optional<String> firstOperandNewName,
            Optional<String> secondOperandNewName) {
        if (isBusy) {
            throw new IllegalStateException("Trying to store instruction " + instruction + " in busy station.");
        }

        isBusy = true;

        if (firstOperandNewName.isPresent()) {
            firstStationThatWillProduceValue = firstOperandNewName.get();
        } else {
            firstOperandValue = instruction.getFirstOperand().getValue().get();
        }

        if (secondOperandNewName.isPresent()) {
            secondStationThatWillProduceValue = secondOperandNewName.get();
        } else {
            secondOperandValue = instruction.getSecondOperand().getValue().get();
        }

        runIfAllOperandsAreAvailable(instruction);
    }

    @Override
    public void reactToBroadcastedFinishedInstruction(RTypeInstruction instruction) {
        Objects.requireNonNull(instruction);

        var destinationRegister = instruction.getDestination();
        var destinationRegisterName = destinationRegister.getName();
        var destinationRegisterValue = destinationRegister.getValue().get();

        if (firstStationThatWillProduceValue != null
                && firstStationThatWillProduceValue.equals(destinationRegisterName)) {
            firstOperandValue = destinationRegisterValue;

            System.out.println("LOG from " + name + " station:");
            System.out.println("\tUsing broadcasted value " + destinationRegisterValue + " for first operand.");

            runIfAllOperandsAreAvailable(instruction);
        }

        if (secondStationThatWillProduceValue != null
                && secondStationThatWillProduceValue.equals(destinationRegisterName)) {
            secondOperandValue = destinationRegisterValue;

            System.out.println("LOG from " + name + " station:");
            System.out.println("\tUsing broadcasted value " + destinationRegisterValue + " for second operand.");

            runIfAllOperandsAreAvailable(instruction);
        }
    }

    private void runIfAllOperandsAreAvailable(RTypeInstruction instruction) {
        if (firstOperandValue != null && secondOperandValue != null) {
            System.out.println("LOG from " + name + " station:");
            System.out.print("\tAll operands available: ");
            System.out.print("<< " + firstOperandValue + " >> and ");
            System.out.print("<< " + secondOperandValue + " >> . ");
            System.out.println("Passing to functional unit.");

            relatedUnit.execute(instruction);
        }
    }
}

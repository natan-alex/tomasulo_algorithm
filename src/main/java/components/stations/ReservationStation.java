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

    private RTypeInstruction instructionBeingExecuted;

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

        this.instructionBeingExecuted = null;
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

        System.out.println("instruction " + instruction + " in " + name);

        isBusy = true;

        if (firstOperandNewName.isPresent()) {
            firstStationThatWillProduceValue = firstOperandNewName.get();
        } else {
            firstOperandValue = instruction.getFirstOperand().getValue().orElseThrow();
        }

        if (secondOperandNewName.isPresent()) {
            secondStationThatWillProduceValue = secondOperandNewName.get();
        } else {
            secondOperandValue = instruction.getSecondOperand().getValue().orElseThrow();
        }

        runIfAllOperandsAreAvailable(instruction);
    }

    @Override
    public void reactToBroadcastedFinishedInstruction(RTypeInstruction instruction) {
        Objects.requireNonNull(instruction);

        var destinationRegister = instruction.getDestination();
        var destinationRegisterName = destinationRegister.getName();
        var destinationRegisterValue = destinationRegister.getValue().orElseThrow();

        if (instructionBeingExecuted != null && instructionBeingExecuted.equals(instruction)) {
            clearStation();
            return;
        }

        if (firstStationThatWillProduceValue != null
                && firstStationThatWillProduceValue.equals(destinationRegisterName)) {
            firstOperandValue = destinationRegisterValue;
            logUsingValueForOperand(destinationRegisterValue, "first operand");
            runIfAllOperandsAreAvailable(instruction);
        }

        if (secondStationThatWillProduceValue != null
                && secondStationThatWillProduceValue.equals(destinationRegisterName)) {
            secondOperandValue = destinationRegisterValue;
            logUsingValueForOperand(destinationRegisterValue, "second operand");
            runIfAllOperandsAreAvailable(instruction);
        }
    }

    private void logUsingValueForOperand(double value, String operand) {
        System.out.println("LOG from " + name + " station:");
        System.out.println("\tUsing broadcasted value " + value + " for " + operand);
    }

    private void clearStation() {
        isBusy = false;
        firstOperandValue = null;
        secondOperandValue = null;
        firstStationThatWillProduceValue = null;
        secondStationThatWillProduceValue = null;
        instructionBeingExecuted = null;
    }

    private void runIfAllOperandsAreAvailable(RTypeInstruction instruction) {
        Objects.requireNonNull(instruction);

        if (firstOperandValue != null && secondOperandValue != null) {
            System.out.println("LOG from " + name + " station:");
            System.out.print("\tAll operands available: ");
            System.out.print("<< " + firstOperandValue + " >> and ");
            System.out.print("<< " + secondOperandValue + " >> . ");
            System.out.println("Passing to functional unit.");

            instructionBeingExecuted = instruction;
            relatedUnit.execute(instruction);
        }
    }
}
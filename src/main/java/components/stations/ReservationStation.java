package main.java.components.stations;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import main.java.components.units.FunctionalUnit;
import main.java.instructions.Operation;
import main.java.instructions.RTypeInstruction;

public class ReservationStation implements Station {
    private final String name;
    private final Operation operation;
    private final FunctionalUnit relatedUnit;
    private boolean isBusy;
    private Double firstOperandValue;
    private Double secondOperandValue;
    private String firstStationThatWillProduceValue;
    private String secondStationThatWillProduceValue;
    private String firstStationThatWillProduceValueOriginalName;
    private String secondStationThatWillProduceValueOriginalName;
    private Object immediateOrAddress;

    private RTypeInstruction instructionToExecute;
    private CountDownLatch countDownLatch;

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

        this.instructionToExecute = null;
        this.firstStationThatWillProduceValueOriginalName = null;
        this.secondStationThatWillProduceValueOriginalName = null;
    }

    @Override
    public String getName() {
        return name;
    }

    public Operation getOperation() {
        return operation;
    }

    @Override
    public boolean isBusy() {
        return isBusy;
    }

    @Override
    public void storeInstructionAndTryDispatch(
            RTypeInstruction instruction,
            Optional<String> firstOperandNewName,
            Optional<String> secondOperandNewName,
            CountDownLatch countDownLatch) {
        if (isBusy) {
            throw new IllegalStateException("Trying to store instruction " + instruction + " in busy station.");
        }

        this.instructionToExecute = Objects.requireNonNull(instruction);
        this.countDownLatch = Objects.requireNonNull(countDownLatch);
        this.isBusy = true;

        if (firstOperandNewName.isPresent()) {
            firstStationThatWillProduceValue = firstOperandNewName.get();
            firstStationThatWillProduceValueOriginalName = instruction.getFirstOperand().getName();
        } else {
            firstOperandValue = instruction.getFirstOperand().getValue().orElseThrow();
        }

        if (secondOperandNewName.isPresent()) {
            secondStationThatWillProduceValue = secondOperandNewName.get();
            secondStationThatWillProduceValueOriginalName = instruction.getSecondOperand().getName();
        } else {
            secondOperandValue = instruction.getSecondOperand().getValue().orElseThrow();
        }

        runIfAllOperandsAreAvailable();
    }

    @Override
    public void handleFinishedInstruction(RTypeInstruction instruction) {
        Objects.requireNonNull(instruction);

        var destinationRegister = instruction.getDestination();
        var destinationRegisterName = destinationRegister.getName();
        var destinationRegisterValue = destinationRegister.getValue().orElseThrow();

        if (instructionToExecute != null && instructionToExecute.equals(instruction)) {
            clearStation();
            return;
        }

        if (firstStationThatWillProduceValue != null
                && firstStationThatWillProduceValueOriginalName.equals(destinationRegisterName)) {
            System.out.println("LOG from " + name + " station:\n\tUsing broadcasted value " + destinationRegisterValue
                    + " for first operand");

            firstOperandValue = destinationRegisterValue;
            runIfAllOperandsAreAvailable();
        }

        if (secondStationThatWillProduceValue != null
                && secondStationThatWillProduceValueOriginalName.equals(destinationRegisterName)) {
            System.out.println("LOG from " + name + " station:\n\tUsing broadcasted value " + destinationRegisterValue
                    + " for second operand");

            secondOperandValue = destinationRegisterValue;
            runIfAllOperandsAreAvailable();
        }
    }

    private void clearStation() {
        isBusy = false;
        firstOperandValue = null;
        secondOperandValue = null;
        firstStationThatWillProduceValue = null;
        secondStationThatWillProduceValue = null;
        instructionToExecute = null;
        countDownLatch = null;
    }

    private void runIfAllOperandsAreAvailable() {
        if (firstOperandValue != null && secondOperandValue != null) {
            System.out.println("LOG from " + name + " station:\n\tAll operands available: << " + firstOperandValue + " >> and << " + secondOperandValue + " >>\n\tPassing to functional unit");

            relatedUnit.execute(instructionToExecute, countDownLatch);
        }
    }
}
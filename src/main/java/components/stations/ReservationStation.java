package main.java.components.stations;

import java.util.Objects;

import main.java.components.units.FunctionalUnit;
import main.java.instructions.Operation;

public class ReservationStation implements Station {
    private final String name;
    private final FunctionalUnit relatedUnit;
    private boolean isBusy;
    private Operation operation;
    private Double firstOperandValue;
    private Double secondOperandValue;
    private String firstStationThatWillProduceValue;
    private String secondStationThatWillProduceValue;
    private Object immediateOrAddress;

    private StationStorableInfos previouslyStoredInfos;

    public ReservationStation(String name, FunctionalUnit relatedUnit) {
        this.name = Objects.requireNonNull(name);
        this.relatedUnit = Objects.requireNonNull(relatedUnit);
        this.isBusy = false;
        this.operation = null;
        this.firstOperandValue = null;
        this.secondOperandValue = null;
        this.firstStationThatWillProduceValue = null;
        this.secondStationThatWillProduceValue = null;
        this.immediateOrAddress = null;
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
    public void storeInfosAndTryDispatch(StationStorableInfos infos) {
        if (isBusy) {
            throw new IllegalStateException("Trying to store instruction in busy station.");
        }

        this.isBusy = true;
        this.operation = infos.getOperation();
        this.previouslyStoredInfos = Objects.requireNonNull(infos);

        if (infos.getFirstOperandNewName().isPresent()) {
            firstStationThatWillProduceValue = infos.getFirstOperandNewName().get();
        } else {
            firstOperandValue = infos.getFirstOperandValue().orElseThrow();
            runIfAllOperandsAreAvailable();
        }

        if (infos.getSecondOperandNewName().isPresent()) {
            secondStationThatWillProduceValue = infos.getSecondOperandNewName().get();
        } else {
            secondOperandValue = infos.getSecondOperandValue().orElseThrow();
            runIfAllOperandsAreAvailable();
        }
    }

    @Override
    public void handleCalculatedResult(StationStorableInfos infos, double calculatedResult) {
        Objects.requireNonNull(infos);

        if (name.equals(infos.getOriginStationName())) {
            clearStation();
            return;
        }
        
        if (firstStationThatWillProduceValue != null && 
            firstStationThatWillProduceValue.equals(infos.getOriginStationName())) {
            System.out.println("LOG from " + name + " station:\n\tUsing broadcasted value " + calculatedResult + " for first operand");
            firstOperandValue = calculatedResult;
            runIfAllOperandsAreAvailable();
        }

        if (secondStationThatWillProduceValue != null && 
            secondStationThatWillProduceValue.equals(infos.getOriginStationName())) {
            System.out.println("LOG from " + name + " station:\n\tUsing broadcasted value " + calculatedResult + " for second operand");
            secondOperandValue = calculatedResult;
            runIfAllOperandsAreAvailable();
        }
    }

    private void clearStation() {
        isBusy = false;
        firstOperandValue = null;
        secondOperandValue = null;
        firstStationThatWillProduceValue = null;
        secondStationThatWillProduceValue = null;
        operation = null;
        immediateOrAddress = null;
        previouslyStoredInfos = null;
    }

    private void runIfAllOperandsAreAvailable() {
        if (firstOperandValue != null && secondOperandValue != null) {
            System.out.println("LOG from " + name + " station:\n\tAll operands available: << " + firstOperandValue + " >> and << " + secondOperandValue + " >>\n\tPassing to functional unit");

            relatedUnit.execute(previouslyStoredInfos);
        }
    }
}
package main.java.components.stations;

import java.util.Objects;

import main.java.components.units.FunctionaUnitBroadcastInfos;
import main.java.components.units.FunctionalUnit;
import main.java.components.units.MemoryUnitBroadcastInfos;

public class ReservationStation extends Station<Double> {
    private StationInstructionAndControlInfos previousInfos;

    public ReservationStation(String name, FunctionalUnit unit) {
        super(name, unit);
    }

    @Override
    public void dispatchStoredInfosToUnitIfPossibleWith(StationInstructionAndControlInfos infos) {
        this.previousInfos = Objects.requireNonNull(infos);

        if (firstOperandValue != null && secondOperandValue != null) {
            System.out.println("LOG from " + name + " station:"
                    + "\n\tAll operands available: << " + firstOperandValue + " >> "
                    + "and << " + secondOperandValue + " >>\n\t"
                    + "Passing to functional unit");

            relatedUnit.execute(new FunctionaUnitBroadcastInfos(
                    name,
                    operation,
                    infos.getDestinationRegisterName(),
                    infos.getFirstOperandName(),
                    firstOperandValue,
                    infos.getSecondOperandName(),
                    secondOperandValue,
                    infos.getCountDownLatch()));
        }
    }

    @Override
    public void handleCalculatedResult(FunctionaUnitBroadcastInfos infos, double calculatedResult) {
        Objects.requireNonNull(infos);

        if (name.equals(infos.getOriginStationName())) {
            clearStation();
            return;
        }

        if (stationThatWillProduceValueForFirstOperand != null &&
                stationThatWillProduceValueForFirstOperand.equals(infos.getOriginStationName())) {
            System.out.println("LOG from " + name + " station:\n\tUsing broadcasted value " + calculatedResult
                    + " for first operand");

            firstOperandValue = calculatedResult;
            dispatchStoredInfosToUnitIfPossibleWith(previousInfos);
        }

        if (stationThatWillProduceValueForSecondOperand != null &&
                stationThatWillProduceValueForSecondOperand.equals(infos.getOriginStationName())) {
            System.out.println("LOG from " + name + " station:\n\tUsing broadcasted value " + calculatedResult
                    + " for second operand");

            secondOperandValue = calculatedResult;
            dispatchStoredInfosToUnitIfPossibleWith(previousInfos);
        }
    }

    private void clearStation() {
        isBusy = false;
        firstOperandValue = null;
        secondOperandValue = null;
        stationThatWillProduceValueForFirstOperand = null;
        stationThatWillProduceValueForSecondOperand = null;
        operation = null;
        immediateOrAddress = null;
    }

    @Override
    public void handleGotMemoryData(MemoryUnitBroadcastInfos infos, double memData) {
        // do nothing
    }
}
package main.java.components.stations;

import java.util.Objects;
import java.util.Optional;

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
            System.out.println("LOG from " + name + " STATION:"
                    + "\n\tAll operands available: << " + firstOperandValue + " >> and << " + secondOperandValue + " >>"
                    + "\n\tPassing to functional unit");

            relatedUnit.execute(new FunctionaUnitBroadcastInfos(
                    name,
                    operationBeingExecuted,
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
            System.out.println("LOG from " + name + " STATION:"
                    + "\n\tUsing broadcasted value << " + calculatedResult + " >> for first operand");

            firstOperandValue = calculatedResult;
            dispatchStoredInfosToUnitIfPossibleWith(previousInfos);
        }

        if (stationThatWillProduceValueForSecondOperand != null &&
                stationThatWillProduceValueForSecondOperand.equals(infos.getOriginStationName())) {
            System.out.println("LOG from " + name + " STATION:"
                    + "\n\tUsing broadcasted value << " + calculatedResult + " >> for second operand");

            secondOperandValue = calculatedResult;
            dispatchStoredInfosToUnitIfPossibleWith(previousInfos);
        }
    }

    @Override
    public void handleGotMemoryData(MemoryUnitBroadcastInfos infos, Optional<Double> memData) {
        Objects.requireNonNull(infos);

        if (stationThatWillProduceValueForFirstOperand != null &&
                stationThatWillProduceValueForFirstOperand.equals(infos.getOriginBufferName())) {
            System.out.println("LOG from " + name + " STATION:"
                    + "\n\tUsing broadcasted value << " + memData.get() + " >> for first operand");

            firstOperandValue = memData.get();
            dispatchStoredInfosToUnitIfPossibleWith(previousInfos);
        }

        if (stationThatWillProduceValueForSecondOperand != null &&
                stationThatWillProduceValueForSecondOperand.equals(infos.getOriginBufferName())) {
            System.out.println("LOG from " + name + " STATION:"
                    + "\n\tUsing broadcasted value << " + memData.get() + " >> for second operand");

            secondOperandValue = memData.get();
            dispatchStoredInfosToUnitIfPossibleWith(previousInfos);
        }
    }

    private void clearStation() {
        isBusy = false;
        firstOperandValue = null;
        secondOperandValue = null;
        stationThatWillProduceValueForFirstOperand = null;
        stationThatWillProduceValueForSecondOperand = null;
        operationBeingExecuted = null;
        immediateOrAddress = null;
    }
}
package main.java.components.busses;

import java.util.Objects;

import main.java.components.registers.BaseRegisterBank;
import main.java.components.registers.BaseReorderBuffer;
import main.java.components.stations.Station;

public class OperandBusses implements BaseOperandBusses<Double> {
    private final BaseRegisterBank<Double> registerBank;
    private final BaseReorderBuffer reorderBuffer;

    public OperandBusses(
            BaseRegisterBank<Double> registerBank,
            BaseReorderBuffer reorderBuffer) {
        this.registerBank = Objects.requireNonNull(registerBank);
        this.reorderBuffer = Objects.requireNonNull(reorderBuffer);
    }

    @Override
    public void fetchOperandValuesIntoStation(
            String firstOperandName,
            String secondOperandName,
            Station<Double> station) {
        Objects.requireNonNull(firstOperandName);
        Objects.requireNonNull(secondOperandName);
        Objects.requireNonNull(station);

        solveFirstOperand(firstOperandName, station);
        solveSecondOperand(secondOperandName, station);
    }

    private void solveFirstOperand(String firstOperandName, Station<Double> station) {
        var newNameForFirstOperand = reorderBuffer.getNewNameForRegister(firstOperandName);

        if (newNameForFirstOperand.isPresent()) {
            station.setStationThatWillProduceValueForFirstOperand(newNameForFirstOperand.get());
        } else {
            var value = registerBank.getRegisterValue(firstOperandName).orElseThrow();
            station.setFirstOperandValue(value);
        }
    }

    private void solveSecondOperand(String secondOperandName, Station<Double> station) {
        var newNameForSecondOperand = reorderBuffer.getNewNameForRegister(secondOperandName);

        if (newNameForSecondOperand.isPresent()) {
            station.setStationThatWillProduceValueForSecondOperand(newNameForSecondOperand.get());
        } else {
            var value = registerBank.getRegisterValue(secondOperandName).orElseThrow();
            station.setSecondOperandValue(value);
        }
    }
}

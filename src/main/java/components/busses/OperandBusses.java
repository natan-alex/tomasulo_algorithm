package main.java.components.busses;

import java.util.Arrays;
import java.util.Objects;

import main.java.components.registers.BaseRegisterBank;
import main.java.components.registers.BaseReorderBuffer;
import main.java.components.stations.Station;
import main.java.components.stations.StationInstructionAndControlInfos;

public class OperandBusses implements BaseOperandBusses<Double> {
    private final Station<Double>[] stations;
    private final BaseRegisterBank<Double> registerBank;
    private final BaseReorderBuffer reorderBuffer;

    public OperandBusses(
            Station<Double>[] stations,
            BaseRegisterBank<Double> registerBank,
            BaseReorderBuffer reorderBuffer) {
        this.stations = Objects.requireNonNull(stations);
        this.registerBank = Objects.requireNonNull(registerBank);
        this.reorderBuffer = Objects.requireNonNull(reorderBuffer);
    }

    @Override
    public void storeInfosInStation(StationInstructionAndControlInfos infos, String stationName) {
        Objects.requireNonNull(infos);

        var station = Arrays.stream(stations)
                .filter(s -> s.getName().equalsIgnoreCase(stationName))
                .findFirst()
                .orElseThrow();

        reorderBuffer.renameRegister(infos.getDestinationRegisterName(), stationName);
        solveFirstOperand(infos.getFirstOperandName(), station);
        solveSecondOperand(infos.getSecondOperandName(), station);
        station.dispatchStoredInfosToUnitIfPossibleWith(infos);
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

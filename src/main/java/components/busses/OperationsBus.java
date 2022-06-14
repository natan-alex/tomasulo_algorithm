package main.java.components.busses;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import main.java.components.stations.Station;
import main.java.instructions.Operation;

public class OperationsBus implements BaseOperationsBus<Double> {
    private final Station<Double>[] stations;

    public OperationsBus(Station<Double>[] stations) {
        this.stations = Objects.requireNonNull(stations);
    }

    @Override
    public Optional<String> storeOperationInStationAndMarkItBusy(Operation operation) {
        Objects.requireNonNull(operation);

        var optional = Arrays.stream(stations)
                .filter(s -> !s.isBusy())
                .findFirst();

        if (optional.isEmpty()) {
            return Optional.empty();
        }

        var station = optional.get();
        station.setOperation(operation);
        station.setBusy(true);

        return Optional.of(station.getName());
    }
}

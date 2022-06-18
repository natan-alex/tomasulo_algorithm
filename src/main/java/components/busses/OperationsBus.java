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
    public Optional<String> tryStoreOperationInAFreeStationAndMarkItBusy(Operation operation) {
        Objects.requireNonNull(operation);

        var optional = Arrays.stream(stations)
                .filter(s -> !s.isBusy() && s.isOperationAllowed(operation))
                .findFirst();

        if (optional.isEmpty()) {
            System.out.println("All reservation stations are busy :(");
            return Optional.empty();
        }

        var station = optional.get();
        station.setOperationBeingExecuted(operation);
        station.setBusy(true);

        System.out.println("LOG from OPERATIONS BUS:"
                + "\n\tStoring operation << " + operation + " >> in station << " + station.getName()
                + " >> and marking it as busy");

        return Optional.of(station.getName());
    }
}

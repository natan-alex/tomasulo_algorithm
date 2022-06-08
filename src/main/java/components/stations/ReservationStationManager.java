package main.java.components.stations;

import java.util.Arrays;
import java.util.Optional;

import main.java.instructions.Operation;

public class ReservationStationManager {
    private final ReservationStation[] addReservationStations;
    private final ReservationStation[] mulReservationStations;

    public ReservationStationManager(int numberOfAddStations, int numberOfMulStations) {
        if (numberOfAddStations <= 0 || numberOfMulStations <= 0) {
            throw new IllegalArgumentException("The number of stations cannot be negative or equal to 0.");
        }

        addReservationStations = new ReservationStation[numberOfAddStations];
        mulReservationStations = new ReservationStation[numberOfMulStations];

        initReservationStations();
    }

    private void initReservationStations() {
        for (int i = 0; i < addReservationStations.length; i++) {
            addReservationStations[i] = new ReservationStation(
                Operation.ADD.representation + i,
                Operation.ADD
            );
        }
        for (int i = 0; i < mulReservationStations.length; i++) {
            mulReservationStations[i] = new ReservationStation(
                Operation.MUL.representation + i,
                Operation.MUL
            );
        }
    }

    public void showStations() {
        for (var reservationStation : addReservationStations) {
            System.out.println(reservationStation.isBusy());
            System.out.println(reservationStation.getFirstOperandValue());
            System.out.println(reservationStation.getFirstStationThatWillProduceValue());
            System.out.println(reservationStation.getSecondOperandValue());
            System.out.println(reservationStation.getSecondStationThatWillProduceValue());
            System.out.println();
        }
    }

    public Optional<ReservationStation> getNotBusyStationForOperation(Operation operation) {
        var stationsToLookIn = addReservationStations;

        if (operation.isMulOrDiv()) {
            stationsToLookIn = mulReservationStations;
        }

        return Arrays.stream(stationsToLookIn)
            .filter(s -> !s.isBusy())
            .findFirst();
    }
}
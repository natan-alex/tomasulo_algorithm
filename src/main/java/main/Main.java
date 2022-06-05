package main.java.main;

import main.java.config.ConfigParser;
import main.java.instructions.Operation;
import main.java.instructions.RTypeInstruction;
import main.java.components.Architecture;
import main.java.components.registers.FPRegister;

public class Main {
    public static void main(String[] args) throws Exception {
        var config = ConfigParser.parse();

        var architecture = new Architecture(config);

        var r0 = new FPRegister("F0");
        var r1 = new FPRegister("F1");
        var r2 = new FPRegister("F2");

        var i0 = new RTypeInstruction(
            Operation.ADD,
            r0, r1, r2
        );

        var i1 = new RTypeInstruction(
            Operation.ADD,
            r1, r0, r2
        );

        architecture.schedule(i0);
        architecture.schedule(i1);

        architecture.startExecution();

        architecture.showReservationStations();
    }
}

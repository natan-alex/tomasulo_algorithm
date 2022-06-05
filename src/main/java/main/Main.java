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

        var r0 = new FPRegister("R0");
        var r1 = new FPRegister("R1");
        var r2 = new FPRegister("R2");

        var instruction = new RTypeInstruction(
            Operation.ADD,
            r0, r1, r2
        );

        architecture.schedule(instruction);

        architecture.startExecution();
    }
}

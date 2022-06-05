package main.java.main;

import main.java.config.ConfigParser;
import main.java.instructions.RTypeInstruction;
import main.java.components.registers.FPRegister;

public class Main {
    public static void main(String[] args) throws Exception {
        var config = ConfigParser.parse();

        var r1 = new FPRegister("R1");
        var r2 = new FPRegister("R2");
        var r3 = new FPRegister("R3");

        var instruction = new RTypeInstruction(
            RTypeInstruction.Operation.ADD,
            r1, r2, r3
        );
    }
}

package main.java.main;

import main.java.config.ConfigParser;
import main.java.core.Architecture;
import main.java.core.InstructionQueue;
import main.java.core.components.FPRegister;
import main.java.core.instructions.Operation;
import main.java.core.instructions.RTypeInstruction;
import main.java.config.Config;

public class Main {
    public static void main(String[] args) throws Exception {
        FPRegister r1 = new FPRegister("R1");
        FPRegister r2 = new FPRegister("R2");
        FPRegister destination = new FPRegister("D");

        RTypeInstruction instruction = new RTypeInstruction(
            Operation.ADD, 
            destination,
            r1,
            r2
        );

        InstructionQueue queue = new InstructionQueue(4);
        queue.addInstruction(instruction);

        instruction.taskHasBeenCompleted();


        Config config = ConfigParser.parse();

        var architecture = new Architecture(config);
    }
}

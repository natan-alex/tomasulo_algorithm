package main.java.main;

import main.java.config.ConfigParser;
import main.java.core.FPRegister;
import main.java.core.InstructionQueue;
import main.java.core.Operation;
import main.java.core.RTypeInstruction;
import main.java.config.Config;

public class Main {
    public static void main(String[] args) throws Exception {
        Config config = ConfigParser.parse();

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
    }
}

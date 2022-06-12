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
        var registers = architecture.getAllRegisters();

        var r0 = new FPRegister(registers[0].getName());
        var r1 = new FPRegister(registers[1].getName());
        var r2 = new FPRegister(registers[2].getName());
        var r3 = new FPRegister(registers[3].getName());
        var r4 = new FPRegister(registers[4].getName());
        var r5 = new FPRegister(registers[5].getName());
        var r6 = new FPRegister(registers[6].getName());

        var i0 = new RTypeInstruction(
                Operation.ADD,
                r0, r1, r2);

        var i1 = new RTypeInstruction(
                Operation.ADD,
                r1, r0, r2);

        var i2 = new RTypeInstruction(
                Operation.SUB,
                r2, r0, r1);

        var i3 = new RTypeInstruction(
                Operation.MUL,
                r1, r0, r2);

        var i4 = new RTypeInstruction(
                Operation.MUL,
                r4, r5, r6);

        architecture.schedule(i0);
        architecture.schedule(i1);
        architecture.schedule(i2);
        architecture.schedule(i3);
        architecture.schedule(i4);

        architecture.startExecution();
    }
}

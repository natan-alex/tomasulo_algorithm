package main.java.main;

import main.java.config.ConfigParser;
import main.java.instructions.MemoryTypeInstruction;
import main.java.instructions.Operation;
import main.java.instructions.RTypeInstruction;
import main.java.components.registers.AddressRegister;
import main.java.components.registers.FPRegister;

public class Main {
    public static void main(String[] args) throws Exception {
        var config = ConfigParser.parse();
        var architecture = new Architecture(config);

        var fpRegisters = architecture.getFPRegisterNames();
        var addressRegisters = architecture.getAddressRegisterNames();

        var r0 = new FPRegister(fpRegisters[0]);
        var r1 = new FPRegister(fpRegisters[1]);
        var r2 = new FPRegister(fpRegisters[2]);
        var r3 = new FPRegister(fpRegisters[3]);
        var r4 = new FPRegister(fpRegisters[4]);
        var r5 = new FPRegister(fpRegisters[5]);
        var r6 = new FPRegister(fpRegisters[6]);

        var m0 = new AddressRegister(addressRegisters[0]);

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

        var i5 = new MemoryTypeInstruction(
                Operation.LOAD,
                r3, 30, m0);

        var i6 = new RTypeInstruction(
                Operation.ADD,
                r4, r3, r0);

        var i7 = new MemoryTypeInstruction(
                Operation.STORE,
                r3, 50, m0);

        architecture.schedule(i0);
        architecture.schedule(i1);
        architecture.schedule(i2);
        architecture.schedule(i3);
        architecture.schedule(i4);
        architecture.schedule(i5);
        architecture.schedule(i6);
        architecture.schedule(i7);

        architecture.startExecution();
    }
}

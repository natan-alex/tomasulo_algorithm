package main.java.components.busses;

import java.util.Objects;

import main.java.instructions.RTypeInstruction;

public class CommonDataBus extends DataBus {
    public CommonDataBus() {
    }

    public synchronized void broadcastFinishedInstruction(RTypeInstruction instruction) {
        Objects.requireNonNull(instruction);

        var destinationRegister = instruction.getDestination();
        var destinationRegisterValue = destinationRegister.getValue().orElseThrow();

        System.out.println("LOG from common data bus:");
        System.out.print("\tBroadcasting instruction << " + instruction + " >>");
        System.out.println(" with calculated value << " + destinationRegisterValue + " >> .");

        super.notifyObserversWith(instruction);
    }
}

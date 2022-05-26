package main.java.core;

import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import main.java.events.Observer;

public class InstructionQueue implements Observer {
    private final Queue<RTypeInstruction> instructions;

    public InstructionQueue(int queueLength) throws Exception {
        if (queueLength <= 0) {
            throw new Exception("Invalid queue length: must be positive and greater than 0");
        }

        instructions = new LinkedBlockingQueue<RTypeInstruction>(queueLength);
    }

    public void addInstruction(RTypeInstruction instruction) {
        Objects.requireNonNull(instruction);
        instruction.addObserver(this);
        instructions.add(instruction);
    }

    @Override
    public void reactToUpdate() {
        System.out.println("schedule next instruction");
    }
}

package main.java.components;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;

import main.java.instructions.RTypeInstruction;

public class InstructionQueue implements Iterable<RTypeInstruction> {
    private final Queue<RTypeInstruction> instructions;
    private int size;

    public InstructionQueue() {
        instructions = new ArrayDeque<>();
        size = 0;
    }

    public void enqueue(RTypeInstruction instruction) {
        Objects.requireNonNull(instruction);
        instructions.add(instruction);
        size++;
    }

    public Optional<RTypeInstruction> dispatch() {
        var polled = instructions.poll();

        if (polled == null) {
            return Optional.empty();
        }

        size--;
        return Optional.of(polled);
    }

    public int size() {
        return size;
    }

    @Override
    public Iterator<RTypeInstruction> iterator() {
        return instructions.iterator();
    }
}

package main.java.components;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;

import main.java.instructions.RTypeInstruction;

public class InstructionQueue {
    private final Queue<RTypeInstruction> instructions;

    public InstructionQueue(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length cannot negative or equal to 0.");
        }

        instructions = new ArrayDeque<>(length);
    }

    public void enqueue(RTypeInstruction instruction) {
        Objects.requireNonNull(instruction);
        instructions.add(instruction);
    }

    public Optional<RTypeInstruction> dispatch() {
        return Optional.ofNullable(instructions.poll());
    }
}

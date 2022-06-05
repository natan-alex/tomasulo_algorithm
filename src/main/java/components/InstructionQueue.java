package main.java.components;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;

import main.java.instructions.Instruction;

public class InstructionQueue {
    private final Queue<Instruction> instructions;

    public InstructionQueue(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length cannot negative or equal to 0.");
        }

        instructions = new ArrayDeque<>(length);
    }

    public void enqueue(Instruction instruction) {
        Objects.requireNonNull(instruction);
        instructions.add(instruction);
    }

    public Optional<Instruction> dispatch() {
        return Optional.ofNullable(instructions.poll());
    }
}

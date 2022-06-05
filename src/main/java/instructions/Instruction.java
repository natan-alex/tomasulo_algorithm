package main.java.instructions;

import java.util.Optional;

import main.java.instructions.states.State;
import main.java.instructions.states.ToBeScheduled;

public abstract class Instruction {
    private State state;
    public final InstructionType type;

    public Instruction(InstructionType type) {
        this.state = new ToBeScheduled();
        this.type = type;
    }

    public void changeState() {
        state = state.nextState();
    }

    public Optional<State> getState() {
        return Optional.ofNullable(state);
    }
}

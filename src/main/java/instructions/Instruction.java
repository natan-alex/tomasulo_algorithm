package main.java.instructions;

import main.java.instructions.states.State;
import main.java.instructions.states.ToBeScheduled;

public abstract class Instruction {
    private State state;

    public final InstructionType type;
    public final Operation operation;

    public Instruction(InstructionType type, Operation operation) {
        this.state = new ToBeScheduled();
        this.type = type;
        this.operation = operation;
        checkIfInstructionAndOperationMatch();
    }

    private void checkIfInstructionAndOperationMatch() {
        if (operation.relatedInstructionType != type) {
            throw new IllegalArgumentException("Instruction type and operation do not match.");
        }
    }

    public void changeState() {
        var nextState = state.nextState();
        
        if (nextState != null) {
            state = nextState;
        }
    }

    public State getState() {
        return state;
    }
}

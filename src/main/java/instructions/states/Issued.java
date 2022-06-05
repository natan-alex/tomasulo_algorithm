package main.java.instructions.states;

public class Issued implements State {
    @Override
    public State nextState() {
        return new Executed();
    }
}

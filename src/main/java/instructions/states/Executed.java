package main.java.instructions.states;

public class Executed implements State {
    @Override
    public State nextState() {
        return new Wrote();
    }
}

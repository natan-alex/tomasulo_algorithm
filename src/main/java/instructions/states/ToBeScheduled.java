package main.java.instructions.states;

public class ToBeScheduled implements State {

    @Override
    public State nextState() {
        return new Issued();
    }
    
}

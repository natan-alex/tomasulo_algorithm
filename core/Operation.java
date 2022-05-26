package main.java.core;

public enum Operation {
    ADD("ADD"),
    SUB("SUB"),
    DIV("DIV"),
    MUL("MUL");

    public final String representation;
    
    private Operation(String representation) {
        this.representation = representation;
    }
}

package main.java.components.registers;

import java.util.Objects;

public class FPRegister {
    public final String name;
    public Number value;

    public FPRegister(String name) {
        this.name = Objects.requireNonNull(name);
        this.value = 0.0;
    }
}
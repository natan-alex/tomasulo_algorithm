package main.java.core;

import java.util.Objects;

public class FPRegister {
    private String name;
    private double value;

    public FPRegister(String name) {
        this.name = Objects.requireNonNull(name);
        this.value = 0;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void rename(String name) {
        this.name = Objects.requireNonNull(name);
    }
}

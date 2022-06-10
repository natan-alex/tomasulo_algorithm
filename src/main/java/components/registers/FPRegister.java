package main.java.components.registers;

import java.util.Objects;
import java.util.Optional;

public class FPRegister {
    private final String name;
    private Double value;

    public FPRegister(String name) {
        this.name = Objects.requireNonNull(name);
        this.value = null;
    }

    public String getName() {
        return name;
    }

    public Optional<Double> getValue() {
        return Optional.ofNullable(value);
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        FPRegister other = (FPRegister) obj;

        return name.equals(other.name);
    }
}
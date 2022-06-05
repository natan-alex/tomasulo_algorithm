package main.java.components.registers;

import java.util.Objects;

public class FPRegister {
    public final String name;
    public double value;

    public FPRegister(String name) {
        this.name = Objects.requireNonNull(name);
        this.value = 0.0;
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
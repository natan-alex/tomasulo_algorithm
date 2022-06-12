package main.java.components.registers;

import java.util.Optional;

import main.java.util.StringUtil;

public abstract class Register<T extends Number> {
    private final String name;
    private T value;

    public Register(String name) {
        if (StringUtil.isNullOrBlank(name)) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        this.name = name;
        this.value = null;
    }

    public String getName() {
        return name;
    }

    public Optional<T> getValue() {
        return Optional.ofNullable(value);
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        Register<T> other = (Register<T>) obj;

        return name.equals(other.getName());
    }
}

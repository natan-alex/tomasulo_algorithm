package main.java.components.registers;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

public class ReorderBuffer {
    private final Map<String, Optional<String>> originalAndCurrentNamesOfRegistrars;

    public ReorderBuffer(String[] registrarNames) {
        Objects.requireNonNull(registrarNames);
        this.originalAndCurrentNamesOfRegistrars = createAndInitRegistrarNamesWith(registrarNames);
    }

    private Map<String, Optional<String>> createAndInitRegistrarNamesWith(String[] names) {
        var registrarNames = new HashMap<String, Optional<String>>(names.length);

        for (int i = 0; i < names.length; i++) {
            registrarNames.put(names[i], Optional.empty());
        }

        return registrarNames;
    }

    public Optional<String> getNewNameForRegister(String registerName) {
        Objects.requireNonNull(registerName);

        if (!originalAndCurrentNamesOfRegistrars.containsKey(registerName)) {
            throw new NoSuchElementException("There is no register with name '" + registerName + "'.");
        }

        return originalAndCurrentNamesOfRegistrars.get(registerName);
    }
}

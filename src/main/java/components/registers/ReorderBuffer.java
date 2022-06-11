package main.java.components.registers;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import main.java.components.busses.BusObserver;
import main.java.instructions.RTypeInstruction;

public class ReorderBuffer implements BusObserver {
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

    private void throwIfNullOrDoNotExist(String registerName) {
        Objects.requireNonNull(registerName);

        if (!originalAndCurrentNamesOfRegistrars.containsKey(registerName)) {
            throw new NoSuchElementException("There is no register with name '" + registerName + "'.");
        }
    }

    public Optional<String> getNewNameForRegister(String registerName) {
        throwIfNullOrDoNotExist(registerName);

        return originalAndCurrentNamesOfRegistrars.get(registerName);
    }

    public void setNewNameForRegister(String registerName, String newName) {
        if (newName == null || newName.isEmpty()) {
            throw new IllegalArgumentException("Invalid new name for register: cannot be empty or null.");
        }

        throwIfNullOrDoNotExist(registerName);

        originalAndCurrentNamesOfRegistrars.put(registerName, Optional.of(newName));
    }

    @Override
    public void reactToBroadcastedFinishedInstruction(RTypeInstruction instruction) {
        Objects.requireNonNull(instruction);

        var destinationRegister = instruction.getDestination();
        var destinationRegisterName = destinationRegister.getName();

        var optional = originalAndCurrentNamesOfRegistrars.get(destinationRegisterName);

        if (optional.isPresent()) {
            System.out.println("LOG from reorder buffer:\n\tMarking << " + destinationRegisterName + " >> as if it was not renamed");

            originalAndCurrentNamesOfRegistrars.put(destinationRegisterName, Optional.empty());
        }
    }
}

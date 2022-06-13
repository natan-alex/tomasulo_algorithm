package main.java.components.registers;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import main.java.components.stations.StationStorableInfos;

public class ReorderBuffer implements BaseReorderBuffer {
    private final Map<String, Optional<String>> originalAndCurrentNamesOfRegisters;

    public ReorderBuffer(BaseRegisterBank<?> registerBank) {
        Objects.requireNonNull(registerBank);

        var registers = registerBank.getRegisterNames();
        this.originalAndCurrentNamesOfRegisters = new HashMap<>(registers.length);
        initRegisterNamesWith(registers);
    }

    private void initRegisterNamesWith(String[] registerNames) {
        for (int i = 0; i < registerNames.length; i++) {
            originalAndCurrentNamesOfRegisters.put(
                registerNames[i],
                Optional.empty());
        }
    }

    private void throwIfNullOrDoNotExist(String registerName) {
        Objects.requireNonNull(registerName);

        if (!originalAndCurrentNamesOfRegisters.containsKey(registerName)) {
            throw new NoSuchElementException("There is no register with name '" + registerName + "'.");
        }
    }

    @Override
    public Optional<String> getNewNameForRegister(String registerName) {
        throwIfNullOrDoNotExist(registerName);

        return originalAndCurrentNamesOfRegisters.get(registerName);
    }

    @Override
    public void renameRegister(String registerName, String newName) {
        if (newName == null || newName.isEmpty()) {
            throw new IllegalArgumentException("Invalid new name for register: cannot be empty or null.");
        }

        throwIfNullOrDoNotExist(registerName);

        originalAndCurrentNamesOfRegisters.put(registerName, Optional.of(newName));
    }

    @Override
    public void handleCalculatedResult(StationStorableInfos infos, double calculatedResult) {
        Objects.requireNonNull(infos);

        var destinationRegisterName = infos.getDestinationRegisterName();
        var optional = originalAndCurrentNamesOfRegisters.get(destinationRegisterName);

        if (optional.isPresent()) {
            System.out.println("LOG from reorder buffer:\n\tMarking << " + destinationRegisterName + " >> as if it was not renamed");

            originalAndCurrentNamesOfRegisters.put(destinationRegisterName, Optional.empty());
        }

    }
}

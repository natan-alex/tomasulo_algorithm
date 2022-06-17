package main.java.components.buffers;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import javax.print.attribute.standard.MediaSize.NA;

import main.java.components.registers.BaseRegisterBank;
import main.java.components.registers.BaseReorderBuffer;
import main.java.components.units.FunctionaUnitBroadcastInfos;
import main.java.components.units.MemoryUnitBroadcastInfos;

public class ReorderBuffer implements BaseReorderBuffer {
    private static final String NAME = "REORDER BUFFER";

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

        System.out.println("LOG from " + NAME + ":"
            + "\n\tRenaming register << " + registerName + " >> to << " + newName + " >>");
    }

    @Override
    public void handleCalculatedResult(FunctionaUnitBroadcastInfos infos, double calculatedResult) {
        markRegisterAsNotRenamed(infos.getDestinationRegisterName());
    }

    @Override
    public void handleGotMemoryData(MemoryUnitBroadcastInfos infos, Optional<Double> memData) {
        markRegisterAsNotRenamed(infos.getDestinationRegisterName());
    }

    private void markRegisterAsNotRenamed(String registerName) {
        var optional = originalAndCurrentNamesOfRegisters.get(registerName);

        if (optional.isPresent()) {
            System.out.println("LOG from " + NAME + ":"
                    + "\n\tMarking << " + registerName + " >>"
                    + " as if it was not renamed");

            originalAndCurrentNamesOfRegisters.put(registerName, Optional.empty());
        }
    }
}

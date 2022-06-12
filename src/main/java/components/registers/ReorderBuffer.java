package main.java.components.registers;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import main.java.instructions.RTypeInstruction;

public class ReorderBuffer implements BaseReorderBuffer {
    private final Map<String, Optional<String>> originalAndCurrentNamesOfRegisters;

    public ReorderBuffer(BaseRegisterBank<?> registerBank) {
        Objects.requireNonNull(registerBank);

        var registers = registerBank.getAllRegisters();
        this.originalAndCurrentNamesOfRegisters = new HashMap<>(registers.length);
        initRegisterNamesWith(registers);
    }

    private void initRegisterNamesWith(Register<?>[] registers) {
        for (int i = 0; i < registers.length; i++) {
            originalAndCurrentNamesOfRegisters.put(
                registers[i].getName(),
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
    public void handleFinishedInstruction(RTypeInstruction instruction) {
        Objects.requireNonNull(instruction);

        var destinationRegister = instruction.getDestination();
        var destinationRegisterName = destinationRegister.getName();

        var optional = originalAndCurrentNamesOfRegisters.get(destinationRegisterName);

        if (optional.isPresent()) {
            System.out.println("LOG from reorder buffer:\n\tMarking << " + destinationRegisterName + " >> as if it was not renamed");

            originalAndCurrentNamesOfRegisters.put(destinationRegisterName, Optional.empty());
        }
    }
}

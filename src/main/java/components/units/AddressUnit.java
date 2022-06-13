package main.java.components.units;

import java.util.Objects;

import main.java.components.registers.BaseRegisterBank;
import main.java.instructions.MemTypeInstruction;

public class AddressUnit implements BaseAddressUnit {
    private final BaseRegisterBank<Integer> addressRegisterBank;

    public AddressUnit(BaseRegisterBank<Integer> addressRegisterBank) {
        this.addressRegisterBank = Objects.requireNonNull(addressRegisterBank);
    }

    @Override
    public int calculateAddressFor(MemTypeInstruction instruction) {
        Objects.requireNonNull(instruction);

        var baseRegister = instruction.getBaseRegister();
        var offset = instruction.getOffset();

        var baseRegisterValue = addressRegisterBank
            .getRegisterValue(baseRegister.getName())
            .orElseThrow();

        return offset + baseRegisterValue;
    }
}

package main.java.components.units;

import main.java.instructions.MemTypeInstruction;

public interface BaseAddressUnit {
    int calculateAddressFor(MemTypeInstruction instruction);
}

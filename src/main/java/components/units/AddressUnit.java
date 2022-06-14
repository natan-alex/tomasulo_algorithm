package main.java.components.units;

import java.util.Arrays;
import java.util.Objects;

import main.java.components.buffers.Buffer;
import main.java.components.registers.BaseRegisterBank;
import main.java.components.registers.BaseReorderBuffer;

public class AddressUnit implements BaseAddressUnit {
    private static final String NAME = "Address unit";
    private final BaseRegisterBank<Integer> addressRegisterBank;
    private final Buffer[] buffers;
    private final BaseReorderBuffer reorderBuffer;
    private final BaseRegisterBank<Double> fpRegisterBank;

    public AddressUnit(
            BaseRegisterBank<Integer> addressRegisterBank,
            BaseRegisterBank<Double> fpRegisterBank,
            Buffer[] buffers,
            BaseReorderBuffer reorderBuffer) {
        this.addressRegisterBank = Objects.requireNonNull(addressRegisterBank);
        this.buffers = Objects.requireNonNull(buffers);
        this.reorderBuffer = Objects.requireNonNull(reorderBuffer);
        this.fpRegisterBank = Objects.requireNonNull(fpRegisterBank);
    }

    public static String getName() {
        return NAME;
    }

    @Override
    public void calculateAddressAndStoreInABuffer(MemoryInstructionAndControlInfos infos) {
        Objects.requireNonNull(infos);

        var baseRegisterValue = addressRegisterBank
                .getRegisterValue(infos.getBaseRegisterName())
                .orElseThrow();

        var allInfos = new MemoryUnitBroadcastInfos(
                infos.getOffset() + baseRegisterValue,
                infos.getOperation(),
                NAME,
                infos.getDestinationRegisterName(),
                fpRegisterBank.getRegisterValue(infos.getDestinationRegisterName()),
                reorderBuffer.getNewNameForRegister(infos.getDestinationRegisterName()),
                infos.getCountDownLatch());

        var buffer = Arrays.stream(buffers)
                .filter(b -> !b.isBusy() && b.getOperation() == infos.getOperation())
                .findFirst();

        if (buffer.isPresent()) {
            buffer.get().storeInfosAndSendToMemoryUnit(allInfos);
        } else {
            System.out.println("All buffers are busy :(");
        }
    }
}

package main.java.components.units;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import main.java.components.buffers.Buffer;
import main.java.components.registers.BaseRegisterBank;
import main.java.components.registers.BaseReorderBuffer;
import main.java.instructions.Operation;

public class AddressUnit implements BaseAddressUnit {
    public static final String NAME = "Address unit";
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

        var buffer = getNotBusyBufferForOperation(infos.getOperation());

        if (buffer.isPresent()) {
            reorderBuffer.renameRegister(infos.getDestinationRegisterName(), MemoryUnit.NAME);
            buffer.get().storeInfosAndSendToMemoryUnit(allInfos);
        } else {
            System.out.println("All buffers are busy :(");
        }
    }

    private Optional<Buffer> getNotBusyBufferForOperation(Operation operation) {
        return Arrays.stream(buffers)
                .filter(b -> !b.isBusy() && b.getOperation() == operation)
                .findFirst();
    }
}

package main.java.components.units;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import main.java.components.buffers.Buffer;
import main.java.components.registers.BaseRegisterBank;
import main.java.components.registers.BaseReorderBuffer;
import main.java.instructions.Operation;

public class AddressUnit implements BaseAddressUnit {
    public static final String NAME = "ADDRESS UNIT";

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

        var buffer = getANotBusyBufferForOperation(infos.getOperation());

        if (buffer.isEmpty()) {
            System.out.println("All buffers are busy :(");
            return;
        }

        var allInfos = new MemoryUnitBroadcastInfos(
                calculateAddress(infos),
                infos.getOperation(),
                buffer.get().getName(),
                infos.getDestinationRegisterName(),
                reorderBuffer.getNewNameForRegister(infos.getDestinationRegisterName()),
                fpRegisterBank.getRegisterValue(infos.getDestinationRegisterName()),
                infos.getCountDownLatch());

        reorderBuffer.renameRegister(infos.getDestinationRegisterName(), buffer.get().getName());

        buffer.get().storeInfosAndSendToMemoryUnit(allInfos);
    }

    private Optional<Buffer> getANotBusyBufferForOperation(Operation operation) {
        return Arrays.stream(buffers)
                .filter(b -> !b.isBusy() && b.getOperation() == operation)
                .findFirst();
    }

    private int calculateAddress(MemoryInstructionAndControlInfos infos) {
        System.out.println("LOG from " + NAME + ":"
                + "\n\tReceived offset << " + infos.getOffset() + " >>"
                + " and base register << " + infos.getBaseRegisterName() + " >>"
                + "\n\tCalculating address");

        var baseRegisterValue = addressRegisterBank
                .getRegisterValue(infos.getBaseRegisterName())
                .orElseThrow();

        System.out.println("LOG from " + NAME + ":"
                + "\n\tValue for << " + infos.getBaseRegisterName() + " >>"
                + " fetched: << " + baseRegisterValue + " >>");

        return infos.getOffset() + baseRegisterValue;
    }
}
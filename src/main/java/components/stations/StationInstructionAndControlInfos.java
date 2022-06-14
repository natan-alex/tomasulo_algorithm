package main.java.components.stations;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import main.java.instructions.RTypeInstruction;

public class StationInstructionAndControlInfos {
    private final String firstOperandName;
    private final String secondOperandName;
    private final String destinationRegisterName;
    private final CountDownLatch countDownLatch;

    public StationInstructionAndControlInfos(
            RTypeInstruction instruction,
            CountDownLatch countDownLatch) {
        Objects.requireNonNull(instruction);

        this.firstOperandName = Objects.requireNonNull(instruction.getFirstOperandRegister().getName());
        this.secondOperandName = Objects.requireNonNull(instruction.getSecondOperandRegister().getName());
        this.destinationRegisterName = Objects.requireNonNull(instruction.getDestinationRegister().getName());
        this.countDownLatch = Objects.requireNonNull(countDownLatch);
    }

    public String getFirstOperandName() {
        return firstOperandName;
    }

    public String getSecondOperandName() {
        return secondOperandName;
    }

    public String getDestinationRegisterName() {
        return destinationRegisterName;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

}

package main.java.components.registers;

import main.java.components.busses.BusObserver;

public interface BaseRegisterBankObserver<T extends Number> 
    extends BaseRegisterBank<T>, BusObserver {
}

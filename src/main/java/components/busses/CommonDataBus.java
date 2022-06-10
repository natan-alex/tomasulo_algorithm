package main.java.components.busses;

public class CommonDataBus extends DataBus {
    public CommonDataBus() {
    }

    public void broadcastValue(double value, String destinationRegisterName) {
        System.out.println("LOG from common data bus: ");
        System.out.println("Broadcasting value << " + value + " >> and register << " + destinationRegisterName + " >>");
        super.notifyObserversWith(value, destinationRegisterName);
    }
}

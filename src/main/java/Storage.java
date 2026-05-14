import java.util.Vector;

import models.dto.OrdineRequestDto;
import models.dto.Prodotto;

public class Storage {
    private boolean isRunning = true;
    private OrderStates state;
    private Vector<Object> toSend;

    public synchronized boolean running() {
        return isRunning;
    }

    public synchronized void stop() {
        isRunning = false;
    }

    public synchronized OrderStates getState() {
        return state;
    }

    public synchronized void changeState(OrderStates state) {
        this.state = state;
    }

    public synchronized void addToSend(Object o) {
        toSend.add(o);
    }

    public synchronized Vector<Object> getToSend() {
        return toSend;
    }
}



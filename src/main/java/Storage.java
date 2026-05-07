import java.util.Vector;

public class Storage {
    private boolean isRunning = true;
    private OrderStates state;

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

    public synchronized void addProducts(Vector<Prodotto> prodotti) {
        
    }
}



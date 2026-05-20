import java.util.Vector;

import models.Area;

public class Storage {
    private boolean isRunning = true;
    private OrderStates state = OrderStates.INMENU;
    private Vector<Object> toSend = new Vector<>();
    private Vector<String> messages = new Vector<>();
    private String currentMenu;

    private String username;
    private Area selectedArea;

    public Storage(String username, Area selectedArea) {
        this.username = username;
        this.selectedArea = selectedArea;
    }

    public synchronized boolean running() {
        return isRunning;
    }

    public synchronized void stop() {
        isRunning = false;
        notifyAll();
    }

    public synchronized OrderStates getState() {
        return state;
    }

    public synchronized void changeState(OrderStates state) {
        this.state = state;
    }

    public synchronized void addToSend(Object o) {
        toSend.add(o);
        this.notifyAll();
    }

    public synchronized Vector<Object> getToSend() {
        while (isRunning && toSend.isEmpty()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Vector<Object> toReturn = new Vector<>(toSend);
        toSend.clear();
        return toReturn;
    }

    public synchronized Vector<String> getMessages() {
        return messages;
    }

    public synchronized void addMessage(String message) {
        messages.add(message);
    }

    public synchronized void writeMenu(String menu) {
        System.out.print(menu);
        this.currentMenu = menu;
    }

    public synchronized String getMenu() {
        return currentMenu;
    }
}

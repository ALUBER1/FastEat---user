public class Main {
    public static void main(String[] args) {
        Storage storage = new Storage();
        KeyboardManager keyboardManager = new KeyboardManager(storage);
        keyboardManager.start();

        System.out.println(storage.getToSend());
        try {
            keyboardManager.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
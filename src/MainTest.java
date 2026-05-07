public class MainTest {
    public static void main(String[] args) {
        Storage s = new Storage();
        KeyboardManager k = new KeyboardManager(s);

        k.start();
    }
}

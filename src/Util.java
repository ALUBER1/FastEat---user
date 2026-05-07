import java.io.IOException;

public class Util {
    public static void errorMessage(String msg) {
        System.out.println(msg);
        System.out.print("premi invio per continuare...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearConsole() {
        
    }
}

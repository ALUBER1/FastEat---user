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
        clearConsole();
    }

    public static void clearConsole() {
        String os = System.getProperty("os.name");

        if (os.contains("Windows")) {
            try {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }
}

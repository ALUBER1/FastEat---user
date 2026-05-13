import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import io.github.cdimascio.dotenv.Dotenv;
import com.google.gson.Gson;
import models.dto.Login;
import models.dto.Area;

public class Main {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        
        String serverUrl = dotenv.get("SERVER_URL");
        String portStr = dotenv.get("PORT");

        if (serverUrl == null || portStr == null || serverUrl.isEmpty()) {
            return;
        }

        int port;
        try {
            port = Integer.parseInt(portStr);
        } catch (NumberFormatException e) {
            return;
        }

        Gson gson = new Gson();
        Scanner scanner = new Scanner(System.in);

        try (Socket socket = new Socket(serverUrl, port)) {
            
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            
            System.out.print("Username: ");
            String usernameInput = scanner.nextLine();
            
            Login loginDto = new Login(usernameInput);
            output.writeUTF(gson.toJson(loginDto));

            String confirmedArea = input.readUTF();

            if (confirmedArea.equals("NEED_AREA")) {
                String areaList = input.readUTF();
                System.out.println(areaList);
                
                System.out.print("Area: ");
                String selectedArea = scanner.nextLine();
                
                Area areaDto = new Area(selectedArea);
                output.writeUTF(gson.toJson(areaDto));
                
                confirmedArea = input.readUTF();
            }

            Storage storage = new Storage();
            storage.setUserName(usernameInput);
            storage.setUserArea(confirmedArea);

            Reciever receiver = new Reciever(storage);
            Sender sender = new Sender(storage);
            KeyboardManager keyboardManager = new KeyboardManager(storage);

            receiver.start();
            sender.start();
            keyboardManager.start();

            receiver.join();
            sender.join();
            keyboardManager.join();

        } catch (IOException | InterruptedException e) {
            System.err.println("Errore: " + e.getMessage());
        }
    }
}
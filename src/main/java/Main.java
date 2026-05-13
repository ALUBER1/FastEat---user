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

        int port = 8080; 
        if (serverUrl == null || serverUrl.isEmpty()) {
            serverUrl = "localhost";
        }
        
        try {
            if (portStr != null) {
                port = Integer.parseInt(portStr);
            }
        } catch (NumberFormatException e) {
            System.err.println("Porta non valida nel .env, uso default 8080");
        }

        Gson gson = new Gson();
        Scanner scanner = new Scanner(System.in);

        try (Socket socket = new Socket(serverUrl, port)) {
            
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            
            ConsoleUtil.clear();
            System.out.print("Username: ");
            String usernameInput = scanner.nextLine();
            
            Login loginDto = new Login(usernameInput);
            output.writeUTF(gson.toJson(loginDto));

            String serverResponse = input.readUTF();
            String finalArea;

            if (serverResponse.contains("{") || serverResponse.contains("[")) {
                ConsoleUtil.clear();
                System.out.println("Seleziona area tra:\n" + serverResponse);
                System.out.print("Area: ");
                finalArea = scanner.nextLine();
                
                Area areaDto = new Area(finalArea);
                output.writeUTF(gson.toJson(areaDto));
            } else {
                finalArea = serverResponse;
            }

            Storage storage = new Storage(usernameInput, finalArea);

            Receiver receiver = new Receiver(storage, socket);
            Sender sender = new Sender(storage, socket);
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
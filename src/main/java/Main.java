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
            throw new RuntimeException("Inserire dati corretti nel file .env: SERVER_URL o PORT mancanti");
        }
        int port;
        try {
            port = Integer.parseInt(portStr);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Inserire dati corretti nel file .env: PORT deve essere un numero");
        }

        Gson gson = new Gson();
        Scanner scanner = new Scanner(System.in);

        try (Socket socket = new Socket(serverUrl, port)) {
            
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream documentOutput = new DataOutputStream(socket.getOutputStream());
            
            Util.clear();
            String usernameInput = "";
            while (usernameInput.trim().isEmpty()) {
                System.out.print("Username: ");
                usernameInput = scanner.nextLine();
            }
            
            Login loginDto = new Login(usernameInput);
            documentOutput.writeUTF(gson.toJson(loginDto));

            String serverResponse = input.readUTF();
            String finalArea;

            if (!serverResponse.equalsIgnoreCase("OK")) {
                Util.clear();
                System.out.println("Aree disponibili:\n" + serverResponse);
                
                String selectedArea = "";
                while (selectedArea.trim().isEmpty()) {
                    System.out.print("Area: ");
                    selectedArea = scanner.nextLine();
                }
                
                Area areaDto = new Area(selectedArea);
                documentOutput.writeUTF(gson.toJson(areaDto));
                finalArea = selectedArea;
            } else {
                finalArea = "Default"; 
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
        } finally {
            scanner.close();
        }
    }
}
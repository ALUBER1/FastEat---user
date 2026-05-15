import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import io.github.cdimascio.dotenv.Dotenv;
import com.google.gson.Gson;
import models.dto.Login;
import models.dto.Area;
import models.dto.AvailableAreas;

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
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            
            String usernameInput = "";
            while (usernameInput.trim().isEmpty()) {
                System.out.print("Username: ");
                usernameInput = scanner.nextLine();
                Util.clear();
            }
            
            Login loginDto = new Login(usernameInput);
            output.writeUTF(gson.toJson(loginDto));
            output.flush();

            String serverResponse = input.readUTF();
            AvailableAreas available = gson.fromJson(serverResponse, AvailableAreas.class);

            String finalAreaName = "";

            if (available.getAreas() != null && !available.getAreas().isEmpty()) {
                boolean valid = false;
                while (!valid) {
                    System.out.println("Aree: " + available.getAreas());
                    System.out.print("Area: ");
                    finalAreaName = scanner.nextLine();
                    Util.clear();

                    if (available.getAreas().contains(finalAreaName)) {
                        valid = true;
                    } else {
                        System.out.println("Area non valida.");
                    }
                }
                
                Area areaDto = new Area(finalAreaName);
                output.writeUTF(gson.toJson(areaDto));
                output.flush();
            } else {
                finalAreaName = "Pre-set";
            }

            Storage storage = new Storage(usernameInput, finalAreaName);

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
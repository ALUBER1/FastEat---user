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
        int port = Integer.parseInt(dotenv.get("PORT"));
        Gson gson = new Gson();

        Scanner scanner = new Scanner(System.in);

        try (Socket socket = new Socket(serverUrl, port);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

            System.out.print("Inserisci lo username: ");
            String username = scanner.nextLine();
            
            Login loginDto = new Login(username);
            output.writeUTF(gson.toJson(loginDto));

            String confirmedArea;
            String serverResponse = input.readUTF();

            if (serverResponse.equals("NEED_AREA")) {
                String areaList = input.readUTF();
                System.out.println("Aree disponibili:\n" + areaList);
                
                System.out.print("Scrivi il nome dell'area tra quelle elencate: ");
                String areaName = scanner.nextLine();
                
                Area areaDto = new Area(areaName);
                output.writeUTF(gson.toJson(areaDto));
                
                confirmedArea = input.readUTF();
            } else {
                confirmedArea = serverResponse;
            }

            System.out.println("Accesso eseguito nell'area: " + confirmedArea);

            Storage storage = new Storage();
            storage.setUserName(username);
            storage.setUserArea(confirmedArea);

            Reciever receiver = new Reciever();
            receiver.s = storage;
            
            Sender sender = new Sender();
            sender.s = storage;
            
            KeyboardManager keyboardManager = new KeyboardManager();
            keyboardManager.s = storage;

            receiver.start();
            sender.start();
            keyboardManager.start();

            receiver.join();
            sender.join();
            keyboardManager.join();

        } catch (IOException | InterruptedException e) {
            System.err.println("Errore di connessione o comunicazione: " + e.getMessage());
        } finally {
            scanner.close();
        }

    }
}
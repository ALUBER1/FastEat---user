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
        
        // Parsing e validazione dei dati dal .env
        String serverUrl = dotenv.get("SERVER_URL");
        String portStr = dotenv.get("PORT");

        if (serverUrl == null || portStr == null || serverUrl.isEmpty()) {
            System.err.println("Errore: Variabili SERVER_URL o PORT mancanti nel file .env");
            return;
        }

        int port;
        try {
            port = Integer.parseInt(portStr);
        } catch (NumberFormatException e) {
            System.err.println("Errore: Il valore della PORT nel file .env non è un numero valido");
            return;
        }

        Gson gson = new Gson();
        Scanner scanner = new Scanner(System.in);
        System.out.println("--- FastEat: Applicativo Cliente ---");

        // Try-with-resources solo per la Socket
        try (Socket socket = new Socket(serverUrl, port)) {
            
            // Inizializzazione manuale degli stream
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            
            System.out.println("Connessione al server stabilita.");

            System.out.print("Inserisci lo username per il login: ");
            String usernameInput = scanner.nextLine();
            
            // Invio DTO Login via JSON
            Login loginDto = new Login(usernameInput);
            output.writeUTF(gson.toJson(loginDto));

            String confirmedArea;
            String serverResponse = input.readUTF();

            // Logica di gestione area mancante
            if (serverResponse.equals("NEED_AREA")) {
                String areaList = input.readUTF();
                System.out.println("Aree disponibili:\n" + areaList);
                
                System.out.print("Scrivi il nome dell'area scelta: ");
                String selectedArea = scanner.nextLine();
                
                // Invio DTO Area via JSON
                Area areaDto = new Area(selectedArea);
                output.writeUTF(gson.toJson(areaDto));
                
                confirmedArea = input.readUTF();
            } else {
                confirmedArea = serverResponse;
            }

            System.out.println("Accesso eseguito nell'area: " + confirmedArea);

            // Creazione storage e passaggio ai thread tramite costruttore
            Storage storage = new Storage();
            storage.setUserName(usernameInput);
            storage.setUserArea(confirmedArea);

            Reciever receiver = new Reciever();
            Sender sender = new Sender();
            KeyboardManager keyboardManager = new KeyboardManager();

            receiver.start();
            sender.start();
            keyboardManager.start();

            // Join per mantenere il socket aperto finché i thread lavorano
            receiver.join();
            sender.join();
            keyboardManager.join();

        } catch (IOException | InterruptedException e) {
            System.err.println("Errore durante l'esecuzione: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
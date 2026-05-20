import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import io.github.cdimascio.dotenv.Dotenv;
import com.google.gson.Gson;
import models.Area;
import models.dto.*;

public class Main {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        String hostname = dotenv.get("SERVER_URL");
        String portStr = dotenv.get("PORT");

        if (hostname == null || portStr == null || hostname.isEmpty()) {
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

        try {
            Socket socket = new Socket(hostname, port);
            
            InputStream istream = socket.getInputStream();
            Scanner reader = new Scanner(istream);
            OutputStream ostream = socket.getOutputStream();
            BufferedOutputStream writer = new BufferedOutputStream(ostream);

            String username = "";
            while (username.trim().isEmpty()) {
                System.out.print("Username: ");
                username = scanner.nextLine();
                Util.clearConsole();
            }

            String loginJson = gson.toJson(new LoginDto(username)) + "\n";
            writer.write(loginJson.getBytes(StandardCharsets.UTF_8));
            writer.flush();

            AreaDto selectedArea = null;
            if (reader.hasNextLine()) {
                String response = reader.nextLine();
                AreasDisponibiliDto available = gson.fromJson(response, AreasDisponibiliDto.class);

                if (available != null && available.getAreas() != null && available.getAreas().length > 0) {
                    AreaDto[] areaArray = available.getAreas();
                    int choice = -1;

                    while (choice < 0 || choice >= areaArray.length) {
                        System.out.println("Seleziona la tua zona:");
                        for (int i = 0; i < areaArray.length; i++) {
                            System.out.println(areaArray[i].toMenu(i));
                        }
                        System.out.print("Scelta: ");
                        String input = scanner.nextLine();
                        Util.clearConsole();

                        if (input.matches("^\\d+$")) {
                            int tempChoice = Integer.parseInt(input);
                            if (tempChoice >= 0 && tempChoice < areaArray.length) {
                                choice = tempChoice;
                            } else {
                                System.out.println("Indice non valido.");
                            }
                        } else {
                            System.out.println("Inserire solo numeri.");
                        }
                    }
                    Area areaScelta = new Area(areaArray[choice].getId(),areaArray[choice].getNome(),areaArray[choice].getIp());
                    AreaSenderDto areaSceltaInvio = new AreaSenderDto(areaScelta);

                    selectedArea = areaArray[choice];
                    String areaJson = gson.toJson(areaSceltaInvio) + "\n";
                    writer.write(areaJson.getBytes(StandardCharsets.UTF_8));
                    writer.flush();
                }
            }else{
                String rispostaName =reader.nextLine();
                UserResponseDto nomeUser = gson.fromJson(rispostaName, UserResponseDto.class);
                selectedArea.setId(nomeUser.getUtente().getZona_id());
            }


            Storage storage = new Storage(username, selectedArea);

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
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
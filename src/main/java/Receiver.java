import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import com.google.gson.Gson;
import models.dto.InsertResponseDto;

public class Receiver extends Thread {

    private final Storage storage;
    private final Socket socket;
    private final Gson gson;

    public Receiver(Storage storage, Socket socket) {
        this.storage = storage;
        this.socket = socket;
        this.gson = new Gson();
    }

    @Override
    public void run() {
        try {
            InputStream istream = socket.getInputStream();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            new BufferedInputStream(istream),
                            StandardCharsets.UTF_8
                    )
            );

            while (storage.running()) {

                String json = reader.readLine();

                InsertResponseDto message = gson.fromJson(json, InsertResponseDto.class);

                storage.addMessage(message.getMsg());
            }

        } catch (IOException e) {
            System.err.println("Errore: " + e.getMessage());
        }
    }
}
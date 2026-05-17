import com.google.gson.Gson;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Vector;

public class Reciever extends Thread {

    private final Storage storage;
    private final Socket socket;
    private final Gson gson;

    public Reciever(Storage storage, Socket socket) {
        this.storage = storage;
        this.socket = socket;
        this.gson = new Gson();
    }

    @Override
    public void run() {
        try {
            OutputStream ostream = socket.getOutputStream();
            BufferedOutputStream writer = new BufferedOutputStream(ostream);

            while (storage.running()) {
                Vector<String> messages = storage.getMessages();

                for (Object message : messages) {
                    String json = gson.toJson(message) + "\n";
                    writer.write(json.getBytes(StandardCharsets.UTF_8));
                    writer.flush();
                }
            }
        } catch (IOException e) {
            System.err.println("Errore: " + e.getMessage());
        }
    }
}

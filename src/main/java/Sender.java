import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Vector;
import com.google.gson.Gson;

public class Sender extends Thread {
    private final Storage storage;
    private final Socket socket;
    private final Gson gson;

    public Sender(Storage storage, Socket socket) {
        this.storage = storage;
        this.socket = socket;
        this.gson = new Gson();
    }

    public Sender(Storage storage) {
        s = storage;
    }

    @Override
    public void run() {
        try {
            OutputStream ostream = socket.getOutputStream();
            BufferedOutputStream writer = new BufferedOutputStream(ostream);

            while (storage.running()) {
                Vector<Object> messages = storage.getToSend();

                for (Object message : messages) {
                    String json = gson.toJson(message) + "\n";
                    writer.write(json.getBytes(StandardCharsets.UTF_8));
                    writer.flush();
                    if (message.equals("!disconnect!"))
                        storage.stop();
                }
            }
        } catch (IOException e) {
            System.err.println("Errore: " + e.getMessage());
        }
    }
}
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
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

    @Override
    public void run() {
        try {
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            while (storage.isRunning()) {
                Vector<Object> messages = storage.fetchMessages();

                for (Object message : messages) {
                    output.writeUTF(gson.toJson(message));
                    output.flush();
                }
            }
        } catch (IOException e) {
            System.err.println("Errore: " + e.getMessage());
        }
    }
}
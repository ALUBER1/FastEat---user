import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
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
                synchronized (storage) {
                    while (storage.isRunning() && !storage.hasMessages()) {
                        storage.wait();
                    }

                    if (!storage.isRunning()) break;

                    Object message = storage.fetchMessage();
                    if (message != null) {
                        output.writeUTF(gson.toJson(message));
                        output.flush();
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Errore: " + e.getMessage());
        }
    }
}
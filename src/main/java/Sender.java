import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import com.google.gson.Gson;
import models.dto.Update;

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

            while (!isInterrupted()) {
                synchronized (storage) {
                    storage.wait();
                    
                    Update update = new Update(
                        storage.getUserName(),
                        storage.getUserArea(),
                        storage.getLabel(),
                        storage.getCommand()
                    );

                    output.writeUTF(gson.toJson(update));
                    output.flush();
                }
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Errore: " + e.getMessage());
        }
    }
}
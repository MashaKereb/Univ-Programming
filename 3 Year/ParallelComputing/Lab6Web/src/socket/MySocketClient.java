package socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Masha Kereb on 21-May-17.
 */
public class MySocketClient {
    private static MySocketClient mySocketClient;
    Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;

    protected MySocketClient(String port){
        System.out.println("Connecting to server...");
        try {
            socket = new Socket("localhost", Integer.parseInt(port));
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Connected to server!");
    }

    static MySocketClient getInstance() {
        if (mySocketClient == null)
            mySocketClient = new MySocketClient();
        return mySocketClient;

    }

    protected MySocketClient() {
        this("3045");
    }
}

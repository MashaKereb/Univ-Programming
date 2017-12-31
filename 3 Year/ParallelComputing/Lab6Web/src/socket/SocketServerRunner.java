package socket;

/**
 * Created by Masha Kereb on 21-May-17.
 */
public class SocketServerRunner {
    public static void main(String[] args) {
        MySocketServer server = new MySocketServer("3045");
        server.start();

    }
}

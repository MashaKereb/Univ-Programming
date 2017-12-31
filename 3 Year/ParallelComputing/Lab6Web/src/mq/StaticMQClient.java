package mq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Masha Kereb on 28-May-17.
 */
public class StaticMQClient {
    private static MQClient client;

    public static MQClient getInstance() {
        if (client == null) {
            try {
                client = new MQClient();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
        return client;
    }
}

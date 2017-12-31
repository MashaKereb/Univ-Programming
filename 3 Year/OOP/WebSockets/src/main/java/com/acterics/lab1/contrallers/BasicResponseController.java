package com.acterics.lab1.contrallers;

import com.acterics.lab1.data.Message;
import org.eclipse.jetty.websocket.api.Session;

/**
 * Created by Masha Kereb on 07-Jun-17.
 */
public class BasicResponseController extends ResponseController {
    BasicResponseController(Session session) {
        super(session);
    }

    @Override
    public void processRequest(Message message) {
        sendFinalMessage();
    }
}

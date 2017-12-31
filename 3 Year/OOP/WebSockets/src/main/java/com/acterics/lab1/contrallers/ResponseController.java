package com.acterics.lab1.contrallers;


import com.acterics.lab1.data.Message;
import com.acterics.lab1.websocket.CannonSocket;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

/**
 * Created by Masha Kereb on 02-Jun-17.
 */

public abstract class ResponseController {
    protected static final String CONNECT_RESPONSE = "connect_response";
    protected static final String STATUS_SUCCESS = "success";
    protected static final String END_RESPONSE = "end_response";
    protected static final String NEXT_RESPONSE = "next_response";
    protected static final String START_RESPONSE = "start_response";

    private Session session;

    final protected static Logger log = Logger.getLogger(CannonSocket.class);
    final protected ObjectMapper mapper = new ObjectMapper();

    ResponseController(Session session){
        this.session = session;
    }

    public abstract void processRequest(Message message);

    public void sendConnectMessage(){
        Message connectMessage = new Message();
        connectMessage.setStatus(STATUS_SUCCESS);
        connectMessage.setType(CONNECT_RESPONSE);
        sendMessage(connectMessage);
    }

    protected void sendMessage(Message message) {
        try {
            String json = mapper.writeValueAsString(message);
            session.getRemote().sendString(json);
        } catch (IOException e) {
            log.error(e.getStackTrace());
        }
    }

    protected void sendFinalMessage(){
        Message response = new Message();
        response.setType(END_RESPONSE);
        response.setStatus(STATUS_SUCCESS);
        sendMessage(response);
    }
}

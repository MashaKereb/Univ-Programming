package com.acterics.lab1.websocket;

import com.acterics.lab1.contrallers.ResponseController;
import com.acterics.lab1.contrallers.ResponseControllerFactory;
import com.acterics.lab1.data.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class CannonSocket {

    final static Logger log = Logger.getLogger(CannonSocket.class);
    final private ObjectMapper mapper = new ObjectMapper();

    private Session session;


    @OnWebSocketConnect
    public void onConnect(Session session) {
        log.info("Connect: " + session.getRemoteAddress().getAddress());
        this.session = session;
        ResponseControllerFactory factory = ResponseControllerFactory.getInstance();
        ResponseController responseController = factory.getResponseController("Basic", this.session);
        responseController.sendConnectMessage();

    }

    @OnWebSocketMessage
    public void onText(String jsonMessage) {
        log.debug("Get message: " + jsonMessage);
        try {
            Message message = mapper.readValue(jsonMessage, Message.class);

            ResponseControllerFactory factory = ResponseControllerFactory.getInstance();
            ResponseController responseController = factory.getResponseController(message.getType(), this.session);
            responseController.processRequest(message);

        } catch (Exception e) {
            log.error(e.getStackTrace());

        }
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        log.info("Close: statusCode = " + statusCode + ", reason = " + reason);
    }

}
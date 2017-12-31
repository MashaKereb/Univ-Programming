package com.acterics.lab1.contrallers;

import org.eclipse.jetty.websocket.api.Session;

/**
 * Created by Masha Kereb on 02-Jun-17.
 */
public class ResponseControllerFactory {
    private static ResponseControllerFactory instance;

    protected ResponseControllerFactory() {
    }

    public synchronized static ResponseControllerFactory getInstance() {

        if (instance == null) {
            instance = new ResponseControllerFactory();
        }
        return instance;
    }

    public ResponseController getResponseController(String controllerType, Session session) {
        if (controllerType.toLowerCase().equals("full")) {
            return new FullResponseController(session);
        } else if (controllerType.toLowerCase().equals("partial")) {
            return new PartialResponseController(session);
        } else {
            return new BasicResponseController(session);
        }
    }
}

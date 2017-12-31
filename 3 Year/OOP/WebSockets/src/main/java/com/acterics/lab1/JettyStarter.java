package com.acterics.lab1;


import com.acterics.lab1.websocket.SocketServlet;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;


public class JettyStarter {

    final static Logger log = Logger.getLogger(JettyStarter.class);

    public static void main(String[] args) {

        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);
        server.addConnector(connector);

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setWelcomeFiles(new String[]{"index.html"});
        resource_handler.setResourceBase("./src/main/resources/Lab1");

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/connect");
        ServletHolder servletHolder = new ServletHolder("ws-events", SocketServlet.class);
        context.addServlet(servletHolder, "/");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{context, resource_handler});

        server.setHandler(handlers);

        try {
            server.start();
            server.join();
        } catch (Throwable t) {
            log.fatal(t.getStackTrace());
        }
    }
}

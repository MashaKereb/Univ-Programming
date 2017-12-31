package command;

import database.DAOFactory;
import service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

public abstract class Command {
    protected HttpServletRequest req;
    protected HttpServletResponse resp;
    protected ServiceFactory serviceFactory = ServiceFactory.getInstance();
    protected MessageFactory messageFactory = MessageFactory.getInstance();
    protected static Logger logger = Logger.getLogger("Command Logger");

    public void setReq(HttpServletRequest req) {
        this.req = req;
    }

    public void setResp(HttpServletResponse resp) {
        this.resp = resp;
    }

    public abstract void execute() throws ServletException, IOException;
}

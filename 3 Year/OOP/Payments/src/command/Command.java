package command;

import database.DAOFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class Command {
    protected HttpServletRequest req;
    protected HttpServletResponse resp;
    protected DAOFactory daoFactory = DAOFactory.getInstance();


    public void setReq(HttpServletRequest req) {
        this.req = req;
    }

    public void setResp(HttpServletResponse resp) {
        this.resp = resp;
    }

    public abstract void execute() throws ServletException,IOException;
}

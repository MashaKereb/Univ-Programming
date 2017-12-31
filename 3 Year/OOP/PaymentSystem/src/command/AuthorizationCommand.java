package command;

import beans.User;
import database.DAO;
import java.io.IOException;

public class AuthorizationCommand extends Command {

    @Override
    public void execute() throws IOException {

        User user=dao.getUser(req.getParameter("login"),req.getParameter("pass"));
        System.out.println("autho command");
        if (user!=null){
            req.getSession().setAttribute("User",user);
            resp.sendRedirect("/Main?command=Menu");

        }

    }
}

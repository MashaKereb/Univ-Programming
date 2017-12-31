
import command.Command;
import command.factory.CommandFactory;
import command.factory.CommandFactoryImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Authorization extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("login").trim().length()==0||
                req.getParameter("pass").trim().length()==0){
            req.setAttribute("Error","Wrong login , or password");
            req.getRequestDispatcher("web/dataError.jsp").forward(req,resp);
        }else {
            CommandFactory factory= CommandFactoryImpl.getFactory();
            Command command = factory.getCommand("Authorization",req,resp);
            command.execute();
        }

    }
}

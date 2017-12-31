

import command.Command;
import command.factory.CommandFactory;
import command.factory.CommandFactoryImpl;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Main extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String commandName = req.getParameter("command");
        if (commandName == null) {
            req.setAttribute("Error", "Wrong command!");
            req.getRequestDispatcher("/dataError.jsp").forward(req, resp);
            return;
        }
        CommandFactory factory = CommandFactoryImpl.getFactory();
        Command command = factory.getCommand(commandName, req, resp);
        command.execute();


    }
}

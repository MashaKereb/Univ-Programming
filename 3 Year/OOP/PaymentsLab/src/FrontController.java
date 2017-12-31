import command.Command;
import command.factory.CommandFactory;
import command.factory.ICommandFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name="Main",urlPatterns = "/Main*")
public class FrontController extends HttpServlet {
    ICommandFactory factory = CommandFactory.getFactory();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);

    }

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String commandName = req.getParameter("command");
        if (commandName == null) {
            req.setAttribute("Error", "Wrong command!");
            req.getRequestDispatcher("/dataError.jsp").forward(req, resp);
            return;
        }
        Command command = factory.getCommand(commandName, req, resp);
        command.execute();
    }
}

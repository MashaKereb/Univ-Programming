import beans.User;
import command.Command;
import command.factory.CommandFactory;
import command.factory.CommandFactoryImpl;
import database.DAOFactory;
import database.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(
        name = "Authorization",
        urlPatterns = {"/Authorization"}
)
public class Authorization extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserDAO db = (UserDAO) DAOFactory.getInstance().getDataAccessObject("user");
        try {
            for (User user: db.getAllElems()
                    ) {
                System.out.println(user.getId());
                System.out.println(user.getName());
                System.out.println(user.getSurname());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (req.getParameter("login").trim().length() == 0 ||
                req.getParameter("pass").trim().length() == 0) {
            req.setAttribute("Error", "Wrong login , or password");
            req.getRequestDispatcher("/dataError.jsp").forward(req, resp);
        } else {
            CommandFactory factory = CommandFactoryImpl.getFactory();
            Command command = factory.getCommand("Authorization", req, resp);
            command.execute();
        }

    }
}

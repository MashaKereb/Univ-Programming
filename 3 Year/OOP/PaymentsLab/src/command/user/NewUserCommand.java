package command.user;

import command.Command;
import command.MessageFactory;
import entity.User;
import exceptions.ElementCreationException;
import service.UserService;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by Masha Kereb on 08-Jun-17.
 */

public class NewUserCommand extends Command {
    @Override
    public void execute() throws ServletException, IOException {
        UserService userService = (UserService) serviceFactory.getService("user");
        String message;

        try {
            String email = req.getParameter("email");
            String password = req.getParameter("password");
            User.Type type = User.Type.valueOf(req.getParameter("type"));
            String name = req.getParameter("name");
            String surname = req.getParameter("surname");

            int id = userService.createUser(new User(email, password, name, surname, type));

            req.setAttribute("user_id", id);
            message = messageFactory.getMessage(MessageFactory.MessageType.SuccessUserCreation);


        } catch (NullPointerException e) {
            message = messageFactory.getMessage(MessageFactory.MessageType.InvalidParameters);
        } catch (ElementCreationException e) {
            message = messageFactory.getMessage(MessageFactory.MessageType.UserCreationError);
        }

        // TODO: use transactions
        logger.info(message);
        req.setAttribute("message", message);
        //req.setAttribute("userList", userDAO.getAllElems());

        req.getRequestDispatcher("/Main?command=AllUsers").forward(req, resp);

    }
}

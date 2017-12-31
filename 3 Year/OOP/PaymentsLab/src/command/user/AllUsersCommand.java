package command.user;

import command.Command;
import command.MessageFactory;
import entity.User;
import exceptions.AccessDeniedException;
import service.UserService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by Masha Kereb on 24-Jun-17.
 */
public class AllUsersCommand extends Command {
    @Override
    public void execute() throws ServletException, IOException {
        UserService userService = (UserService) serviceFactory.getService("user");
        String message;
        List<User> userList = null;
        try {
            User user = (User) req.getSession().getAttribute("user");
            if (user == null || !user.isAdmin()) {
                throw new AccessDeniedException();
            } else {
                userList = userService.getAllUsers();
            }

            String curUser = req.getParameter("user_id");
            req.setAttribute("user_id", curUser);

        } catch (NullPointerException e) {
            message = messageFactory.getMessage(MessageFactory.MessageType.InvalidParameters);
            logger.info(message);
            req.setAttribute("message", message);
        } catch (NoSuchElementException e) {
            message = messageFactory.getMessage(MessageFactory.MessageType.NoSuchAccount);
            logger.info(message);
            req.setAttribute("message", message);
        } catch (AccessDeniedException e) {
            req.getRequestDispatcher("/access-denied-error.jsp").forward(req, resp);
            return;
        }

        req.setAttribute("userList", userList);

        req.getRequestDispatcher("/jsp/user-list.jsp").forward(req, resp);
    }
}

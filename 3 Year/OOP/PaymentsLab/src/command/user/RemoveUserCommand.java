package command.user;

import command.Command;
import command.MessageFactory;
import entity.User;
import exceptions.AccessDeniedException;
import exceptions.ElementRemoveException;
import service.UserService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * Created by Masha Kereb on 25-Jun-17.
 */
public class RemoveUserCommand extends Command {
    @Override
    public void execute() throws ServletException, IOException {
        UserService userService = (UserService) serviceFactory.getService("user");
        String message;

        try {
            User user = (User) req.getSession().getAttribute("user");
            if (user == null || !user.isAdmin()) {
                throw new AccessDeniedException();
            } else {
                String curUser = req.getParameter("user_id");
                userService.deleteUser(Integer.parseInt(curUser));

                message = messageFactory.getMessage(MessageFactory.MessageType.Success);
            }

        } catch (NullPointerException e) {
            message = messageFactory.getMessage(MessageFactory.MessageType.InvalidParameters);
        } catch (NoSuchElementException e) {
            message = messageFactory.getMessage(MessageFactory.MessageType.NoSuchAccount);
        } catch (AccessDeniedException e) {
            req.getRequestDispatcher("/access-denied-error.jsp").forward(req, resp);
            return;
        } catch (ElementRemoveException e) {
            message = messageFactory.getMessage(MessageFactory.MessageType.UserOperationError);
        }

        logger.info(message);
        req.setAttribute("message", message);

        req.getRequestDispatcher("Main?command=AllUsers").forward(req, resp);
    }
}

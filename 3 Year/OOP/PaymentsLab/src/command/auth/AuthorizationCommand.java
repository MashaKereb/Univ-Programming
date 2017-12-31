package command.auth;

import command.Command;
import command.MessageFactory;
import entity.User;
import exceptions.IncorrectPasswordException;
import service.AuthService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.NoSuchElementException;

public class AuthorizationCommand extends Command {

    @Override
    public void execute() throws IOException, ServletException {

        AuthService authService = (AuthService) serviceFactory.getService("auth");
        String message;

        try {
            String email = req.getParameter("email");
            String password = req.getParameter("password");

            User user = authService.authorization(email, password);

            logger.info("The user " + email + " successfully logged in");

            req.getSession().setAttribute("user", user);

            if (user.isAdmin()) {
                resp.sendRedirect("/Main?command=AllUsers");
            } else {
                resp.sendRedirect("/Main?command=AllAccounts&user_id=" + user.getId());
            }
            return;


        } catch (NoSuchElementException e) {
            message = messageFactory.getMessage(MessageFactory.MessageType.NoSuchUser);
        } catch (NumberFormatException e) {
            message = messageFactory.getMessage(MessageFactory.MessageType.InvalidParameters);
        } catch (IncorrectPasswordException e) {
            message = messageFactory.getMessage(MessageFactory.MessageType.IncorrectPassword);
        }

        req.setAttribute("message", message);
        logger.info(message);
        req.getRequestDispatcher("authorization.jsp").forward(req, resp);

    }

}

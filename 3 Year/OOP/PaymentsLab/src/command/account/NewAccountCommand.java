package command.account;

import command.Command;
import command.MessageFactory;
import entity.Account;
import entity.User;
import exceptions.ElementCreationException;
import service.AccountService;
import service.UserService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * Created by Masha Kereb on 08-Jun-17.
 */
public class NewAccountCommand extends Command {
    @Override
    public void execute() throws ServletException, IOException {
        AccountService accountService = (AccountService) serviceFactory.getService("account");
        UserService userService = (UserService) serviceFactory.getService("user");
        String message;

        try {
            Integer userId = Integer.parseInt(req.getParameter("user_id"));
            User currentUser = userService.getUser(userId);

            String number = req.getParameter("number");
            String balance = req.getParameter("balance");

            Account account = new Account(number, Integer.parseInt(balance), Account.Status.Active, currentUser);

            int accountId = accountService.createAccount(account);
            message = messageFactory.getMessage(MessageFactory.MessageType.SuccessAccountCreation);

            req.setAttribute("user_id", userId);
            req.setAttribute("account_id", accountId);

        } catch (NoSuchElementException e) {
            message = messageFactory.getMessage(MessageFactory.MessageType.NoSuchUser);
        } catch (NumberFormatException e) {
            message = messageFactory.getMessage(MessageFactory.MessageType.InvalidParameters);
        } catch (ElementCreationException e) {
            message = messageFactory.getMessage(MessageFactory.MessageType.AccountCreationError);
        }

        logger.info(message);
        req.setAttribute("message", message);

        req.getRequestDispatcher("Main?command=AllAccounts").forward(req, resp);

    }
}

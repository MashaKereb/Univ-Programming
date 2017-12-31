package command.account;

import command.Command;
import command.MessageFactory;
import entity.Account;
import entity.User;
import exceptions.AccessDeniedException;
import exceptions.ElementRemoveException;
import service.AccountService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * Created by Masha Kereb on 25-Jun-17.
 */
public class RemoveAccountCommand extends Command {
    @Override
    public void execute() throws ServletException, IOException {

        AccountService accountService = (AccountService) serviceFactory.getService("account");
        String message;
        try {

            String accountId = req.getParameter("account_id");

            Account account = accountService.getAccount(Integer.parseInt(accountId));
            User user = (User) req.getSession().getAttribute("user");
            if (!user.isAdmin() && user.getId() == account.getUser().getId()) {
                throw new AccessDeniedException();
            }
            accountService.deleteAccount(Integer.parseInt(accountId));
            message = messageFactory.getMessage(MessageFactory.MessageType.Success);

        } catch (NullPointerException e) {
            message = messageFactory.getMessage(MessageFactory.MessageType.InvalidParameters);
        } catch (NoSuchElementException e) {
            message = messageFactory.getMessage(MessageFactory.MessageType.NoSuchAccount);
        } catch (AccessDeniedException e) {
            req.getRequestDispatcher("/access-denied-error.jsp").forward(req, resp);
            return;
        } catch (ElementRemoveException e) {
            message = messageFactory.getMessage(MessageFactory.MessageType.AccountOperationError);
        }

        logger.info(message);
        req.setAttribute("message", message);

        req.getRequestDispatcher("Main?command=AllAccounts").forward(req, resp);
    }

}

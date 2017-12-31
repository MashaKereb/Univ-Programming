package command.account;

import command.Command;
import command.MessageFactory;
import entity.User;
import exceptions.AccessDeniedException;
import exceptions.ElementUpdateException;
import service.AccountService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * Created by Masha Kereb on 08-Jun-17.
 */
public class UnblockAccountCommand extends Command {
    @Override
    public void execute() throws ServletException, IOException {
        AccountService accountService = (AccountService) serviceFactory.getService("account");
        String message;

        try {
            User user = (User) req.getSession().getAttribute("user");
            if (!user.isAdmin()) {
                throw new AccessDeniedException();
            }
            int accountId = Integer.valueOf(req.getParameter("account_id"));
            accountService.unblockAccount(accountId);


            message = messageFactory.getMessage(MessageFactory.MessageType.Success);
            req.setAttribute("account_id", accountId);

        } catch (NullPointerException e) {
            message = messageFactory.getMessage(MessageFactory.MessageType.InvalidParameters);
        } catch (NoSuchElementException e) {
            message = messageFactory.getMessage(MessageFactory.MessageType.NoSuchAccount);
        } catch (ElementUpdateException e) {
            message = messageFactory.getMessage(MessageFactory.MessageType.AccountOperationError);
        } catch (AccessDeniedException e) {
            req.getRequestDispatcher("/access-denied-error.jsp").forward(req, resp);
            return;
        }

        // TODO: use transactions
        logger.info(message);
        req.setAttribute("message", message);

        req.getRequestDispatcher("/Main?command=AllAccounts").forward(req, resp);
    }
}

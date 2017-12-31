package command.account;

import command.Command;
import command.MessageFactory;
import entity.Account;
import entity.User;
import service.AccountService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by Masha Kereb on 24-Jun-17.
 */
public class AllAccountsCommand extends Command {
    @Override
    public void execute() throws ServletException, IOException {
        AccountService accountService = (AccountService) serviceFactory.getService("account");

        String message;
        String curAccount = req.getParameter("account_id");
        if (curAccount != null) {
            req.setAttribute("account_id", curAccount);
        }
        List<Account> accountList = null;
        try {
            String userId = req.getParameter("user_id");
            if (userId != null) {
                accountList = accountService.getAccountsForUser(Integer.parseInt(userId));
                req.setAttribute("user_id", userId);
            } else if (((User) req.getSession().getAttribute("user")).isAdmin()) {
                accountList = accountService.getAllAccounts();

            } else {
                Object user = req.getSession().getAttribute("user");
                int curUserId = ((User) user).getId();
                accountList = accountService.getAccountsForUser(curUserId);
            }

        } catch (NullPointerException e) {
            message = messageFactory.getMessage(MessageFactory.MessageType.InvalidParameters);
            logger.info(message);
            req.setAttribute("message", message);
        } catch (NoSuchElementException e) {
            message = messageFactory.getMessage(MessageFactory.MessageType.NoSuchAccount);
            logger.info(message);
            req.setAttribute("message", message);
        }

        req.setAttribute("accountList", accountList);

        req.getRequestDispatcher("/jsp/account-list.jsp").forward(req, resp);
    }
}

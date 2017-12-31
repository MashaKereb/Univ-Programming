package command.account;

import command.Command;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by Masha Kereb on 25-Jun-17.
 */
public class AccountCreationCommand extends Command {
    @Override
    public void execute() throws ServletException, IOException {
        String userId = req.getParameter("user_id");
        String accountId = req.getParameter("account_id");
        req.setAttribute("user_id", userId);
        req.setAttribute("account_id", accountId);
        req.getRequestDispatcher("/jsp/new-account.jsp").forward(req, resp);
    }
}

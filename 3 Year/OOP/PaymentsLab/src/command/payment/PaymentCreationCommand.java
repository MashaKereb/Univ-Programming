package command.payment;

import command.Command;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by Masha Kereb on 25-Jun-17.
 */
public class PaymentCreationCommand extends Command {
    @Override
    public void execute() throws ServletException, IOException {
        String accountId = req.getParameter("account_id");
        req.setAttribute("account_id", accountId);
        req.getRequestDispatcher("/jsp/new-payment.jsp").forward(req, resp);
    }
}

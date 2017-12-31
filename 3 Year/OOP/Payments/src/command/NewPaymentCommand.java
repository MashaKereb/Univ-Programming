package command;

import beans.Payment;
import database.AccountDAO;
import database.PaymentDAO;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by Masha Kereb on 08-Jun-17.
 */
public class NewPaymentCommand extends Command {
    @Override
    public void execute() throws ServletException, IOException {

        AccountDAO accountDAO = (AccountDAO)daoFactory.getDataAccessObject("account");

        try {
            Payment payment = new Payment(
                    Integer.valueOf(req.getParameter("amount")),
                    Payment.Type.valueOf(req.getParameter("type")),
                    accountDAO.getElem(Integer.valueOf(req.getParameter("account_id")))
            );

            PaymentDAO paymentDAO = (PaymentDAO)daoFactory.getDataAccessObject("payment");
            paymentDAO.addElem(payment);

            // TODO: add account.balance.modification
            // TODO: use transactions

        } catch (Exception e) {
            e.printStackTrace();
        }

        req.setAttribute("message", "success");
        // TODO: add more advanced messages

        //  req.getRequestDispatcher("/confirmPage.jsp").forward(req,resp);
        req.getRequestDispatcher("Main").forward(req,resp);

    }
}

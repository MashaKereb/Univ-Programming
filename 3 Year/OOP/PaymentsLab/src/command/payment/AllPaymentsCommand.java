package command.payment;

import command.Command;
import command.MessageFactory;
import entity.Account;
import entity.Payment;
import entity.User;
import exceptions.AccessDeniedException;
import service.AccountService;
import service.PaymentService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by Masha Kereb on 24-Jun-17.
 */
public class AllPaymentsCommand extends Command {

    @Override
    public void execute() throws ServletException, IOException {
        PaymentService paymentService = (PaymentService) serviceFactory.getService("payment");
        AccountService accountService = (AccountService) serviceFactory.getService("account");

        String message;
        List<Payment> paymentList = null;
        try {
            String strAccountId = req.getParameter("account_id");
            if (strAccountId != null) {

                int accountId = Integer.parseInt(strAccountId);
                Account account = accountService.getAccount(accountId);
                User curUser = (User) req.getSession().getAttribute("user");

                if (account != null && (curUser == account.getUser() || curUser.isAdmin())) {
                    paymentList = paymentService.getAllPaymentsForAccount(accountId);
                } else {
                    throw new AccessDeniedException();
                }

                req.setAttribute("account_id", strAccountId);
            } else if (((User) req.getSession().getAttribute("user")).isAdmin()) {
                paymentList = paymentService.getAllPayments();
            }
            String curPayment = req.getParameter("payment_id");
            if (curPayment != null) {
                req.setAttribute("payment_id", curPayment);
            }

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

        req.setAttribute("paymentList", paymentList);

        req.getRequestDispatcher("/jsp/payment-list.jsp").forward(req, resp);
    }
}

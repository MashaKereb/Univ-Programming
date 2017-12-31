package command.payment;

import command.Command;
import command.MessageFactory;
import entity.Account;
import entity.Payment;
import exceptions.BlockedAccountException;
import exceptions.ElementCreationException;
import exceptions.ElementUpdateException;
import exceptions.InsufficientFundsException;
import service.AccountService;
import service.PaymentService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * Created by Masha Kereb on 08-Jun-17.
 */
public class NewPaymentCommand extends Command {
    @Override
    public void execute() throws ServletException, IOException {

        PaymentService paymentService = (PaymentService) serviceFactory.getService("payment");
        AccountService accountService = (AccountService) serviceFactory.getService("account");

        String message;
        String accountIdStr = req.getParameter("account_id");

        try {
            Integer accountId = Integer.parseInt(accountIdStr);
            Account currentAccount = accountService.getAccount(accountId);

            Integer amount = Integer.valueOf(req.getParameter("amount"));
            Payment.Type type = Payment.Type.valueOf(req.getParameter("type"));

            Payment payment = new Payment(amount, type, currentAccount);
            int paymentId = paymentService.createNewPayment(payment);
            req.setAttribute("payment_id", paymentId);
            message = messageFactory.getMessage(MessageFactory.MessageType.SuccessPayment);


        } catch (NoSuchElementException e) {
            message = messageFactory.getMessage(MessageFactory.MessageType.NoSuchAccount);
        } catch (NumberFormatException e) {
            message = messageFactory.getMessage(MessageFactory.MessageType.InvalidParameters);
        } catch (InsufficientFundsException e) {
            message = messageFactory.getMessage(MessageFactory.MessageType.InsufficientFunds);
        } catch (BlockedAccountException e) {
            message = messageFactory.getMessage(MessageFactory.MessageType.AccountBlockedError);
        } catch (ElementCreationException | ElementUpdateException e) {
            message = messageFactory.getMessage(MessageFactory.MessageType.PaymentCreationError);
        }

        logger.info(message);
        req.setAttribute("message", message);

        req.getRequestDispatcher("Main?command=AllPayments&account_id=" + accountIdStr).forward(req, resp);

    }
}

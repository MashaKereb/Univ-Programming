package service;

import database.AccountDAO;
import database.DAOFactory;
import database.PaymentDAO;
import entity.Account;
import entity.Payment;
import exceptions.BlockedAccountException;
import exceptions.ElementCreationException;
import exceptions.ElementUpdateException;
import exceptions.InsufficientFundsException;

import java.util.List;

/**
 * Created by Masha Kereb on 26-Jun-17.
 */
public class PaymentService {
    static PaymentService instance;

    AccountDAO accountDao = (AccountDAO) DAOFactory.getInstance().getDataAccessObject("account");
    PaymentDAO paymentDao = (PaymentDAO) DAOFactory.getInstance().getDataAccessObject("payment");

    public static synchronized PaymentService getInstance() {
        if (instance == null) {
            instance = new PaymentService();
        }
        return instance;
    }

    protected PaymentService() {
    }

    public List<Payment> getAllPayments() {
        return paymentDao.getAllElems();
    }

    public List<Payment> getAllPaymentsForAccount(int accountId) {
        return paymentDao.getElemsForCriteria("account_id", String.valueOf(accountId));
    }

    public int createNewPayment(Payment payment) throws InsufficientFundsException, ElementUpdateException, ElementCreationException, BlockedAccountException {
        int amount = payment.getAmount();
        Account currentAccount = payment.getAccount();
        if (currentAccount.getStatus().equals(Account.Status.Blocked)) {
            throw new BlockedAccountException();
        }

        if (payment.getType() == Payment.Type.Withdrawal && currentAccount.getBalance() - amount < 0) {
            throw new InsufficientFundsException();
        }

        return paymentDao.createPaymentWithTransaction(payment);
    }
}

package service;

import database.AccountDAO;
import database.DAOFactory;
import entity.Account;
import exceptions.ElementCreationException;
import exceptions.ElementRemoveException;
import exceptions.ElementUpdateException;

import java.util.List;

/**
 * Created by Masha Kereb on 26-Jun-17.
 */
public class AccountService {
    static AccountService instance;
    AccountDAO accountDAO = (AccountDAO) DAOFactory.getInstance().getDataAccessObject("account");

    public static synchronized AccountService getInstance() {
        if (instance == null) {
            instance = new AccountService();
        }
        return instance;
    }

    protected AccountService() {
    }

    public Account getAccount(int accountId) {
        return accountDAO.getElem(accountId);
    }

    public int createAccount(Account account) throws ElementCreationException {
        return accountDAO.addElem(account);
    }

    public boolean deleteAccount(int accountId) throws ElementRemoveException {
        return accountDAO.deleteElem(accountId);
    }

    public List<Account> getAllAccounts() {
        return accountDAO.getAllElems();
    }

    public List<Account> getAccountsForUser(int userId) {
        return accountDAO.getElemsForCriteria("user_id", String.valueOf(userId));
    }

    public void blockAccount(int accountId) throws ElementUpdateException {
        Account account = accountDAO.getElem(accountId);
        account.setStatus(Account.Status.Blocked);
        accountDAO.updateElem(account);
    }

    public void unblockAccount(int accountId) throws ElementUpdateException {
        Account account = accountDAO.getElem(accountId);
        account.setStatus(Account.Status.Active);
        accountDAO.updateElem(account);
    }
}

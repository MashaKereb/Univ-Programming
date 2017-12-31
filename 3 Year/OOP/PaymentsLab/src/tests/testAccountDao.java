package tests;

import entity.Account;
import entity.User;
import database.AccountDAO;
import database.DAOFactory;
import database.UserDAO;
import org.junit.Assert;
import org.junit.Test;

import java.util.NoSuchElementException;

/**
 * Created by Masha Kereb on 12-Jun-17.
 */
public class testAccountDao {
    private AccountDAO db = (AccountDAO) DAOFactory.getInstance().getDataAccessObject("account");
    private UserDAO userdb = (UserDAO) DAOFactory.getInstance().getDataAccessObject("user");

    @Test
    public void testGetAll() throws Exception {
        Assert.assertTrue(db.getAllElems().size() > 1);
    }

    @Test
    public void testGetSingle() throws NoSuchElementException {
        Assert.assertEquals(db.getElem(1).getId(), 1);
        Assert.assertEquals(db.getElem(2).getId(), 2);
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetSingleWithException() throws NoSuchElementException {
        db.getElem(-1);

    }

    @Test
    public void testCreate() throws Exception {

        String number = "100-100-100";
        int balance = 100;
        User user = userdb.getElem(1);

        int index = db.addElem(new Account(number, balance, Account.Status.Active, user));

        Assert.assertEquals(db.getElem(index).getNumber(), number);
        Assert.assertEquals(db.getElem(index).getBalance(), balance);
        Assert.assertEquals(db.getElem(index).getUser(), user);

        Assert.assertTrue(db.deleteElem(index));

    }

    @Test
    public void testGetForCriteria() throws Exception {

        Account account = db.getElem(1);
        Account testAccount = db.getElemsForCriteria("number", account.getNumber()).get(0);

        Assert.assertEquals(testAccount.getNumber(), account.getNumber());
        Assert.assertEquals(testAccount.getId(), account.getId());
        Assert.assertEquals(testAccount.getUser(), account.getUser());
        Assert.assertEquals(testAccount.getBalance(), account.getBalance());

    }


    @Test
    public void testUserDaoUpdate() throws Exception{

        String number = "222-555-555";
        Account account = db.getElem(2);

        String oldNumber = account.getNumber();

        account.setNumber(number);
        db.updateElem(account);

        Account newAccount = db.getElemsForCriteria("number", number).get(0);

        Assert.assertEquals(newAccount.getId(), account.getId());
        Assert.assertEquals(newAccount.getNumber(), account.getNumber());
        Assert.assertEquals(newAccount.getBalance(), account.getBalance());

        account.setNumber(oldNumber);
        db.updateElem(account);

        newAccount = db.getElemsForCriteria("number", oldNumber).get(0);

        Assert.assertEquals(newAccount.getId(), account.getId());
        Assert.assertEquals(newAccount.getNumber(), account.getNumber());
        Assert.assertEquals(newAccount.getBalance(), account.getBalance());

    }
}

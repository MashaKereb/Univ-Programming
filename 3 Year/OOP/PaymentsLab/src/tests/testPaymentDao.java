package tests;

import entity.Account;
import entity.Payment;
import database.AccountDAO;
import database.DAOFactory;
import database.PaymentDAO;
import org.junit.Assert;
import org.junit.Test;

import java.util.NoSuchElementException;

/**
 * Created by Masha Kereb on 12-Jun-17.
 */
public class testPaymentDao {
    private PaymentDAO db = (PaymentDAO) DAOFactory.getInstance().getDataAccessObject("payment");
    private AccountDAO accountdb = (AccountDAO) DAOFactory.getInstance().getDataAccessObject("account");

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


        int amount = 10;
        Account account = accountdb.getElem(1);
        Payment.Type type = Payment.Type.Deposit;

        int index = db.addElem(new Payment(amount, type, account));

        Payment testPayment = db.getElem(index);
        Assert.assertEquals(testPayment.getAmount(), amount);
        Assert.assertEquals(testPayment.getAccount(), account);
        Assert.assertEquals(testPayment.getType(), type);

        Assert.assertTrue(db.deleteElem(index));

    }

    @Test
    public void testGetForCriteria() throws Exception {

        Payment payment = db.getElem(1);
        Payment testPayment = db.getElemsForCriteria("id", Integer.toString(payment.getId())).get(0);

        Assert.assertEquals(testPayment.getAmount(), payment.getAmount());
        Assert.assertEquals(testPayment.getId(), payment.getId());
        Assert.assertEquals(testPayment.getAccount(), payment.getAccount());
        Assert.assertEquals(testPayment.getType(), payment.getType());

    }


    @Test
    public void testUpdate() throws Exception{

        int amount = 20;
        Payment payment = db.getElem(2);

        int oldAmount = payment.getAmount();

        payment.setAmount(amount);
        db.updateElem(payment);

        Payment newPayment = db.getElemsForCriteria("amount", Integer.toString(amount)).get(0);

        Assert.assertEquals(newPayment.getId(), payment.getId());
        Assert.assertEquals(newPayment.getType(), payment.getType());
        Assert.assertEquals(newPayment.getAmount(), payment.getAmount());
        Assert.assertEquals(newPayment.getAccount(), payment.getAccount());

        payment.setAmount(oldAmount);
        db.updateElem(payment);

        newPayment = db.getElemsForCriteria("amount", Integer.toString(oldAmount)).get(0);

        Assert.assertEquals(newPayment.getId(), payment.getId());
        Assert.assertEquals(newPayment.getType(), payment.getType());
        Assert.assertEquals(newPayment.getAmount(), payment.getAmount());
        Assert.assertEquals(newPayment.getAccount(), payment.getAccount());

    }
}

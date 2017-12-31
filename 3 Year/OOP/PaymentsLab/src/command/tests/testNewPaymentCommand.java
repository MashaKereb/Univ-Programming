package command.tests;

import entity.Account;
import command.Command;
import command.payment.NewPaymentCommand;
import database.AccountDAO;
import database.DAOFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Masha Kereb on 12-Jun-17.
 */
public class testNewPaymentCommand {
    AccountDAO accountDAO = (AccountDAO) DAOFactory.getInstance().getDataAccessObject("account");

    HttpServletRequest testReq;
    HttpServletResponse testResp;
    RequestDispatcher testDispatcher;


    @Before
    public void initMocks() {
        testReq = Mockito.mock(HttpServletRequest.class);
        testResp = Mockito.mock(HttpServletResponse.class);
        testDispatcher = Mockito.mock(RequestDispatcher.class);

        Mockito.when(testReq.getRequestDispatcher(Mockito.any(String.class))).thenReturn(testDispatcher);
    }

    @Test
    public void testWithdrawal() throws ServletException, IOException {

        Mockito.when(testReq.getParameter("account_id")).thenReturn("1");
        Mockito.when(testReq.getParameter("amount")).thenReturn("10");
        Mockito.when(testReq.getParameter("type")).thenReturn("Withdrawal");

        Account accountBefore = accountDAO.getElem(1);

        Command com = new NewPaymentCommand();
        com.setReq(testReq);
        com.setResp(testResp);
        com.execute();

        Account accountAfter = accountDAO.getElem(1);
        Assert.assertEquals(accountBefore.getBalance() - 10, accountAfter.getBalance());


    }

    @Test
    public void testDeposit() throws ServletException, IOException {

        Mockito.when(testReq.getParameter("type")).thenReturn("Deposit");
        Mockito.when(testReq.getParameter("account_id")).thenReturn("1");
        Mockito.when(testReq.getParameter("amount")).thenReturn("10");

        Account accountBefore = accountDAO.getElem(1);

        Command com = new NewPaymentCommand();
        com.setReq(testReq);
        com.setResp(testResp);
        com.execute();

        Account accountAfter = accountDAO.getElem(1);
        Assert.assertEquals(accountBefore.getBalance() + 10, accountAfter.getBalance());

    }
}

package database;

import entity.Account;
import entity.Payment;
import exceptions.ElementCreationException;
import exceptions.ElementUpdateException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by Masha Kereb on 08-Jun-17.
 */
public class PaymentDAO extends DAO<Payment> {

    private AccountDAO accountDao = AccountDAO.getInstance();

    private static PaymentDAO instance;

    protected PaymentDAO(String tableName) {
        super(tableName);
    }

    static synchronized PaymentDAO getInstance() {
        if (instance == null) {
            instance = new PaymentDAO("payment");
        }
        return instance;
    }

    @Override
    public List<Payment> getAllElems() throws NoSuchElementException {
        try (Connection conn = connectionManager.getConnection()) {
            String request = "SELECT id, amount, \"type\", account_id FROM " + tableName;
            PreparedStatement statement = conn.prepareStatement(request);
            ResultSet result = statement.executeQuery();
            List<Payment> resultList = new ArrayList<>();
            while (result.next()) {
                Payment payment = new Payment(
                        result.getInt("id"),
                        result.getInt("amount"),
                        Payment.Type.valueOf(result.getString("type")),
                        accountDao.getElem(result.getInt("account_id"))
                );
                resultList.add(payment);
            }

            return resultList;
        } catch (SQLException e) {
            logger.warning("Cannot select all payments. \n" + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Payment> getElemsForCriteria(String field, String value) {
        try (Connection conn = connectionManager.getConnection()) {
            String request = "SELECT id, amount, \"type\", account_id FROM " + tableName +
                    " WHERE " + field + " = ?";
            PreparedStatement statement = conn.prepareStatement(request);

            try {
                statement.setInt(1, Integer.valueOf(value));
            } catch (NumberFormatException e) {
                statement.setString(1, value);
            }

            ResultSet result = statement.executeQuery();
            List<Payment> resultList = new ArrayList<>();
            while (result.next()) {
                Payment payment = new Payment(
                        result.getInt("id"),
                        result.getInt("amount"),
                        Payment.Type.valueOf(result.getString("type")),
                        accountDao.getElem(result.getInt("account_id"))
                );
                resultList.add(payment);
            }

            return resultList;
        } catch (SQLException e) {
            logger.warning("Cannot select all payments. \n" + e.getMessage());
            return new ArrayList<>();
        }
    }


    @Override
    public Payment getElem(int id) throws NoSuchElementException {
        try (Connection conn = connectionManager.getConnection()) {
            String request = "SELECT id, amount, \"type\", account_id FROM " + tableName + " WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            result.next();
            Payment payment = new Payment(
                    result.getInt("id"),
                    result.getInt("amount"),
                    Payment.Type.valueOf(result.getString("type")),
                    accountDao.getElem(result.getInt("account_id"), conn)
            );

            return payment;
        } catch (SQLException e) {
            String message = "Cannot select Payment with id " + id;
            logger.warning(message);
            throw new NoSuchElementException(message);
        }
    }


    private int addElem(Payment elem, Connection conn) throws ElementCreationException {
        try {
            String INSERT = "INSERT INTO " + tableName + "(amount, \"type\", account_id ) VALUES(?,CAST(? AS paymenttype),?)";
            PreparedStatement statement = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, elem.getAmount());
            statement.setString(2, elem.getType().toString());
            statement.setInt(3, elem.getAccount().getId());

            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            return rs.getInt("id");

        } catch (SQLException e) {
            String message = "Cannot create payment on account " + elem.getAccount().getId();
            logger.warning(message);
            throw new ElementCreationException(message);
        }
    }

    @Override
    public int addElem(Payment elem) throws ElementCreationException {
        try {
            try (Connection conn = connectionManager.getConnection()) {
                return addElem(elem, conn);
            }
        } catch (SQLException e) {
            String message = "Cannot create payment on account " + elem.getAccount().getId();
            logger.warning(message);
            throw new ElementCreationException(message);
        }
    }

    @Override
    public boolean updateElem(Payment elem) throws ElementUpdateException {
        try (Connection conn = connectionManager.getConnection()) {
            String INSERT = "UPDATE " + tableName + " SET amount = ?, " +
                    "\"type\" = CAST(? AS paymenttype), account_id = ? WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(INSERT);
            statement.setInt(1, elem.getAmount());
            statement.setString(2, elem.getType().toString());
            statement.setInt(3, elem.getAccount().getId());
            statement.setInt(4, elem.getId());
            if (statement.executeUpdate() > 0) {
                return true;
            } else return false;
        } catch (SQLException e) {
            String message = "Cannot update payment element " + elem.getId();
            logger.warning(message);
            throw new ElementUpdateException(message);
        }
    }

    public int createPaymentWithTransaction(Payment elem) throws ElementCreationException, ElementUpdateException {
        try (Connection conn = connectionManager.getConnection()) {
            conn.setAutoCommit(false);
            int index = addElem(elem, conn);

            AccountDAO accountDAO = (AccountDAO) DAOFactory.getInstance().getDataAccessObject("account");
            Account account = elem.getAccount();

            if (elem.getType() == Payment.Type.Withdrawal) {
                account.setBalance(account.getBalance() - elem.getAmount());
                if (account.getBalance() < 0) {
                    conn.rollback();
                } else {
                    accountDAO.updateElem(account);
                }
            } else {
                account.setBalance(account.getBalance() + elem.getAmount());
                accountDAO.updateElem(account);
            }
            Account modifiedAcc = accountDAO.getElem(account.getId(), conn);
            if (modifiedAcc.getBalance() == account.getBalance()) {
                conn.commit();
                return index;
            } else {
                conn.rollback();
                String message = "Cannot create payment on account " + elem.getAccount().getId();
                logger.warning(message);
                throw new ElementCreationException(message);
            }


        } catch (SQLException e) {
            String message = "Cannot create payment on account " + elem.getAccount().getId();
            logger.warning(message);
            throw new ElementCreationException(message);
        }
    }
}

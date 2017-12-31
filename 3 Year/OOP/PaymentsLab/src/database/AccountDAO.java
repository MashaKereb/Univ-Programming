package database;

import entity.Account;
import exceptions.ElementCreationException;
import exceptions.ElementUpdateException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by Masha Kereb on 08-Jun-17.
 */
public class AccountDAO extends DAO<Account> {
    private UserDAO userDao = UserDAO.getInstance();

    private static AccountDAO instance;


    protected AccountDAO(String tableName) {
        super(tableName);
    }

    static synchronized AccountDAO getInstance() {
        if (instance == null) {
            instance = new AccountDAO("account");
        }
        return instance;
    }

    @Override
    public List<Account> getAllElems() {
        try (Connection conn = connectionManager.getConnection()) {
            String request = "SELECT id, \"number\", balance, status, user_id FROM " + tableName;
            PreparedStatement statement = conn.prepareStatement(request);
            ResultSet result = statement.executeQuery();
            List<Account> resultList = new ArrayList<>();
            while (result.next()) {
                Account account = new Account(
                        result.getInt("id"),
                        result.getString("number"),
                        result.getInt("balance"),
                        Account.Status.valueOf(result.getString("status")),
                        userDao.getElem(result.getInt("user_id"))
                );
                resultList.add(account);
            }

            return resultList;
        } catch (SQLException e) {
            logger.warning("Cannot select all accounts.");
            return new ArrayList<>();
        }
    }

    @Override
    public List<Account> getElemsForCriteria(String field, String value) {
        try (Connection conn = connectionManager.getConnection()) {
            String request = "SELECT id, number, balance, status, user_id FROM " + tableName +
                    " WHERE " + field + " = ?";
            PreparedStatement statement = conn.prepareStatement(request);
            try {
                statement.setInt(1, Integer.valueOf(value));
            } catch (NumberFormatException e) {
                statement.setString(1, value);
            }

            ResultSet result = statement.executeQuery();
            List<Account> resultList = new ArrayList<>();
            while (result.next()) {
                Account account = new Account(
                        result.getInt("id"),
                        result.getString("number"),
                        result.getInt("balance"),
                        Account.Status.valueOf(result.getString("status")),
                        userDao.getElem(result.getInt("user_id"))
                );
                resultList.add(account);
            }

            return resultList;
        } catch (SQLException e) {
            logger.warning("Cannot select all accounts. \n" + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Account getElem(int id, Connection conn) throws NoSuchElementException {
        try{
            String request = "SELECT id, number, balance, status, user_id FROM " + tableName + " WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            result.next();
            Account account = new Account(
                    result.getInt("id"),
                    result.getString("number"),
                    result.getInt("balance"),
                    Account.Status.valueOf(result.getString("status")),
                    userDao.getElem(result.getInt("user_id"))
            );

            return account;
        } catch (SQLException e) {
            String message = "Cannot select account with id " + id;
            logger.warning(message);
            throw new NoSuchElementException(message);
        }
    }
    @Override
    public Account getElem(int id) throws NoSuchElementException {
        try (Connection conn = connectionManager.getConnection()) {
            return getElem(id, conn);
        } catch (SQLException e) {
            String message = "Cannot select account with id " + id;
            logger.warning(message);
            throw new NoSuchElementException(message);
        }
    }

    @Override
    public int addElem(Account elem) throws ElementCreationException {
        try (Connection conn = connectionManager.getConnection()) {
            String INSERT = "INSERT INTO " + tableName + "(\"number\", balance, status, user_id )" +
                    " VALUES(?, ?,CAST(? AS accountstatus),?)";
            PreparedStatement statement = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, elem.getNumber());
            statement.setInt(2, elem.getBalance());
            statement.setString(3, elem.getStatus().toString());
            statement.setInt(4, elem.getUser().getId());
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            return rs.getInt("id");

        } catch (SQLException e) {
            String message = "Cannot create payment from user " + elem.getUser().getId();
            logger.warning(message);
            throw new ElementCreationException(message);
        }
    }

    @Override
    public boolean updateElem(Account elem) throws ElementUpdateException {
        try (Connection conn = connectionManager.getConnection()) {
            String INSERT = "UPDATE " + tableName + " SET \"number\" = ?, balance = ?, " +
                    "status = CAST(? AS accountstatus), user_id = ? WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(INSERT);
            statement.setString(1, elem.getNumber());
            statement.setInt(2, elem.getBalance());
            statement.setString(3, elem.getStatus().toString());
            statement.setInt(4, elem.getUser().getId());
            statement.setInt(5, elem.getId());
            if (statement.executeUpdate() > 0) {
                return true;
            } else return false;
        } catch (SQLException e) {
            String message = "Cannot update account element " + elem.getId();
            logger.warning(message);
            throw new ElementUpdateException(message);
        }
    }
}

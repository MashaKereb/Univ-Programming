package database;

import beans.Account;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Masha Kereb on 08-Jun-17.
 */
public class AccountDAO extends DAO<Account> {
    private UserDAO userDao = UserDAO.getInstance();

    private static AccountDAO instance;
    private String tableName;


    protected AccountDAO(String tableName) {
        this.tableName = tableName;
    }

    static synchronized AccountDAO getInstance() {
        if (instance == null) {
            instance = new AccountDAO("account");
        }
        return instance;
    }

    @Override
    public List<Account> getAllElems() throws Exception {
        try (Connection conn = connectionManager.getConnection()) {
            String request = "SELECT id, number, balance, status, user_id FROM " + tableName;
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
            logger.warning("Cannot select all accounts. \n" + e.getMessage());
            throw e;
            // TODO: send new meaningful exception
        }
    }

    @Override
    public List<Account> getElemsForCriteria(String field, String value) throws Exception {
        try (Connection conn = connectionManager.getConnection()) {
            String request = "SELECT id, number, balance, status, user_id FROM" + tableName + "WHERE ? = ?";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.setString(1, field);
            statement.setString(2, value);

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
            throw e;
            // TODO: send new meaningful exception
        }
    }

    @Override
    public Account getElem(int id) throws Exception {
        try (Connection conn = connectionManager.getConnection()) {
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
            logger.warning("Cannot select account with id " + id);
            throw e;
        }
    }

    @Override
    public int addElem(Account elem) throws Exception {
        try (Connection conn = connectionManager.getConnection()) {
            String INSERT = "INSERT INTO" + tableName + "(number, balance, status, user_id )" +
                    " VALUES(?, ?,CAST(? AS accountstatus),?)";
            PreparedStatement statement = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, elem.getNumber());
            statement.setInt(2, elem.getBalance());
            statement.setString(3, elem.getStatus().toString());
            statement.setInt(4, elem.getUser().getId());
            if (statement.executeUpdate() > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                return rs.getInt("id");
            } else {
                logger.warning("Cannot create payment from user " + elem.getUser().getId());
                throw new SQLException("Cannot create account");
            }
        }
    }

    @Override
    public boolean updateElem(Account elem) throws Exception {
        try (Connection conn = connectionManager.getConnection()) {
            String INSERT = "UPDATE " + tableName + "SET number = ?, balance = ?, " +
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
            logger.warning("Cannot update account element " + elem.getId());
            throw e;
        }
    }
}

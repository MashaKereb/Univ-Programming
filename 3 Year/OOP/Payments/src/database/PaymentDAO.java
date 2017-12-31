package database;

import beans.Payment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Masha Kereb on 08-Jun-17.
 */
public class PaymentDAO extends DAO<Payment> {

    private AccountDAO accountDao = AccountDAO.getInstance();

    private static PaymentDAO instance;
    private String tableName;


    protected PaymentDAO(String tableName){
        this.tableName = tableName;
    }

    static synchronized PaymentDAO getInstance(){
        if (instance == null){
            instance = new PaymentDAO("payment");
        }
        return instance;
    }

    @Override
    public List<Payment> getAllElems() throws Exception {
        try (Connection conn = connectionManager.getConnection()) {
            String request="SELECT id, amount, \"type\", account_id FROM "+ tableName;
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
            throw e;
            // TODO: send new meaningful exception
        }
    }

    @Override
    public List<Payment> getElemsForCriteria(String field, String value) throws Exception {
        try (Connection conn = connectionManager.getConnection()) {
            String request="SELECT id, amount, \"type\", account_id FROM "+ tableName + "WHERE ? = ?";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.setString(1, field);
            statement.setString(2, value);

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
            throw e;
            // TODO: send new meaningful exception
        }
    }

    @Override
    public Payment getElem(int id) throws Exception {
        try (Connection conn = connectionManager.getConnection()) {
            String request="SELECT id, amount, \"type\", account_id FROM "+ tableName + " WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            result.next();
            Payment payment = new Payment(
                    result.getInt("id"),
                    result.getInt("amount"),
                    Payment.Type.valueOf(result.getString("type")),
                    accountDao.getElem(result.getInt("account_id"))
                );

            return payment;
        } catch (SQLException e) {
            logger.warning("Cannot select Payment with id " + id);
            throw e;
        }
    }

    @Override
    public int addElem(Payment elem) throws Exception {
        try (Connection conn = connectionManager.getConnection()) {
            String INSERT = "INSERT INTO" + tableName + "(amount, \"type\", account_id ) VALUES(?,CAST(? AS paymenttype),?)";
            PreparedStatement statement = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, elem.getAmount());
            statement.setString(2, elem.getType().toString());
            statement.setInt(3, elem.getAccount().getId());
            if (statement.executeUpdate() > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                return rs.getInt("id");
            } else {
                logger.warning("Cannot create payment on account " + elem.getAccount().getId());
                throw new SQLException("Cannot create payment");
            }
        }
    }

    @Override
    public boolean updateElem(Payment elem) throws Exception {
        try (Connection conn = connectionManager.getConnection()) {
            String INSERT = "UPDATE " + tableName + "SET amount = ?, " +
                    "\"type\" = CAST(? AS paymenttype), account_id =? WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(INSERT);
            statement.setInt(1, elem.getAmount());
            statement.setString(2, elem.getType().toString());
            statement.setInt(3, elem.getAccount().getId());
            statement.setInt(4, elem.getId());
            if (statement.executeUpdate() > 0) {
                return true;
            }
            else return false;
        }catch (SQLException e) {
            logger.warning("Cannot update payment element " + elem.getId());
            throw e;
        }
    }
}

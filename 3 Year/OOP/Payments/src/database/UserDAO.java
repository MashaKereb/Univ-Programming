package database;

import beans.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Masha Kereb on 08-Jun-17.
 */
public class UserDAO extends DAO<User> {

    private static UserDAO instance;
    private String tableName;


    protected UserDAO(String tableName) {
        this.tableName = tableName;
    }

    static synchronized UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO("users");
        }
        return instance;
    }

    @Override
    public List<User> getAllElems() throws Exception {
        try (Connection conn = connectionManager.getConnection()) {
            String request = "SELECT id, email, password, \"name\", surname, \"type\" FROM " + tableName;
            PreparedStatement statement = conn.prepareStatement(request);
            ResultSet result = statement.executeQuery();
            List<User> resultList = new ArrayList<>();
            while (result.next()) {
                User user = new User(
                        result.getInt("id"),
                        result.getString("email"),
                        result.getString("password"),
                        result.getString("name"),
                        result.getString("surname"),
                        User.Type.valueOf(result.getString("type"))
                );
                resultList.add(user);
            }

            return resultList;
        } catch (SQLException e) {
            logger.warning("Cannot select all user. \n" + e.getMessage());
            throw e;
            // TODO: send new meaningful exception
        }
    }

    @Override
    public List<User> getElemsForCriteria(String field, String value) throws Exception {
        try (Connection conn = connectionManager.getConnection()) {
            String request = "SELECT id, email, password, \"name\", surname, \"type\" FROM " + tableName + "WHERE ? = ?";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.setString(1, field);
            statement.setString(2, value);

            ResultSet result = statement.executeQuery();
            List<User> resultList = new ArrayList<>();
            while (result.next()) {
                User user = new User(
                        result.getInt("id"),
                        result.getString("email"),
                        result.getString("password"),
                        result.getString("name"),
                        result.getString("surname"),
                        User.Type.valueOf(result.getString("type"))
                );
                resultList.add(user);
            }

            return resultList;
        } catch (SQLException e) {
            logger.warning("Cannot select all users by criteria " + field + " = " + value + " \n" + e.getMessage());
            throw e;
            // TODO: send new meaningful exception
        }
    }

    @Override
    public User getElem(int id) throws Exception {
        try (Connection conn = connectionManager.getConnection()) {
            String request = "SELECT id, email, password, \"name\", surname, \"type\" FROM " + tableName + " WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            result.next();
            User user = new User(
                    result.getInt("id"),
                    result.getString("email"),
                    result.getString("password"),
                    result.getString("name"),
                    result.getString("surname"),
                    User.Type.valueOf(result.getString("type"))
            );

            return user;
        } catch (SQLException e) {
            logger.warning("Cannot select user with id " + id);
            throw e;
        }
    }

    @Override
    public int addElem(User elem) throws Exception {
        try (Connection conn = connectionManager.getConnection()) {
            String INSERT = "INSERT INTO" + tableName + "(email, password, \"name\", surname, \"type\" )" +
                    " VALUES(?, ?, ?, ?, CAST(? AS usertype))";
            PreparedStatement statement = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, elem.getEmail());
            statement.setString(2, elem.getPassword());
            statement.setString(3, elem.getName());
            statement.setString(4, elem.getSurname());
            statement.setString(5, elem.getType().toString());

            if (statement.executeUpdate() > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                return rs.getInt("id");
            } else {
                logger.warning("Cannot create user with email = " + elem.getEmail());
                throw new SQLException("Cannot create user");
            }
        }
    }

    @Override
    public boolean updateElem(User elem) throws Exception {
        try (Connection conn = connectionManager.getConnection()) {
            String INSERT = "UPDATE " + tableName + "SET email = ?, password = ?, " +
                    "\"name\" = ?, surname = ?, type = CAST(? AS usertype) WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(INSERT);
            statement.setString(1, elem.getEmail());
            statement.setString(2, elem.getPassword());
            statement.setString(3, elem.getName());
            statement.setString(4, elem.getSurname());
            statement.setString(5, elem.getType().toString());
            statement.setInt(6, elem.getId());
            if (statement.executeUpdate() > 0) {
                return true;
            } else return false;
        } catch (SQLException e) {
            logger.warning("Cannot update user element " + elem.getId());
            throw e;
        }
    }
}

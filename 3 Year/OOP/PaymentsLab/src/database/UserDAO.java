package database;

import entity.User;
import exceptions.ElementCreationException;
import exceptions.ElementUpdateException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by Masha Kereb on 08-Jun-17.
 */
public class UserDAO extends DAO<User> {

    private static UserDAO instance;


    protected UserDAO(String tableName) {
        super(tableName);
    }

    static synchronized UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO("users");
        }
        return instance;
    }

    @Override
    public List<User> getAllElems() {
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
            String message = "Cannot select all user.";
            logger.warning(message);
            return new ArrayList<>();
        }
    }

    @Override
    public List<User> getElemsForCriteria(String field, String value) {
        try (Connection conn = connectionManager.getConnection()) {
            String request = "SELECT id, email, password, \"name\", surname, \"type\" FROM " + tableName
                    + " WHERE " + field + " = ?";
            PreparedStatement statement = conn.prepareStatement(request);
            try {
                statement.setInt(1, Integer.valueOf(value));
            } catch (NumberFormatException e) {
                statement.setString(1, value);
            }

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
            return new ArrayList<>();
        }
    }

    @Override
    public User getElem(int id) throws NoSuchElementException {
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
            String message = "Cannot select user with id " + id;
            logger.warning(message);
            throw new NoSuchElementException(message);
        }
    }

    @Override
    public int addElem(User elem) throws ElementCreationException {
        try (Connection conn = connectionManager.getConnection()) {
            String INSERT = "INSERT INTO " + tableName + "(email, password, \"name\", surname, \"type\" )" +
                    " VALUES(?, ?, ?, ?, CAST(? AS usertype))";
            PreparedStatement statement = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, elem.getEmail());
            statement.setString(2, elem.getPassword());
            statement.setString(3, elem.getName());
            statement.setString(4, elem.getSurname());
            statement.setString(5, elem.getType().toString());

            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            return rs.getInt("id");

        } catch (SQLException e) {
            String message = "Cannot create user with email = " + elem.getEmail();
            logger.warning(message);
            throw new ElementCreationException(message);
        }
    }

    @Override
    public boolean updateElem(User elem) throws ElementUpdateException {
        try (Connection conn = connectionManager.getConnection()) {
            String INSERT = "UPDATE " + tableName + " SET email = ?, password = ?, " +
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
            String message = "Cannot update user element " + elem.getId();
            logger.warning(message);
            throw new ElementUpdateException(message);
        }
    }
}

package db;

import entities.User;
import service.IUserService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Masha Kereb on 21-May-17.
 */
public class DBUserService extends DBService<User> implements IUserService {
    String tableName = "\"User\"";
    DBGroupService dbGroupService = new DBGroupService();
    @Override
    public List<User> getAllElems() throws Exception {
        List<User> resList = new ArrayList<>();
        try (Connection conn = connectionManager.getConnection()){
            String request="SELECT id, name, email, status, role,  \"group\", created FROM " + tableName;
            PreparedStatement statement = conn.prepareStatement(request);
            ResultSet result = statement.executeQuery();
            while(result.next()){
                resList.add(
                        new User(result.getString("name"),
                                result.getString("email"),
                                User.Status.valueOf(result.getString("status")),
                                User.Role.valueOf(result.getString("role")),
                                dbGroupService.getElem(result.getInt("group")),
                                result.getDate("created"),
                                result.getInt("id"))
                );

            }
            return resList;
        } catch (SQLException e) {
            logger.warning("Error select all Users");
            throw e;
        }
    }

    @Override
    public User getElem(int id) throws Exception {
        try (Connection conn = connectionManager.getConnection()) {
            String request="SELECT id, name, email, status, role, \"group\", created FROM "+ tableName + " WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            result.next();
            User user = new User(result.getString("name"),
                    result.getString("email"),
                    User.Status.valueOf(result.getString("status")),
                    User.Role.valueOf(result.getString("role")),
                    dbGroupService.getElem(result.getObject("group")),
                    result.getDate("created"),
                    result.getInt("id"));

            return user;
        } catch (SQLException e) {
            logger.warning("Cannot select User with id " + id);
            throw e;
        }
    }

    @Override
    public User getElem(Object id) throws Exception {
        if(id != null)
            return this.getElem((int)id);
        else return null;
    }

    @Override
    public int addElem(User elem) throws Exception {
        try (Connection conn = connectionManager.getConnection()) {
            String INSERT = "INSERT INTO" + tableName + "(name, email, status, role, \"group\") VALUES(?,?,CAST(? AS status),CAST(? AS userrole),?)";
            PreparedStatement statement = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, elem.getName());
            statement.setString(2, elem.getEmail());
            statement.setString(3, elem.getStatus().toString());
            statement.setString(4, elem.getRole().toString());
            statement.setObject(5, (elem.getGroup() != null) ? elem.getGroup().getId() : null);
            if (statement.executeUpdate() > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                return rs.getInt("id");
            } else {
                logger.warning("Cannot insert User element " + elem.getName());
                throw new SQLException("Cannot create User element");
            }
        }
    }

    @Override
    public boolean updateElem(User elem) throws Exception {
        try (Connection conn = connectionManager.getConnection()){
            String request="UPDATE "+ tableName+" SET name = ?, email = ?, status = CAST(? AS status), role = CAST(? as userrole), \"group\" = ?  WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.setString(1, elem.getName());
            statement.setString(2, elem.getEmail());
            statement.setString(3, elem.getStatus().toString());
            statement.setString(4, elem.getRole().toString());
            statement.setObject(5, (elem.getGroup() != null) ? elem.getGroup().getId() : null);
            statement.setInt(6, elem.getId());
            if (statement.executeUpdate() > 0)
                return true;
            else return false;
        } catch (SQLException e) {
            logger.warning("Cannot update Group element " + elem.getName());
            throw e;
        }
    }

    @Override
    public boolean deleteElem(int id) throws Exception {
        try (Connection conn = connectionManager.getConnection()) {
            String request="DELETE FROM " + tableName + " WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.setInt(1, id);
            if (statement.executeUpdate() > 0)
                return true;
            else return false;
        } catch (SQLException e) {
            logger.warning("Cannot delete User with id " + id);
            throw e;
        }
    }

    @Override
    public User getRandomElem() throws Exception {
        try (Connection conn = connectionManager.getConnection()) {
            String request="SELECT id, name, email, status, role,  \"group\", created  FROM " + tableName +
                    " OFFSET floor(random()* (SELECT COUNT(*) FROM " + tableName +")) LIMIT 1;";
            PreparedStatement statement = conn.prepareStatement(request);
            ResultSet result = statement.executeQuery();
            result.next();
            User user = new User(result.getString("name"),
                    result.getString("email"),
                    User.Status.valueOf(result.getString("status")),
                    User.Role.valueOf(result.getString("role")),
                    dbGroupService.getElem(result.getObject("group")),
                    result.getDate("created"),
                    result.getInt("id"));
            return user;
        } catch (SQLException e) {
            logger.warning("Cannot select random User");
            logger.warning(e.getMessage());
            return  null;
        }
    }

    @Override
    public List<User> searchUsersByName(String name) throws SQLException {
        List<User> resList = new ArrayList<>();
        try (Connection conn = connectionManager.getConnection()){
            String request="SELECT id, name, email, status, role,  \"group\", created FROM " + tableName +
                    "WHERE name LIKE '%' || ? || '%'";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.setString(1, name);
            ResultSet result = statement.executeQuery();
            while(result.next()){
                resList.add(
                        new User(result.getString("name"),
                                result.getString("email"),
                                User.Status.valueOf(result.getString("status")),
                                User.Role.valueOf(result.getString("role")),
                                dbGroupService.getElem(result.getObject("group")),
                                result.getDate("created"),
                                result.getInt("id")));

            }
            return resList;
        } catch (SQLException e) {
            logger.warning("Error select all Users that match query "+name);
            throw e;
        }
    }
}

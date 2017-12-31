package db;

import entities.Group;
import service.IGroupService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Masha Kereb on 21-May-17.
 */
public class DBGroupService extends DBService<Group> implements IGroupService {
    String tableName = "\"Group\"";
    @Override
    public List<Group> getAllElems() throws SQLException {
        List<Group> resList = new ArrayList<>();
        try (Connection conn = connectionManager.getConnection()){
            String request="SELECT id, name, description, created FROM " + tableName;
            PreparedStatement statement = conn.prepareStatement(request);
            ResultSet result = statement.executeQuery();
            while(result.next()){
                resList.add(
                        new Group(result.getString("name"),
                                  result.getString("description"),
                                  result.getDate("created"),
                                  result.getInt("id"))
                );

            }
            return resList;
        } catch (SQLException e) {
            logger.warning("Error select all Groups");
            throw e;
        }
    }

    @Override
    public Group getElem(int id) throws SQLException {
        try (Connection conn = connectionManager.getConnection()) {
            String request="Select name, description, created  FROM "+ tableName + " WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            result.next();
            Group group = new Group(result.getString("name"), result.getString("description"),
                    result.getDate("created"), id);

            return group;
        } catch (SQLException e) {
            logger.warning("Cannot select Group with id " + id);
            return null;
        }
    }

    @Override
    public Group getElem(Object id) throws SQLException {
        if(id != null)
        return this.getElem((int)id);
        else return null;
    }

    @Override
    public int addElem(Group elem) throws SQLException {
        try (Connection conn = connectionManager.getConnection()) {
            String INSERT = "INSERT INTO" + tableName + "(name, description) VALUES(?,?)";
            PreparedStatement statement = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, elem.getName());
            statement.setString(2, elem.getDescription());
            if (statement.executeUpdate() > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                return rs.getInt("id");
            } else {
                logger.warning("Cannot insert Group element " + elem.getName());
                throw new SQLException("Cannot create Group element");
            }
        }
    }

    @Override
    public boolean updateElem(Group elem) throws SQLException {
        try (Connection conn = connectionManager.getConnection()){
            String request="UPDATE "+ tableName+" SET name = ?, description = ?  WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.setString(1, elem.getName());
            statement.setString(2, elem.getDescription());
            statement.setInt(3, elem.getId());
            if (statement.executeUpdate() > 0)
                return true;
            else return false;
        } catch (SQLException e) {
            logger.warning("Cannot update Group element " + elem.getName());
            throw e;
        }
    }

    @Override
    public boolean deleteElem(int id) throws SQLException {
        try (Connection conn = connectionManager.getConnection()) {
            String request="DELETE FROM " + tableName + " WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.setInt(1, id);
            if (statement.executeUpdate() > 0)
                return true;
            else return false;
        } catch (SQLException e) {
            logger.warning("Cannot delete Group with id " + id);
            throw e;
        }
    }

    @Override
    public Group getRandomElem(){
        try (Connection conn = connectionManager.getConnection()) {
            String request="SELECT id, name, description, created  FROM " + tableName +
                    " OFFSET floor(random()* (SELECT COUNT(*) FROM " + tableName +")) LIMIT 1;";
            PreparedStatement statement = conn.prepareStatement(request);
            ResultSet result = statement.executeQuery();
            result.next();
            Group group = new Group(result.getString("name"), result.getString("description"),
                    result.getDate("created"), result.getInt("id"));

            return group;
        } catch (SQLException e) {
            logger.warning("Cannot select random Group");
            logger.warning(e.getMessage());
            return  null;
        }
    }

    @Override
    public List<Group> searchGroupByNameAndDescription(String name) throws SQLException {
        List<Group> resList = new ArrayList<>();
        try (Connection conn = connectionManager.getConnection()){
            String request="SELECT id, name, description, created FROM " + tableName +
                    "WHERE name LIKE '%' || ? || '%' OR description LIKE '%' || ? || '%'";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.setString(1, name);
            statement.setString(2, name);
            ResultSet result = statement.executeQuery();
            while(result.next()){
                resList.add(
                        new Group(result.getString("name"),
                                result.getString("description"),
                                result.getDate("created"),
                                result.getInt("id"))
                );

            }
            return resList;
        } catch (SQLException e) {
            logger.warning("Error select all Groups that match query "+name);
            throw e;
        }
    }
}

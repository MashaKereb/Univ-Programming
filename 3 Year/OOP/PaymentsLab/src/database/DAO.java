package database;

import entity.Entity;
import exceptions.ElementRemoveException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Created by Masha Kereb on 21-May-17.
 */
public abstract class DAO<T extends Entity> implements IDAO<T> {
    protected String tableName;

    static Logger logger = Logger.getLogger("DB Service Logger");
    ConnectionManager connectionManager;

    DAO(String tableName, ConnectionManager connectionManager){
        this.tableName = tableName;
        this.connectionManager = connectionManager;
    }

    DAO(String tableName){
        connectionManager = ConnectionManager.getInstance();
        this.tableName = tableName;
    }

    protected String getTableName(){
        return tableName;
    }

    @Override
    public boolean deleteElem(int id) throws ElementRemoveException {
        try (Connection conn = connectionManager.getConnection()) {

            String request="DELETE FROM " + tableName + " WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            String message = "Cannot delete" + tableName + " with id " + id;
            logger.warning(message);
            throw new ElementRemoveException(message);
        }
    }
}

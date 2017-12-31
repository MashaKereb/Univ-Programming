package database;

import beans.Entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Created by Masha Kereb on 21-May-17.
 */
public abstract class DAO<T extends Entity> implements IDAO<T> {
    private String tableName = "";

    static Logger logger = Logger.getLogger("DB Service Logger");
    ConnectionManager connectionManager;

    DAO(ConnectionManager connectionManager){
     this.connectionManager = connectionManager;
    }

    DAO(){
        connectionManager = ConnectionManager.getInstance();
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
            logger.warning("Cannot delete" + tableName + " with id " + id);
            throw e;
        }
    }
}

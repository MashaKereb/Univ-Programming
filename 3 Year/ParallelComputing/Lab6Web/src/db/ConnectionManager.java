package db;

import org.postgresql.ds.PGConnectionPoolDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Created by Masha Kereb on 21-May-17.
 */
public class ConnectionManager {
    PGConnectionPoolDataSource source;
    private static ConnectionManager connectionManager;
    static Logger logger = Logger.getLogger("Connection manager logger");

    protected ConnectionManager(){
        source = new PGConnectionPoolDataSource();
        source.setServerName("localhost");
        source.setPortNumber(5432);
        source.setDatabaseName("postgres");
        source.setUser("postgres");
        source.setPassword("MarketData");
        source.setDefaultAutoCommit(true);

    }

    Connection getConnection() throws SQLException {
        return source.getConnection();
    }


    static ConnectionManager getInstance(){
        if (connectionManager == null)
            connectionManager = new ConnectionManager();
        return connectionManager;
    }
}

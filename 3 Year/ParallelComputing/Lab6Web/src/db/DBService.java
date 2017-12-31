package db;

import entities.Entity;
import service.IService;

import java.util.logging.Logger;

/**
 * Created by Masha Kereb on 21-May-17.
 */
public abstract class DBService<T extends Entity> implements IService<T>{
    static Logger logger = Logger.getLogger("DB Service Logger");
    ConnectionManager connectionManager;

    DBService(ConnectionManager connectionManager){
     this.connectionManager = connectionManager;
    }

    DBService(){
        connectionManager = ConnectionManager.getInstance();
    }
}

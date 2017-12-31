package service;

import entities.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Masha Kereb on 21-May-17.
 */
public interface IUserService  extends IService<User>{
    public List<User> searchUsersByName(String name) throws SQLException, Exception;
}

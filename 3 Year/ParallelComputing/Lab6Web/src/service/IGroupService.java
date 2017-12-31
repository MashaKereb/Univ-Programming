package service;

import entities.Group;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Masha Kereb on 21-May-17.
 */
public interface IGroupService extends IService<Group> {
    public List<Group> searchGroupByNameAndDescription(String name) throws SQLException, Exception;
}

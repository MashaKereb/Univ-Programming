package service;

import entities.Entity;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Masha Kereb on 21-May-17.
 */
public interface IService<T extends Entity> {
    public List<T> getAllElems() throws Exception;

    public T getElem(int id) throws Exception;
    public T getElem(Object id) throws SQLException, Exception;

    public int addElem(T elem) throws Exception;

    public boolean updateElem(T elem) throws Exception;

    public boolean deleteElem(int id) throws Exception;

    public T getRandomElem() throws Exception;
}

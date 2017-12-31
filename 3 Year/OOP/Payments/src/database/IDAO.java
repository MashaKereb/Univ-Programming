package database;

import beans.Entity;

import java.util.List;

/**
 * Created by Masha Kereb on 21-May-17.
 */
public interface IDAO<T extends Entity> {
    public List<T> getAllElems() throws Exception;

    public List<T> getElemsForCriteria(String field, String value) throws Exception;

    public T getElem(int id) throws Exception;

    public int addElem(T elem) throws Exception;

    public boolean updateElem(T elem) throws Exception;

    public boolean deleteElem(int id) throws Exception;

}

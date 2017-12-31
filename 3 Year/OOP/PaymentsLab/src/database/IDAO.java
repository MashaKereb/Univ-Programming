package database;

import entity.Entity;
import exceptions.ElementCreationException;
import exceptions.ElementRemoveException;
import exceptions.ElementUpdateException;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by Masha Kereb on 21-May-17.
 */
public interface IDAO<T extends Entity> {
    public List<T> getAllElems();

    public List<T> getElemsForCriteria(String field, String value);

    public T getElem(int id) throws NoSuchElementException;

    public int addElem(T elem) throws ElementCreationException;

    public boolean updateElem(T elem) throws ElementUpdateException;

    public boolean deleteElem(int id) throws ElementRemoveException;

}

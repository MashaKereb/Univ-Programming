package tests;

import database.DAOFactory;
import database.UserDAO;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Masha Kereb on 11-Jun-17.
 */
public class testDB {


    @Test
    public void testUserDaoGetAll() throws Exception {
        UserDAO db = (UserDAO) DAOFactory.getInstance().getDataAccessObject("user");

        Assert.assertTrue(db.getAllElems().size() > 1);

    }

    @Test
    public void testUserDaoGetSingle() throws Exception {
        UserDAO db = (UserDAO) DAOFactory.getInstance().getDataAccessObject("user");

        Assert.assertEquals(db.getElem(1).getId(), 1);
        Assert.assertEquals(db.getElem(2).getId(), 2);


    }
}

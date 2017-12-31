package tests;

import entity.User;
import database.DAOFactory;
import database.UserDAO;
import exceptions.ElementCreationException;
import org.junit.Assert;
import org.junit.Test;

import java.util.NoSuchElementException;

/**
 * Created by Masha Kereb on 11-Jun-17.
 */
public class testUserDao {
    private UserDAO db = (UserDAO) DAOFactory.getInstance().getDataAccessObject("user");

    @Test
    public void testGetAll() throws Exception {

        Assert.assertTrue(db.getAllElems().size() > 1);

    }

    @Test
    public void testGetSingle() throws NoSuchElementException {

        Assert.assertEquals(db.getElem(1).getId(), 1);
        Assert.assertEquals(db.getElem(2).getId(), 2);

    }

    @Test(expected = NoSuchElementException.class)
    public void testGetSingleWithException() throws NoSuchElementException {

        db.getElem(-1);

    }

    @Test(expected = ElementCreationException.class)
    public void testCreateWithException() throws Exception {

        db.addElem(db.getElem(1));

    }

    @Test
    public void testCreate() throws Exception {

        String name = "Jane", surname = "Jany", email = "jj@gmail.com", password = "1111";

        int index = db.addElem(new User(email, password, name, surname, User.Type.User));

        Assert.assertEquals(db.getElem(index).getName(), name);
        Assert.assertEquals(db.getElem(index).getSurname(), surname);

        Assert.assertTrue(db.deleteElem(index));

    }

    @Test
    public void testGetForCriteria() throws Exception {

        User user = db.getElem(1);
        User testUser = db.getElemsForCriteria("name", user.getName()).get(0);

        Assert.assertEquals(testUser.getEmail(), user.getEmail());
        Assert.assertEquals(testUser.getId(), user.getId());
        Assert.assertEquals(testUser.getSurname(), user.getSurname());

    }


    @Test
    public void testUserDaoUpdate() throws Exception{

        String name = "Jane";
        User user = db.getElem(2);
        String oldName = user.getName();

        user.setName(name);
        db.updateElem(user);

        Assert.assertEquals(db.getElemsForCriteria("name", name).get(0).getId(), user.getId());

        user.setName(oldName);
        db.updateElem(user);

        Assert.assertEquals(db.getElemsForCriteria("name", oldName).get(0).getId(), user.getId());

    }
}

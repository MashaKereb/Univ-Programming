package rmi;

import entities.Group;
import entities.User;

import java.rmi.Remote;
import java.util.List;

/**
 * Created by Masha Kereb on 28-May-17.
 */
public interface IServer extends Remote{
    int addNewUser(User user) throws Exception;

    boolean delUser(int id) throws Exception;

    boolean updateUser(User user) throws Exception;

    User selectUser(int id) throws Exception;

    List<User> selectAllUsers() throws Exception;

    List<User> searchUsers(String name) throws Exception;

    User randomUser() throws Exception;



    int addNewGroup(Group group) throws Exception;

    boolean delGroup(int id) throws Exception;

    boolean updateGroup(Group group) throws Exception;

    Group selectGroup(int id) throws Exception;

    List<Group> selectAllGroups() throws Exception;

    List<Group> searchGroups(String name) throws Exception;

    Group randomGroup() throws Exception;


}

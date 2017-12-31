package rmi;

import entities.Group;
import service.IGroupService;

import java.util.List;

/**
 * Created by Masha Kereb on 28-May-17.
 */
public class RMIGroupService implements IGroupService {
    IServer server = RMIServerRunner.getServerInstance();

    @Override
    public List<Group> getAllElems() throws Exception {
        return server.selectAllGroups();
    }

    @Override
    public Group getElem(int id) throws Exception {
        return server.selectGroup(id);
    }

    @Override
    public Group getElem(Object id) throws Exception {
        return server.selectGroup((int)id);
    }

    @Override
    public int addElem(Group elem) throws Exception {
        return server.addNewGroup(elem);
    }

    @Override
    public boolean updateElem(Group elem) throws Exception {
        return server.updateGroup(elem);
    }

    @Override
    public boolean deleteElem(int id) throws Exception {
        return server.delGroup(id);
    }

    @Override
    public Group getRandomElem() throws Exception {
        return server.randomGroup();
    }

    @Override
    public List<Group> searchGroupByNameAndDescription(String name) throws Exception {
        return server.searchGroups(name);
    }
}

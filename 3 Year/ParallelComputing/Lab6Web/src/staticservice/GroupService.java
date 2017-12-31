package staticservice;

import entities.Group;
import service.IGroupService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Masha Kereb on 20-May-17.
 */
public class GroupService extends Service<Group> implements IGroupService {
    public GroupService(List<Group> groupList){
        this.elementList = groupList;
    }

    public List<Group> searchGroupByNameAndDescription(String name) {
        Comparator<Group> groupByComparator = Comparator.comparing(Group::getName)
                .thenComparing(Group::getDescription);
        return elementList
                .stream()
                .filter(e -> e.getName().equalsIgnoreCase(name) ||  e.getDescription().contains(name))
                .sorted(groupByComparator)
                .collect(Collectors.toList());
    }

    @Override
    public Group getElem(Object id) {
        return this.getElem((int)id);
    }
}

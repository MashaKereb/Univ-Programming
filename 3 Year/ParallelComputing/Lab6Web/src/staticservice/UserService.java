package staticservice;

import entities.User;
import service.IUserService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Masha Kereb on 21-May-17.
 */
public class UserService extends Service<User> implements IUserService {


    public UserService(List<User> userList) {
        this.elementList = userList;
    }

    public List<User> searchUsersByName(String name) {
        Comparator<User> groupByComparator = Comparator.comparing(User::getName);
        return elementList
                .stream()
                .filter(e -> e.getName().toLowerCase().contains(name.toLowerCase()))
                .sorted(groupByComparator)
                .collect(Collectors.toList());
    }

    @Override
    public User getElem(Object id) {
        return this.getElem((int)id);
    }
}

package staticservice;

import entities.Entity;
import service.IService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;

/**
 * Created by Masha Kereb on 20-May-17.
 */

public class Service<T extends Entity> implements IService<T> {
    List<T> elementList;
    private Random randomGenerator = new Random();

    public void Service(List<T> elemList){
        this.elementList = elemList;
    }

    public List<T> getAllElems() {
        return elementList;
    }


    public T getElem(int id){
        Optional<T> match
                = elementList.stream()
                .filter(e -> e.getId() == id)
                .findFirst();
        return match.orElse(null);
    }

    @Override
    public T getElem(Object id) throws SQLException, Exception {
        return this.getElem((int)id);
    }

    public int addElem(T elem) {
        elementList.add(elem);
        return elem.getId();
    }

    public boolean updateElem(T elem) {
        int matchIdx;
        Optional<T> match = elementList.stream()
                .filter(e -> e.getId() == elem.getId())
                .findFirst();
        if (match.isPresent()) {
            matchIdx = elementList.indexOf(match.get());
            elementList.set(matchIdx, elem);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteElem(int id) {
        Predicate<T> elem = e -> e.getId() == id;
        if (elementList.removeIf(elem)) {
            return true;
        } else {
            return false;
        }
    }

    public T getRandomElem(){
        int index = randomGenerator.nextInt(elementList.size());
        return elementList.get(index);
    }

}

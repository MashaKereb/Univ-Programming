package mq;

import entities.Group;
import entities.User;
import service.IGroupService;
import service.IUserService;

import java.util.List;

/**
 * Created by Masha Kereb on 29-May-17.
 */
public class TestClientRunner {
    public static void main(String[] argv) {
        MQClient client = null;
        try {
            client = new MQClient();
            IGroupService gserv = new MQGroupService(client);
            IUserService userv = new MQUserService(client);
            List<Group> gres;
            while (true) {
                System.out.println(" [x] Requesting ");
                List<User> ures = userv.getAllElems();
                for (User user : ures
                        ) {
                    System.out.println(user);

                }
                Thread.sleep(3);
                System.out.println("------------------");
                gres = gserv.searchGroupByNameAndDescription("i");
                for (Group group : gres
                        ) {
                    System.out.println(group);

                }

                System.out.println("*********************************************************");
                Thread.sleep(30);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

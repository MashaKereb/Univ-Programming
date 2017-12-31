package entities;

import entities.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Masha Kereb on 20-May-17.
 */
public class GroupList {
    private static final List<Group> groupList = new ArrayList();

    static{
        groupList.add(new Group("Default","Default group for new users"));
        groupList.add(new Group("VipUsers","Very important users"));
        groupList.add(new Group("Managers","Staff, admins and managers"));
        groupList.add(new Group("Finance","Managers with access to the financial info"));
        groupList.add(new Group("ForApproval","Users for review"));
    }

    public static List <Group> getInstance(){
        return groupList;
    }
}

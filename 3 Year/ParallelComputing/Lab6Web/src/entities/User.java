package entities;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Masha Kereb on 19-May-17.
 */
public class User implements Entity, Serializable {
    public enum Role{
        User, Manager, Admin, Moderator, PremiumUser
    }
    public enum Status{
        New, Active, Staff, Blocked
    }
    private Date created;
    private Status status;
    private Role role;

    private Group group;

    private static final AtomicInteger counter = new AtomicInteger(200);

    public User(String name, String email, Status status, Role role, Group group) {
        this.name = name;
        this.email = email;
        this.status = status;
        this.role = role;
        this.group = group;
        this.created = new Date();
        this.id = counter.incrementAndGet();
    }

    public User(String name, String email, Status status, Role role, Group group, Date created, int id) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.created = created;
        this.status = status;
        this.role = role;
        this.group = group;
    }

    public User(String name, String email, Status status, Role role) {
        this(name, email, status, role, null);
    }

    private int id;
    private String name;
    private String email;

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public int getId() {
        return id;
    }

}

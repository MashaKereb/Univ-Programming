package entity;

import java.util.Objects;

/**
 * Created by Masha Kereb on 07-Jun-17.
 */
public class User implements Entity{
    public enum Type {
        User, Admin
    }
    private int id;
    private String email;
    private String password;
    private String name;
    private String surname;
    private Type type;

    public User(int id, String email, String password, String name, String surname, Type type) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.type = type;
    }
    public User(String email, String password, String name, String surname, Type type){
        this(-1, email, password, name, surname, type);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean checkPassword(String password){
        return Objects.equals(this.password, password);
    }

    public String getPassword() {
        return password;
    }
    public boolean isAdmin() {
        return type == Type.Admin;
    }

    @Override
    public boolean equals(Object other){
        User otherUser = (User)other;
        return otherUser.checkPassword(password) &&
                Objects.equals(otherUser.getEmail(), email) &&
                otherUser.getId() == id &&
                Objects.equals(otherUser.getName(), name) &&
                Objects.equals(otherUser.getSurname(), surname) &&
                otherUser.getType() == type;
    }
}

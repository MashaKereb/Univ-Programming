package entities;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Masha Kereb on 19-May-17.
 */
public class Group implements Entity, Serializable{
    private int id;
    private String name;
    private String description;
    private Date created;
    private static final AtomicInteger counter = new AtomicInteger(100);

    public Group(String name, String description) {
        this.id = counter.incrementAndGet();
        this.name = name;
        this.description = description;
        this.created = new Date();
    }

    public Group(String name, String description, Date created, int id) {
        this.id = id;
        this.created = created;
        this.name = name;
        this.description = description;
    }

    public Date getCreated(){
        return this.created;
    }

    String getStringCreated(){
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(created);
    }

    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

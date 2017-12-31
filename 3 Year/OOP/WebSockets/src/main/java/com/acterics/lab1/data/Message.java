package com.acterics.lab1.data;

public class Message {

    private String type;
    private String status;
    private String data = "";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Message: {" + "type = '" + type + " , status = '" + status + " , data = '" + data + '}';
    }
}

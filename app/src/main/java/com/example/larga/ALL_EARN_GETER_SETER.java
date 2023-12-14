package com.example.larga;

public class ALL_EARN_GETER_SETER {

    String myid,id,total;

    public ALL_EARN_GETER_SETER() {
    }

    public ALL_EARN_GETER_SETER(String myid, String id, String total) {
        this.myid = myid;
        this.id = id;
        this.total = total;
    }

    public String getMyid() {
        return myid;
    }

    public void setMyid(String myid) {
        this.myid = myid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}

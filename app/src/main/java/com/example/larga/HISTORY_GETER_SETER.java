package com.example.larga;

public class HISTORY_GETER_SETER {

    String myid,userid,name,contact,locationA,latA,lonA,locationB,latB,lonB,type;

    public HISTORY_GETER_SETER() {
    }

    public HISTORY_GETER_SETER(String myid, String userid, String name, String contact, String locationA, String latA, String lonA, String locationB, String latB, String lonB, String type) {
        this.myid = myid;
        this.userid = userid;
        this.name = name;
        this.contact = contact;
        this.locationA = locationA;
        this.latA = latA;
        this.lonA = lonA;
        this.locationB = locationB;
        this.latB = latB;
        this.lonB = lonB;
        this.type = type;
    }


    public String getMyid() {
        return myid;
    }

    public void setMyid(String myid) {
        this.myid = myid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLocationA() {
        return locationA;
    }

    public void setLocationA(String locationA) {
        this.locationA = locationA;
    }

    public String getLatA() {
        return latA;
    }

    public void setLatA(String latA) {
        this.latA = latA;
    }

    public String getLonA() {
        return lonA;
    }

    public void setLonA(String lonA) {
        this.lonA = lonA;
    }

    public String getLocationB() {
        return locationB;
    }

    public void setLocationB(String locationB) {
        this.locationB = locationB;
    }

    public String getLatB() {
        return latB;
    }

    public void setLatB(String latB) {
        this.latB = latB;
    }

    public String getLonB() {
        return lonB;
    }

    public void setLonB(String lonB) {
        this.lonB = lonB;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

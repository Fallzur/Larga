package com.example.larga;

public class User_geterSeter {

    String id,name,password,email,contact,typeUser,typeMotor,lat,lon,address;

    public User_geterSeter() {
    }

    public User_geterSeter(String id, String name, String password, String email, String contact, String typeUser, String typeMotor, String lat, String lon, String address) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.contact = contact;
        this.typeUser = typeUser;
        this.typeMotor = typeMotor;
        this.lat = lat;
        this.lon = lon;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(String typeUser) {
        this.typeUser = typeUser;
    }

    public String getTypeMotor() {
        return typeMotor;
    }

    public void setTypeMotor(String typeMotor) {
        this.typeMotor = typeMotor;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

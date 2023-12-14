package com.example.larga;

public class ALL_REQEUST_GETER_SETER {

    String id,name,contact,locationA,latA,lonA,locationB,latB,lonB,paymentType,motorType,IdOfFavoriteDriver,papalitInfo,total,status;

    public ALL_REQEUST_GETER_SETER() {

    }

    public ALL_REQEUST_GETER_SETER(String id, String name, String contact, String locationA, String latA, String lonA, String locationB, String latB, String lonB, String paymentType, String motorType, String idOfFavoriteDriver, String papalitInfo, String total, String status) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.locationA = locationA;
        this.latA = latA;
        this.lonA = lonA;
        this.locationB = locationB;
        this.latB = latB;
        this.lonB = lonB;
        this.paymentType = paymentType;
        this.motorType = motorType;
        IdOfFavoriteDriver = idOfFavoriteDriver;
        this.papalitInfo = papalitInfo;
        this.total = total;
        this.status = status;
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

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getMotorType() {
        return motorType;
    }

    public void setMotorType(String motorType) {
        this.motorType = motorType;
    }

    public String getIdOfFavoriteDriver() {
        return IdOfFavoriteDriver;
    }

    public void setIdOfFavoriteDriver(String idOfFavoriteDriver) {
        IdOfFavoriteDriver = idOfFavoriteDriver;
    }

    public String getPapalitInfo() {
        return papalitInfo;
    }

    public void setPapalitInfo(String papalitInfo) {
        this.papalitInfo = papalitInfo;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

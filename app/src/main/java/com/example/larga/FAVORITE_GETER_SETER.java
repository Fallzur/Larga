package com.example.larga;

public class FAVORITE_GETER_SETER {



    String myid,idOfDriver,nameOfDriver,numberOfDriver,status ,availavility;
    public FAVORITE_GETER_SETER() {
    }

    public String getMyid() {
        return myid;
    }

    public void setMyid(String myid) {
        this.myid = myid;
    }

    public String getIdOfDriver() {
        return idOfDriver;
    }

    public void setIdOfDriver(String idOfDriver) {
        this.idOfDriver = idOfDriver;
    }

    public String getNameOfDriver() {
        return nameOfDriver;
    }

    public void setNameOfDriver(String nameOfDriver) {
        this.nameOfDriver = nameOfDriver;
    }

    public String getNumberOfDriver() {
        return numberOfDriver;
    }

    public void setNumberOfDriver(String numberOfDriver) {
        this.numberOfDriver = numberOfDriver;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAvailavility() {
        return availavility;
    }

    public void setAvailavility(String availavility) {
        this.availavility = availavility;
    }

    public FAVORITE_GETER_SETER(String myid, String idOfDriver, String nameOfDriver, String numberOfDriver, String status, String availavility) {
        this.myid = myid;
        this.idOfDriver = idOfDriver;
        this.nameOfDriver = nameOfDriver;
        this.numberOfDriver = numberOfDriver;
        this.status = status;
        this.availavility = availavility;
    }




}

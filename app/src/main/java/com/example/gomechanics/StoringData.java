package com.example.gomechanics;

public class StoringData {
    String name,vehical,contact,password,address,usertype;

    public StoringData(String name, String vehical, String contact, String password, String address ,String usertype) {
        this.name = name;
        this.vehical = vehical;
        this.contact = contact;
        this.password = password;
        this.address = address;
        this.usertype = usertype;


    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVehical() {
        return vehical;
    }

    public void setVehical(String vehical) {
        this.vehical = vehical;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}


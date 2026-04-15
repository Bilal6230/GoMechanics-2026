package com.example.gomechanics;

public class StoringDataMechanic {
    String name,vehical,contact,password,age,usertype,imageUrl;

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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public StoringDataMechanic(String name, String vehical, String contact, String password, String age, String usertype) {
        this.name = name;
        this.vehical = vehical;
        this.contact = contact;
        this.password = password;
        this.age = age;
        this.usertype = usertype;
    }
    public StoringDataMechanic(String name, String vehical, String contact, String password, String age, String usertype, String imageUrl) {
        this.name = name;
        this.vehical = vehical;
        this.contact = contact;
        this.password = password;
        this.age = age;
        this.usertype = usertype;
        this.imageUrl = imageUrl;
    }
}

package com.example.gomechanics;

public class neworderslist {
    String orderaddress,ordercontact,orderbudget,orderissues,contact;

    public neworderslist(String orderaddress, String ordercontact, String orderbudget, String orderissues,String contact) {
        this.orderaddress = orderaddress;
        this.ordercontact = ordercontact;
        this.orderbudget = orderbudget;
        this.orderissues = orderissues;
        this.contact = contact;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getOrderaddress() {
        return orderaddress;
    }

    public void setOrderaddress(String orderaddress) {
        this.orderaddress = orderaddress;
    }

    public String getOrdercontact() {
        return ordercontact;
    }

    public void setOrdercontact(String ordercontact) {
        this.ordercontact = ordercontact;
    }

    public String getOrderbudget() {
        return orderbudget;
    }

    public void setOrderbudget(String orderbudget) {
        this.orderbudget = orderbudget;
    }

    public String getOrderissues() {
        return orderissues;
    }

    public void setOrderissues(String orderissues) {
        this.orderissues = orderissues;
    }
}

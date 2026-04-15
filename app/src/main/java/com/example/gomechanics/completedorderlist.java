package com.example.gomechanics;

public class completedorderlist {
    String address, contact, max, mcontact, min, mname, vehicalIssues, vehicaleDec;

    public completedorderlist(String address, String contact, String max, String mcontact, String min, String mname, String vehicalIssues, String vehicaleDec) {
        this.address = address;
        this.contact = contact;
        this.max = max;
        this.mcontact = mcontact;
        this.min = min;
        this.mname = mname;
        this.vehicalIssues = vehicalIssues;
        this.vehicaleDec = vehicaleDec;
    }
    public completedorderlist()
    {

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMcontact() {
        return mcontact;
    }

    public void setMcontact(String mcontact) {
        this.mcontact = mcontact;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getVehicalIssues() {
        return vehicalIssues;
    }

    public void setVehicalIssues(String vehicalIssues) {
        this.vehicalIssues = vehicalIssues;
    }

    public String getVehicaleDec() {
        return vehicaleDec;
    }

    public void setVehicaleDec(String vehicaleDec) {
        this.vehicaleDec = vehicaleDec;
    }
}

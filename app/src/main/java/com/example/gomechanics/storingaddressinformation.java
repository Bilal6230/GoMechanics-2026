package com.example.gomechanics;

public class storingaddressinformation {
    String VehicaleDec,VehicalIssues,contact,min,address,max,Mcontact,Mname;

    public storingaddressinformation(String vehicaleDec, String vehicalIssues, String contact,  String address, String min,String max,String Mcontact,String Mname) {
        VehicaleDec = vehicaleDec;
        VehicalIssues = vehicalIssues;
        this.contact = contact;
        this.min = min;
        this.address = address;
        this.max = max;
        this.Mcontact = Mcontact;
        this.Mname = Mname;
    }

    public String getMname() {
        return Mname;
    }

    public void setMname(String mname) {
        Mname = mname;
    }

    public String getMcontact() {
        return Mcontact;
    }

    public void setMcontact(String mcontact) {
        Mcontact = mcontact;
    }

    public String getVehicaleDec() {
        return VehicaleDec;
    }

    public void setVehicaleDec(String vehicaleDec) {
        VehicaleDec = vehicaleDec;
    }

    public String getVehicalIssues() {
        return VehicalIssues;
    }

    public void setVehicalIssues(String vehicalIssues) {
        VehicalIssues = vehicalIssues;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }
}

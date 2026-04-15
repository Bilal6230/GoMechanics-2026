package com.example.gomechanics;

import android.widget.TextView;

public class priviousbookinglist {
    String VehicaleDec,VehicalIssues,Vehicalecontact,Mname;

    public priviousbookinglist(String vehicaleDec, String vehicalIssues, String Vehicalecontact,String Mname) {
        VehicaleDec = vehicaleDec;
        VehicalIssues = vehicalIssues;
        this.Vehicalecontact = Vehicalecontact;
        this.Mname = Mname;

    }

    public String getVehicalecontact() {
        return Vehicalecontact;
    }

    public void setVehicalecontact(String vehicalecontact) {
        Vehicalecontact = vehicalecontact;
    }

    public String getMname() {
        return Mname;
    }

    public void setMname(String mname) {
        Mname = mname;
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
        return Vehicalecontact;
    }

    public void setContact(String contact) {
        this.Vehicalecontact = contact;
    }

}

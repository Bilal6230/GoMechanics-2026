package com.example.gomechanics;

public class Mechanic {
    String name, age, expert , contact;


    public Mechanic(String name, String age, String expert, String contact) {
        this.name = name;
        this.age = age;
        this.expert = expert;
        this.contact = contact;
    }

    public String getContact() {
        return contact;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getExpert() {
        return expert;
    }
}

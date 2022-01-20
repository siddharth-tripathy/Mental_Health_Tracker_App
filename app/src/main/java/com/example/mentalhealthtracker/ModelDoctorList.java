package com.example.mentalhealthtracker;

public class ModelDoctorList {

    String Name, UID;

    public ModelDoctorList() {
    }

    public ModelDoctorList(String name, String UID) {
        Name = name;
        this.UID = UID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}

package com.example.mentalhealthtracker;

public class ModelRequestList {
    String NameUser, PatientId;

    public ModelRequestList() {
    }

    public ModelRequestList(String nameUser, String patientId) {
        NameUser = nameUser;
        PatientId = patientId;
    }

    public String getNameUser() {
        return NameUser;
    }

    public void setNameUser(String nameUser) {
        NameUser = nameUser;
    }

    public String getPatientId() {
        return PatientId;
    }

    public void setPatientId(String patientId) {
        PatientId = patientId;
    }
}

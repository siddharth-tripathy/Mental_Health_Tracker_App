package com.example.mentalhealthtracker;

public class ModelDoctorPatientList {
    String NameUser, AppointmentDate, PatientId;

    public ModelDoctorPatientList() {
    }

    public ModelDoctorPatientList(String nameUser, String appointmentDate, String patientId) {
        NameUser = nameUser;
        AppointmentDate = appointmentDate;
        PatientId = patientId;
    }

    public String getNameUser() {
        return NameUser;
    }

    public void setNameUser(String nameUser) {
        NameUser = nameUser;
    }

    public String getAppointmentDate() {
        return AppointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        AppointmentDate = appointmentDate;
    }

    public String getPatientId() {
        return PatientId;
    }

    public void setPatientId(String patientId) {
        PatientId = patientId;
    }
}
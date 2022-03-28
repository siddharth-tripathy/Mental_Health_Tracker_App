package com.example.mentalhealthtracker;

import androidx.recyclerview.widget.RecyclerView;

public class ModelUserAppoinmentHistory {
    String NameDoctor, AppointmentDate, AppointmentTime, Time, timeStamp;

    public ModelUserAppoinmentHistory() {
    }

    public ModelUserAppoinmentHistory(String nameDoctor, String appointmentDate, String appointmentTime, String time, String timeStamp) {
        NameDoctor = nameDoctor;
        AppointmentDate = appointmentDate;
        AppointmentTime = appointmentTime;
        Time = time;
        this.timeStamp = timeStamp;
    }

    public String getNameDoctor() {
        return NameDoctor;
    }

    public void setNameDoctor(String nameDoctor) {
        NameDoctor = nameDoctor;
    }

    public String getAppointmentDate() {
        return AppointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        AppointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return AppointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        AppointmentTime = appointmentTime;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}

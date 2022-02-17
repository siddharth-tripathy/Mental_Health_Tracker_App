package com.example.mentalhealthtracker;

public class ModelTrackResultD {
    String Result, Date, DateTS;

    public ModelTrackResultD() {
    }

    public ModelTrackResultD(String result, String date, String dateTS) {
        Result = result;
        Date = date;
        DateTS = dateTS;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDateTS() {
        return DateTS;
    }

    public void setDateTS(String dateTS) {
        DateTS = dateTS;
    }
}

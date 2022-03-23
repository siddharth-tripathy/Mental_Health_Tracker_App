package com.example.mentalhealthtracker;

public class ModelChatList {
    String By, TimeStamp;

    public ModelChatList() {
    }

    public ModelChatList(String by, String timeStamp) {
        By = by;
        TimeStamp = timeStamp;
    }

    public String getBy() {
        return By;
    }

    public void setBy(String by) {
        By = by;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }
}

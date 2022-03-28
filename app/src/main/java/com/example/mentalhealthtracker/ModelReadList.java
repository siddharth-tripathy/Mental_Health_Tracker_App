package com.example.mentalhealthtracker;

public class ModelReadList {
    String UID, URL, Name;

    public ModelReadList() {
    }

    public ModelReadList(String UID, String URL, String name) {
        this.UID = UID;
        this.URL = URL;
        Name = name;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}

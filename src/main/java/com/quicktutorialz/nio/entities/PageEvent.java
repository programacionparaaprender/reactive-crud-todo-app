package com.quicktutorialz.nio.entities;

import java.util.Date;

public class PageEvent {
    private String name;
    private String user;
    private Date date;
    private int duration;

    public PageEvent(String name, String user, Date date, int duration) {
        this.name = name;
        this.user = user;
        this.date = date;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
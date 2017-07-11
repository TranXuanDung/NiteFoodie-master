package com.t3h.nitefoodie.model;

public class TimeStore {
    private int hour;
    private int minute;

    public TimeStore(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }
}

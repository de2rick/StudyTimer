package com.example.studytimer;

import java.io.Serializable;

public class TimerModel implements Serializable {
    private String showtime;
    private String  realtime;
    private double second;

    public TimerModel() {
    }

    public TimerModel(String showtime, String  currenttime, double second) {
        this.showtime = showtime;
        this.realtime = currenttime;
        this.second = second;
    }

    public String getTaskName() {
        return showtime;
    }

    public String getTaskAddedTime() {
        return realtime;
    }
}

package com.example.studytimer;

import java.io.Serializable;

public class TimerModel implements Serializable {
    private String showtime;
    private double  realtime;

    public TimerModel() {
    }

    public TimerModel(String showtime, double  taskAddedTime) {
        this.showtime = showtime;
        this.realtime = taskAddedTime;
    }

    public String getTaskName() {
        return showtime;
    }

    public double getTaskAddedTime() {
        return realtime;
    }
}

package de.inces.hackathonrweapp;

/**
 * Created by Koerfer on 20.11.2015.
 */
public class BatteryHistoryPoint {

    private int Time;
    private int Percentage;

    public BatteryHistoryPoint(int time, int percentage) {
        this.Time = time;
        this.Percentage = percentage;
    }

    public int getTime() {
        return Time;
    }

    public int getPercentage() {
        return Percentage;
    }
}

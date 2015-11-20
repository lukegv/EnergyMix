package de.inces.hackathonrweapp;

/**
 * Created by Koerfer on 20.11.2015.
 */
public class BatteryHistoryPoint {

    private String DateTime;
    private int Percentage;

    public BatteryHistoryPoint(String datetime, int percentage) {
        this.DateTime = datetime;
        this.Percentage = percentage;
    }

    public String getTime() {
        return this.DateTime;
    }

    public int getPercentage() {
        return Percentage;
    }
}

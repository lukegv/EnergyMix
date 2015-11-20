package de.inces.hackathonrweapp.batteryDataRecord;

import android.os.SystemClock;

import java.util.Calendar;

/**
 * Created by Koerfer on 19.11.2015.
 */
public class BatteryDataPoint {

    private int LoadPercentage;
    private boolean IsCharging;
    private String ChargingMode;

    public BatteryDataPoint(int percentage, boolean isCharging, String chargingMode) {
        this.LoadPercentage = percentage;
        this.IsCharging = isCharging;
        this.ChargingMode = chargingMode;
    }

    public int getLoadPercentage() {
        return this.LoadPercentage;
    }

    public boolean isCharging() {
        return this.IsCharging;
    }

    public String getChargingMode() {
        return this.ChargingMode;
    }
}

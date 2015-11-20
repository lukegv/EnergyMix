package de.inces.hackathonrweapp.batteryDataRecord;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.BatteryManager;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by Koerfer on 19.11.2015.
 */
public class BatteryUpdateReceiver extends BroadcastReceiver {

    final static String ENERGY_UPDATE_ACTION = "de.inces.hackathonrweapp.ENERGY_UPDATE_ACTION";

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case Intent.ACTION_BOOT_COMPLETED:
                // Register PendingIntent on every reboot
                BatteryUpdateReceiver.registerUpdate(context);
                break;
            case ENERGY_UPDATE_ACTION:
                // Save battery data to database
                BatteryDataPoint data = getCurrentEnergyData(context);
                this.addBatteryDataToDb(context, data);
                break;
            case Intent.ACTION_POWER_CONNECTED:

                break;
            case Intent.ACTION_POWER_DISCONNECTED:

                break;
        }
    }

    private BatteryDataPoint getCurrentEnergyData(Context context) {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent lastBattery = context.getApplicationContext().registerReceiver(null, filter);
        int current = lastBattery.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int maximal = lastBattery.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int percentage = (int)(((float) current / (float) maximal) * 100);
        int status = lastBattery.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;
        String mode = "None";
        if (isCharging) {
            int chargeMode = lastBattery.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            if (chargeMode == BatteryManager.BATTERY_PLUGGED_USB) {
                mode = "Usb";
            }
            if (chargeMode == BatteryManager.BATTERY_PLUGGED_AC) {
                mode = "Ac";
            }
            if (chargeMode == BatteryManager.BATTERY_PLUGGED_WIRELESS) {
                mode = "Wireless";
            }
        }
        return new BatteryDataPoint(percentage, isCharging, mode);
    }

    public void addBatteryDataToDb(Context context, BatteryDataPoint data) {
        SQLiteDatabase db = (new BatteryDB(context)).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BatteryDB.PERCENTAGE, data.getLoadPercentage());
        values.put(BatteryDB.CHARGING, data.isCharging());
        values.put(BatteryDB.CHARGE_MODE, data.getChargingMode());
        db.insert(BatteryDB.UPDATE_TABLE, null, values);
        db.close();
    }

    public static void registerUpdate(Context context) {
        Intent updateIntent = new Intent(ENERGY_UPDATE_ACTION);
        PendingIntent pending = PendingIntent.getBroadcast(context.getApplicationContext(), 0, updateIntent, PendingIntent.FLAG_NO_CREATE);

        if (pending == null) {
            Log.e("HRA", "Register Pending Intent");
            pending = PendingIntent.getBroadcast(context.getApplicationContext(), 0, updateIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager manager = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 100, 1000 * 60 * 5, pending);
        }
    }
}

package de.inces.hackathonrweapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Koerfer on 19.11.2015.
 */
public class BatteryDB extends SQLiteOpenHelper {

    final static String UPDATE_TABLE = "batterydata";

    final static String ID = "id";
    final static String DATE_TIME = "datetime";
    final static String PERCENTAGE = "percentage";
    final static String CHARGING = "charging";
    final static String CHARGE_MODE = "chargemode";

    public BatteryDB(Context context) {
        super(context, "BatteryDB", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createQuery = "CREATE TABLE "
                + UPDATE_TABLE + " ("
                + ID + " INTEGER PRIMARY KEY, "
                + DATE_TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
                + PERCENTAGE + " TINYINT, "
                + CHARGING + " BOOLEAN, "
                + CHARGE_MODE + " TEXT)";
        db.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

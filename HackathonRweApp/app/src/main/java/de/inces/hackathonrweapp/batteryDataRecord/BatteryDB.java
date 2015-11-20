package de.inces.hackathonrweapp.batteryDataRecord;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Koerfer on 19.11.2015.
 */
public class BatteryDB extends SQLiteOpenHelper {

    public final static String UPDATE_TABLE = "batterydata";

    public final static String ID = "id";
    public final static String DATE_TIME = "datetime";
    public final static String PERCENTAGE = "percentage";
    public final static String CHARGING = "charging";
    public final static String CHARGE_MODE = "chargemode";

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

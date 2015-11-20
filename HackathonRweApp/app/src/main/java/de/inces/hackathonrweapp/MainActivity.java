package de.inces.hackathonrweapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import de.inces.hackathonrweapp.batteryActivity.BatteryActivity;
import de.inces.hackathonrweapp.batteryActivity.BatteryHistoryPoint;
import de.inces.hackathonrweapp.batteryDataRecord.BatteryDB;
import de.inces.hackathonrweapp.batteryDataRecord.BatteryUpdateReceiver;

public class MainActivity extends AppCompatActivity {

    private LinearLayout layoutGeneral;

    private TextView txtBatteryPercentage;
    private ImageView imgBatteryChargeState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());

        BatteryUpdateReceiver.registerUpdate(this.getApplicationContext());

        this.layoutGeneral = (LinearLayout) findViewById(R.id.layoutGeneral);
        this.txtBatteryPercentage = (TextView) findViewById(R.id.txtBatteryPercentage);
        this.imgBatteryChargeState = (ImageView) findViewById(R.id.imgBatteryChargeState);

        this.layoutGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startBatteryActivityIntent = new Intent(MainActivity.this.getApplicationContext(), BatteryActivity.class);
                MainActivity.this.startActivity(startBatteryActivityIntent);
            }
        });
    }

    private BroadcastReceiver connectedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            MainActivity.this.updateGeneralData();
        }
    };

    private BroadcastReceiver disconnectedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            MainActivity.this.updateGeneralData();
        }
    };

    private Handler updateHandler = new Handler();
    private Runnable updateRun = new Runnable() {
        @Override
        public void run() {
            MainActivity.this.updateGeneralData();
            // repeat the Update each minute
            MainActivity.this.updateHandler.postDelayed(MainActivity.this.updateRun, 60 * 1000);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter confilter = new IntentFilter(Intent.ACTION_POWER_CONNECTED);
        this.registerReceiver(this.connectedReceiver, confilter);
        IntentFilter disfilter = new IntentFilter(Intent.ACTION_POWER_DISCONNECTED);
        this.registerReceiver(this.disconnectedReceiver, disfilter);
        this.updateHandler.post(this.updateRun);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(this.connectedReceiver);
        this.unregisterReceiver(this.disconnectedReceiver);
        this.updateHandler.removeCallbacks(this.updateRun);
    }

    // General section

    private void updateGeneralData() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent lastBattery = MainActivity.this.registerReceiver(null, filter);
        int status = lastBattery.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        final boolean isCharging = (status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL);
        int current = lastBattery.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int maximal = lastBattery.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        final int percentage = (int)(((float) current / (float) maximal) * 100);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MainActivity.this.setGeneralState(isCharging, percentage);
            }
        });
    }

    private boolean GeneralBatteryCharging = false;
    private int GeneralBatteryPercentage = 100;

    private void setGeneralState(boolean ischarging, int percentage) {
        this.GeneralBatteryCharging = ischarging;
        this.GeneralBatteryPercentage = percentage;
        this.updateGeneralVisualization();
    }

    private void updateGeneralVisualization() {
        this.txtBatteryPercentage.setText(Integer.toString(this.GeneralBatteryPercentage) + "%");
        if (this.GeneralBatteryCharging) {
            if (this.GeneralBatteryPercentage >= 0 && this.GeneralBatteryPercentage <= 12) {
                this.imgBatteryChargeState.setImageResource(R.drawable.battery_y_0);
            }
            if (this.GeneralBatteryPercentage >= 13 && this.GeneralBatteryPercentage <= 37) {
                this.imgBatteryChargeState.setImageResource(R.drawable.battery_y_25);
            }
            if (this.GeneralBatteryPercentage >= 38 && this.GeneralBatteryPercentage <= 62) {
                this.imgBatteryChargeState.setImageResource(R.drawable.battery_y_50);
            }
            if (this.GeneralBatteryPercentage >= 63 && this.GeneralBatteryPercentage <= 87) {
                this.imgBatteryChargeState.setImageResource(R.drawable.battery_y_75);
            }
            if (this.GeneralBatteryPercentage >= 88 && this.GeneralBatteryPercentage <= 100) {
                this.imgBatteryChargeState.setImageResource(R.drawable.battery_y_100);
            }
        } else {
            if (this.GeneralBatteryPercentage >= 0 && this.GeneralBatteryPercentage <= 12) {
                this.imgBatteryChargeState.setImageResource(R.drawable.battery_n_0);
            }
            if (this.GeneralBatteryPercentage >= 13 && this.GeneralBatteryPercentage <= 37) {
                this.imgBatteryChargeState.setImageResource(R.drawable.battery_n_25);
            }
            if (this.GeneralBatteryPercentage >= 38 && this.GeneralBatteryPercentage <= 62) {
                this.imgBatteryChargeState.setImageResource(R.drawable.battery_n_50);
            }
            if (this.GeneralBatteryPercentage >= 63 && this.GeneralBatteryPercentage <= 87) {
                this.imgBatteryChargeState.setImageResource(R.drawable.battery_n_75);
            }
            if (this.GeneralBatteryPercentage >= 88 && this.GeneralBatteryPercentage <= 100) {
                this.imgBatteryChargeState.setImageResource(R.drawable.battery_n_100);
            }
        }
    }
}

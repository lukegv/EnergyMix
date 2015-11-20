package de.inces.hackathonrweapp.batteryActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import de.inces.hackathonrweapp.R;
import de.inces.hackathonrweapp.batteryDataRecord.BatteryDB;

public class BatteryActivity extends AppCompatActivity {

    private LineChart chartHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery);

        this.chartHistory = (LineChart) findViewById(R.id.chartHistory);
        this.chartHistory.setTouchEnabled(false);
        this.chartHistory.setDescription("");
        this.chartHistory.getLegend().setEnabled(false);
        this.chartHistory.getXAxis().setDrawLabels(false);

        Handler h = new Handler();
        h.post(new Runnable() {
            @Override
            public void run() {
                updateHistoryData();
            }
        });
    }

    private void updateHistoryData() {
        List<BatteryHistoryPoint> historyList = new ArrayList<BatteryHistoryPoint>();
        SQLiteDatabase db = (new BatteryDB(this.getApplicationContext())).getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + BatteryDB.UPDATE_TABLE + " WHERE " + BatteryDB.DATE_TIME + " > datetime('now', '-4 hour')", null);
        if (c.moveToFirst()) {
            do {
                historyList.add(new BatteryHistoryPoint(c.getString(1), c.getInt(2)));
            } while (c.moveToNext());
        }
        db.close();
        final BatteryHistoryPoint[] history = historyList.toArray(new BatteryHistoryPoint[historyList.size()]);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BatteryActivity.this.updateHistoryVisualization(history);
            }
        });
    }

    private void updateHistoryVisualization(BatteryHistoryPoint[] history) {
        ArrayList<Entry> entries = new ArrayList<Entry>();
        ArrayList<String> xvals = new ArrayList<String>();
        for (int i = 0; i < history.length; i++) {
            entries.add(new Entry(history[i].getPercentage(), i));
            xvals.add(history[i].getTime());
        }
        LineDataSet set = new LineDataSet(entries, "History");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setDrawCircles(false);
        set.setLineWidth(3f);
        set.setDrawCubic(true);
        set.setColor(Color.BLACK);
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set);
        LineData data = new LineData(xvals, dataSets);
        data.setDrawValues(false);
        this.chartHistory.setData(data);
        this.chartHistory.invalidate();
    }

}

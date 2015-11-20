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
import android.util.Pair;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

import de.inces.hackathonrweapp.batteryDataRecord.BatteryDB;
import de.inces.hackathonrweapp.batteryDataRecord.BatteryUpdateReceiver;

public class MainActivity extends AppCompatActivity {

    // Battery State
    private TextView txtBatteryPercentage;
    private ImageView imgBatteryChargeState;
    // Battery History
    private LineChart chartHistory;
    // Energy Mix
    private RelativeLayout containerEnergyMix;
    // Energy Mix Over Time
    private LineChart chartEnergyMixOverTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());

        BatteryUpdateReceiver.registerUpdate(this.getApplicationContext());

        this.txtBatteryPercentage = (TextView) findViewById(R.id.txtBatteryPercentage);
        this.imgBatteryChargeState = (ImageView) findViewById(R.id.imgBatteryChargeState);

        this.chartHistory = (LineChart) findViewById(R.id.chartHistory);
        this.chartHistory.setTouchEnabled(false);
        this.chartHistory.setDescription("");
        this.chartHistory.getLegend().setEnabled(false);
        this.chartHistory.getXAxis().setDrawLabels(false);

        this.containerEnergyMix = (RelativeLayout) findViewById(R.id.containerEnergyMix);

        this.chartEnergyMixOverTime = (LineChart) findViewById(R.id.chartEnergyMixOverTime);
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
            MainActivity.this.updateHistoryData();
            // repeat the Update each minute
            MainActivity.this.updateHandler.postDelayed(MainActivity.this.updateRun, 60 * 1000);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        this.startEnergyMix();
        this.startEnergyMixOverTime();
        WebRequester webRequester = new WebRequester();
        webRequester.loadData(MainActivity.this);
        IntentFilter confilter = new IntentFilter(Intent.ACTION_POWER_CONNECTED);
        this.registerReceiver(this.connectedReceiver, confilter);
        IntentFilter disfilter = new IntentFilter(Intent.ACTION_POWER_DISCONNECTED);
        this.registerReceiver(this.disconnectedReceiver, disfilter);
        this.updateHandler.post(this.updateRun);
    }

    public String EnergyMixData;

    public void updateEnergyMixData() {
        this.updateEnergyMix();
        this.updateEnergyMixOverTime();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(this.connectedReceiver);
        this.unregisterReceiver(this.disconnectedReceiver);
        this.updateHandler.removeCallbacks(this.updateRun);
    }

    // Battery Status

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

    // Battery History

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
                MainActivity.this.updateHistoryVisualization(history);
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

    // Energy Mix

    /** Colors to be used for the pie slices. */
    private static int[] EnergyMixCOLORS = new int[] { Color.GREEN, Color.BLUE, Color.MAGENTA, Color.CYAN, Color.RED, Color.YELLOW,
            Color.GRAY};

    private CategorySeries series;
    private DefaultRenderer renderer;
    /** The chart view that displays the data. */
    private GraphicalView chartView;
    private SeriesSelection seriesSelection;

    private void startEnergyMix() {
        series = new CategorySeries("series");

        renderer = new DefaultRenderer();
        renderer.setStartAngle(180);
        renderer.setDisplayValues(false);
        renderer.setZoomEnabled(false);
        renderer.setShowLegend(false);
        renderer.setClickEnabled(false);
        float textSize = renderer.getLabelsTextSize();
        textSize = textSize *3;
        renderer.setLabelsTextSize(textSize);

        chartView = ChartFactory.getPieChartView(this, series, renderer);
    }

    public void updateEnergyMix() {
        EnergyDataParser energyDataParser = new EnergyDataParser(this.EnergyMixData);

        EnergyDataHolder dataHolder = energyDataParser.GetLatestDataSet();

        for(int i = 0; i < dataHolder.data.size(); i++) {
            Pair p = dataHolder.data.get(i);
            addNewSeriespoint(p);
        }

        seriesSelection = chartView.getCurrentSeriesAndPoint();

        if (seriesSelection == null) {
            Toast.makeText(MainActivity.this, "No chart element selected", Toast.LENGTH_SHORT)
                    .show();
        } else {
            for (int i = 0; i < series.getItemCount(); i++) {
                renderer.getSeriesRendererAt(i).setHighlighted(i == seriesSelection.getPointIndex());
            }
            chartView.repaint();
            Toast.makeText(
                    MainActivity.this,
                    "Chart data point index " + seriesSelection.getPointIndex() + " selected"
                            + " point value=" + seriesSelection.getValue(), Toast.LENGTH_SHORT).show();
        }

        this.containerEnergyMix.addView(chartView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        chartView.repaint();
    }

    private void addNewSeriespoint(Pair p){
        series.add(p.first.toString(), new Double(p.second.toString()));
        SimpleSeriesRenderer simpleSeriesRenderer = new SimpleSeriesRenderer();
        simpleSeriesRenderer.setColor(EnergyMixCOLORS[(series.getItemCount() - 1) % EnergyMixCOLORS.length]);
        renderer.addSeriesRenderer(simpleSeriesRenderer);
    }

    // Energy Mix Over Time

    private static int[] EnergyMixOverTimeCOLORS = new int[] { Color.GREEN, Color.BLUE, Color.MAGENTA, Color.CYAN, Color.RED, Color.YELLOW,
            Color.GRAY};

    private void startEnergyMixOverTime() {
        this.chartEnergyMixOverTime.setTouchEnabled(false);
        this.chartEnergyMixOverTime.setDescription("");
    }

    private void updateEnergyMixOverTime() {
        EnergyDataParser energyDataParser = new EnergyDataParser(this.EnergyMixData);

        EnergyDataHolder dataHolder = energyDataParser.GetLatestDataSet();
        List<EnergyDataHolder> dataHistory = energyDataParser.GetTodaysDataHistory();

        ArrayList<String> xvals = new ArrayList<String>();
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();

        for (int j = 0; j < dataHolder.data.size(); j++) {

            ArrayList<Entry> entries = new ArrayList<Entry>();

            String name = dataHistory.get(0).data.get(j).first;

            for (int i = 0; i < dataHistory.size(); i++) {
                Pair<String, Double> pa = dataHistory.get(i).data.get(j);
                double d = pa.second;
                float v = (float) d;
                entries.add(new Entry(v, i));
                if(j==0)
                    xvals.add(Integer.toString(i));
            }
            LineDataSet set = new LineDataSet(entries, name);
            set.setAxisDependency(YAxis.AxisDependency.LEFT);
            set.setDrawCircles(false);
            set.setLineWidth(3f);
            set.setColor(EnergyMixOverTimeCOLORS[(j % EnergyMixOverTimeCOLORS.length)]);
            set.setDrawCubic(true);

            dataSets.add(set);
        }

        LineData data = new LineData(xvals, dataSets);
        data.setDrawValues(false);
        this.chartEnergyMixOverTime.setData(data);
        this.chartEnergyMixOverTime.invalidate();
    }

}

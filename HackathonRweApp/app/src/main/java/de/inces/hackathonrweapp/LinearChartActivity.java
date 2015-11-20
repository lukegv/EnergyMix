package de.inces.hackathonrweapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;


public class LinearChartActivity extends AppCompatActivity {

    private static int[] COLORS = new int[] { Color.GREEN, Color.BLUE, Color.MAGENTA, Color.CYAN, Color.RED, Color.YELLOW,
            Color.GRAY};
    private LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear_chart);
    }

    @Override
    protected void onResume() {
        super.onResume();
        chart = (LineChart) findViewById(R.id.chart);
        chart.setTouchEnabled(false);
        chart.setDescription("");
        setUpGraph();
    }


    void setUpGraph() {
        WebRequester webRequester = new WebRequester();
        String csvData = webRequester.loadData(LinearChartActivity.this);

        EnergyDataParser energyDataParser = new EnergyDataParser(csvData);

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
            set.setColor(COLORS[(j % COLORS.length)]);
            set.setDrawCubic(true);

            dataSets.add(set);
        }

        LineData data = new LineData(xvals, dataSets);
        data.setDrawValues(false);
        this.chart.setData(data);
        this.chart.invalidate();
    }


}

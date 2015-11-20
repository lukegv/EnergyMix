package de.inces.hackathonrweapp;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;


public class PieChartActivity extends ActionBarActivity {

    /** Colors to be used for the pie slices. */
    private static int[] COLORS = new int[] { Color.GREEN, Color.BLUE, Color.MAGENTA, Color.CYAN, Color.RED, Color.YELLOW,
            Color.GRAY};

    private CategorySeries series;
    private DefaultRenderer renderer;
    /** The chart view that displays the data. */
    private GraphicalView chartView;
    SeriesSelection seriesSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
    }

    @Override
    protected void onResume() {
        super.onResume();

        LinearLayout layout = (LinearLayout) findViewById(R.id.chart);


        series = new CategorySeries("series");


        renderer = new DefaultRenderer();
        renderer.setStartAngle(180);
        renderer.setDisplayValues(false);
        renderer.setZoomEnabled(false);
        renderer.setShowLegend(false);
        float textSize = renderer.getLabelsTextSize();
        textSize = textSize *3;
        renderer.setLabelsTextSize(textSize);

        chartView = ChartFactory.getPieChartView(this, series, renderer);

        WebRequester webRequester = new WebRequester();
        String csvData = webRequester.loadData(PieChartActivity.this);

        EnergyDataParser energyDataParser = new EnergyDataParser(csvData);

        EnergyDataHolder dataHolder = energyDataParser.GetLatestDataSet();

        for(int i = 0; i < dataHolder.data.size(); i++) {
            Pair p = dataHolder.data.get(i);
            addNewSeriespoint(p);
        }

        seriesSelection = chartView.getCurrentSeriesAndPoint();

        if (seriesSelection == null) {
            Toast.makeText(PieChartActivity.this, "No chart element selected", Toast.LENGTH_SHORT)
                    .show();
        } else {
            for (int i = 0; i < series.getItemCount(); i++) {
                renderer.getSeriesRendererAt(i).setHighlighted(i == seriesSelection.getPointIndex());
            }
            chartView.repaint();
            Toast.makeText(
                    PieChartActivity.this,
                    "Chart data point index " + seriesSelection.getPointIndex() + " selected"
                            + " point value=" + seriesSelection.getValue(), Toast.LENGTH_SHORT).show();
        }

        layout.addView(chartView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));
        chartView.repaint();;


    }



    void addNewSeriespoint(Pair p){
        series.add(p.first.toString(), new Double(p.second.toString()));
        SimpleSeriesRenderer simpleSeriesRenderer = new SimpleSeriesRenderer();
        simpleSeriesRenderer.setColor(COLORS[(series.getItemCount() - 1) % COLORS.length]);
        renderer.addSeriesRenderer(simpleSeriesRenderer);
    }


}

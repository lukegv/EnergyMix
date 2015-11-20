package de.inces.hackathonrweapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;



import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static String csvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        WebRequester webRequester = new WebRequester();
        webRequester.loadData(MainActivity.this);


    }

    public void updateView() {

        EnergyDataParser energyDataParser = new EnergyDataParser(csvData);

        EnergyDataHolder dataHolder = energyDataParser.GetLatestDataSet();

        for(int i = 0; i < dataHolder.data.size(); i++) {
            Pair p = dataHolder.data.get(i);
            Log.d("main activity", p.first + " " + p.second);
        }
        Log.d("main activity", "time of latest data set: " + dataHolder.timestamp);

        Log.d("main activity", "history data");
        List<EnergyDataHolder> dataHistory = energyDataParser.GetTodaysDataHistory();
        for(int i = 0; i < dataHistory.size(); i++) {
            Pair p = dataHistory.get(i).data.get(0);
            Log.d("main activity", p.first + " " + p.second);
        }
    }
}

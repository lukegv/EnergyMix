package de.inces.hackathonrweapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        EnergyDataProvider energyDataProvider = new EnergyDataProvider();

        EnergyDataHolder dataHolder = energyDataProvider.GetLatestDataSet();

        for(int i = 0; i < dataHolder.data.size(); i++) {
            Pair p = dataHolder.data.get(i);
            Log.d("main activity", p.first + " " + p.second);
        }
    }
}

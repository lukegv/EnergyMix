package de.inces.hackathonrweapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;



import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static String csvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.energytypes_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);



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

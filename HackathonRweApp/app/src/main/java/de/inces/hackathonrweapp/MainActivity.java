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

        String sampleCsvData = "\"Area\",\"MTU\",\"Biomass  - Actual Aggregated [MW]\",\"Biomass  - Actual Aggregated [MW]\",\"Fossil Brown coal/Lignite  - Actual Aggregated [MW]\",\"Fossil Brown coal/Lignite  - Actual Aggregated [MW]\",\"Fossil Coal-derived gas  - Actual Aggregated [MW]\",\"Fossil Coal-derived gas  - Actual Aggregated [MW]\",\"Fossil Gas  - Actual Aggregated [MW]\",\"Fossil Gas  - Actual Aggregated [MW]\",\"Fossil Hard coal  - Actual Aggregated [MW]\",\"Fossil Hard coal  - Actual Aggregated [MW]\",\"Fossil Oil  - Actual Aggregated [MW]\",\"Fossil Oil  - Actual Aggregated [MW]\",\"Fossil Oil shale  - Actual Aggregated [MW]\",\"Fossil Oil shale  - Actual Aggregated [MW]\",\"Fossil Peat  - Actual Aggregated [MW]\",\"Fossil Peat  - Actual Aggregated [MW]\",\"Geothermal  - Actual Aggregated [MW]\",\"Geothermal  - Actual Aggregated [MW]\",\"Hydro Pumped Storage  - Actual Aggregated [MW]\",\"Hydro Pumped Storage  - Actual Aggregated [MW]\",\"Hydro Run-of-river and poundage  - Actual Aggregated [MW]\",\"Hydro Run-of-river and poundage  - Actual Aggregated [MW]\",\"Hydro Water Reservoir  - Actual Aggregated [MW]\",\"Hydro Water Reservoir  - Actual Aggregated [MW]\",\"Marine  - Actual Aggregated [MW]\",\"Marine  - Actual Aggregated [MW]\",\"Nuclear  - Actual Aggregated [MW]\",\"Nuclear  - Actual Aggregated [MW]\",\"Other  - Actual Aggregated [MW]\",\"Other  - Actual Aggregated [MW]\",\"Other renewable  - Actual Aggregated [MW]\",\"Other renewable  - Actual Aggregated [MW]\",\"Solar  - Actual Aggregated [MW]\",\"Solar  - Actual Aggregated [MW]\",\"Waste  - Actual Aggregated [MW]\",\"Waste  - Actual Aggregated [MW]\",\"Wind Offshore  - Actual Aggregated [MW]\",\"Wind Offshore  - Actual Aggregated [MW]\",\"Wind Onshore  - Actual Aggregated [MW]\",\"Wind Onshore  - Actual Aggregated [MW]\"\n" +
                "\"BZN|DE-AT-LU\",\"19.11.2015 00:00 - 19.11.2015 00:15 (CET)\",\"4044\",\"0\",\"10496\",\"0\",\"366\",\"0\",\"2864\",\"0\",\"2777\",\"0\",\"194\",\"0\",\"0\",\"0\",\"0\",\"0\",\"7\",\"0\",\"18\",\"1762\",\"2009\",\"0\",\"277\",\"0\",\"0\",\"0\",\"9865\",\"0\",\"3482\",\"0\",\"37\",\"0\",\"0\",\"0\",\"275\",\"0\",\"2481\",\"0\",\"27251\",\"0\"\n" +
                "\"BZN|DE-AT-LU\",\"19.11.2015 00:15 - 19.11.2015 00:30 (CET)\",\"4049\",\"0\",\"10338\",\"0\",\"365\",\"0\",\"2845\",\"0\",\"2570\",\"0\",\"194\",\"0\",\"0\",\"0\",\"0\",\"0\",\"7\",\"0\",\"25\",\"2878\",\"2005\",\"0\",\"265\",\"0\",\"0\",\"0\",\"9845\",\"0\",\"4140\",\"0\",\"37\",\"0\",\"0\",\"0\",\"272\",\"0\",\"2479\",\"0\",\"27503\",\"0\"\n" +
                "\"BZN|DE-AT-LU\",\"19.11.2015 00:30 - 19.11.2015 00:45 (CET)\",\"4047\",\"0\",\"10386\",\"0\",\"391\",\"0\",\"2725\",\"0\",\"2475\",\"0\",\"195\",\"0\",\"0\",\"0\",\"0\",\"0\",\"7\",\"0\",\"22\",\"3630\",\"2002\",\"0\",\"264\",\"0\",\"0\",\"0\",\"9859\",\"0\",\"4074\",\"0\",\"37\",\"0\",\"0\",\"0\",\"277\",\"0\",\"2468\",\"0\",\"27469\",\"0\"\n";


        EnergyDataParser energyDataParser = new EnergyDataParser(sampleCsvData);

        EnergyDataHolder dataHolder = energyDataParser.GetLatestDataSet();

        for(int i = 0; i < dataHolder.data.size(); i++) {
            Pair p = dataHolder.data.get(i);
            Log.d("main activity", p.first + " " + p.second);
        }
    }
}

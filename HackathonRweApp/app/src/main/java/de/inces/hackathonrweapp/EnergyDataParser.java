package de.inces.hackathonrweapp;

import android.util.Log;
import android.util.Pair;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Lennart on 19.11.2015.
 */
public class EnergyDataParser {

    // indices of data in CSV file
    int minDataIndex = 2;
    int maxDataIndex = 40;
    int incrementIndex = 2;

    String csvData;

    public EnergyDataParser(String inputData) {
        csvData = inputData;
    }

    public EnergyDataHolder GetLatestDataSet() {

        CSVReader reader = new CSVReader(new StringReader(csvData));
        String [] nextLine;
        String [] lastLine;

        EnergyDataHolder dataHolder;

        try {
            // skip first line that contains headers
            nextLine = reader.readNext();
            lastLine = nextLine;


            while ((nextLine = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                //Log.d("csv parser", nextLine[0] + nextLine[1] + "etc...");

                // check if all relevant entries are numerical (e.g. not NA)
                for(int i = minDataIndex; i <= maxDataIndex; i+=incrementIndex) {
                    // if the entry contains not a number, we found our 'latest' data set
                    if(!isNumeric(nextLine[i])) {
                        // parse lastLine
                        dataHolder = parseLine(lastLine);

                        return dataHolder;
                    }
                    // else, continue...
                }
                // all entries are numeric, save current line and check the next one...
                lastLine = nextLine;
            }

            // if all lines were read and no NA is found, parse the last line
            dataHolder = parseLine(lastLine);
            return dataHolder;
        }
        catch(IOException e)
        {
            Log.d("csv parser", "io exception during CSV parsing");
        }

        // return empty data
        return new EnergyDataHolder();
    }

    public List<EnergyDataHolder> GetTodaysDataHistory() {

        CSVReader reader = new CSVReader(new StringReader(csvData));
        String [] nextLine;
        String [] lastLine;

        List<EnergyDataHolder> dataHolderList = new ArrayList<>();

        try {
            // skip first line that contains headers DEPRECATED
            //nextLine = reader.readNext();
//            lastLine = nextLine;


            while ((nextLine = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                //Log.d("csv parser", nextLine[0] + nextLine[1] + "etc...");

                // check if all relevant entries are numerical (e.g. not NA)
                for(int i = minDataIndex; i <= maxDataIndex; i+=incrementIndex) {
                    // if the entry contains not a number, we found our 'latest' data set
                    if(!isNumeric(nextLine[i])) {
                        // end at the first NA line
                        return dataHolderList;
                    }
                    // else, continue...
                }
                // all entries are numeric, parse current line and add to result
                dataHolderList.add(parseLine(nextLine));
            }
            return dataHolderList;
        }
        catch(IOException e)
        {
            Log.d("csv parser", "io exception during CSV parsing");
        }

        // return empty data
        return dataHolderList;
    }

    EnergyDataHolder parseLine(String[] line) {

        // return data
        EnergyDataHolder dataHolder = new EnergyDataHolder();

        // parse timestamp
        // Direct use of Pattern:
        Pattern p = Pattern.compile("^(.*)-(.*) \\((?:.*)\\)$");
        Matcher m = p.matcher(line[1]);
        String dateStart, dateEnd = "01.01.2000 00:00";
        while (m.find()) { // Find each match in turn. there should be only one
            dateStart = m.group(1); // Access a submatch group;
            dateEnd = m.group(2);

//            Log.d("csv parser", "parsed dates: " + dateStart + ", " + dateEnd);
        }
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Date date = new Date();
        try {
            date = format.parse(dateEnd);
        } catch (Exception e) {
            e.printStackTrace();
        }

        dataHolder.timestamp = date;


        // parse numbers
        double biomass = Double.parseDouble(line[2]);
        double coal = Double.parseDouble(line[4]) + Double.parseDouble(line[10]) + Double.parseDouble(line[16]);
        double gas = Double.parseDouble(line[6]) + Double.parseDouble(line[8]);
        double oil = Double.parseDouble(line[12]) + Double.parseDouble(line[14]);
        double geothermal = Double.parseDouble(line[18]);
        double hydro = Double.parseDouble(line[20]) + Double.parseDouble(line[22])
                + Double.parseDouble(line[24]) +  Double.parseDouble(line[26]);
        double nuclear = Double.parseDouble(line[28]);
        double solar = Double.parseDouble(line[34]);
        double waste = Double.parseDouble(line[36]);
        double wind = Double.parseDouble(line[38]) + Double.parseDouble(line[40]);
        double other = Double.parseDouble(line[30]) + Double.parseDouble(line[32]);

        dataHolder.AddSet("Biomass", biomass);
        dataHolder.AddSet("Coal", coal);
        dataHolder.AddSet("Gas", gas);
        dataHolder.AddSet("Oil", oil);
        dataHolder.AddSet("Geothermal", geothermal);
        dataHolder.AddSet("Hydro", hydro);
        dataHolder.AddSet("Nuclear", nuclear);
        dataHolder.AddSet("Solar", solar);
        dataHolder.AddSet("Waste", waste);
        dataHolder.AddSet("Wind", wind);
        dataHolder.AddSet("Other", other);

        return dataHolder;
    }

//    private String mapIndexToResourceName(int i) {
//        switch (i) {
//            case 2:
//                return "Biomass";
//            break;
//            case 4:
//                return "Fossil Brown Coal";
//            break;
//            case 6:
//                return "Fossil Coal-derived Gas";
//            break;
//            case 8:
//                return "Fossil Gas";
//            break;
//            case 10:
//                return "Fossil Hard Coal";
//            break;
//            case 12:
//                return "Fossil Oil";
//            break;
//        }
//    }

    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }
}

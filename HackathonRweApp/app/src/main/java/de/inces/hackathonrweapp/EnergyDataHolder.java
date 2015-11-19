package de.inces.hackathonrweapp;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lennart on 19.11.2015.
 */
public class EnergyDataHolder {

    public List<Pair<String, Double>> data;

    // todo: time stamp;

    public  EnergyDataHolder() {
        data = new ArrayList<>();
    }

    public void AddSet(String name, Double value) {
        Pair<String, Double> p = new Pair<>(name, value);
        data.add(p);
    }
}

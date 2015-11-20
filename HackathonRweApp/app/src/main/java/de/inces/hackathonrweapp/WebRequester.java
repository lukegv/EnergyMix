package de.inces.hackathonrweapp;

/**
 * Created by Lennart on 20.11.2015.
 */

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;

public class WebRequester {

    public  WebRequester() {
        // constructor
    }

    public void loadData(final Context context) {

        // async http request
        // Instantiate the RequestQueue.
//        RequestQueue queue = Volley.newRequestQueue(this);
//        HttpsTrustManager.allowAllSSL();

        String url ="http://rwe-hackathon.lima-city.de/query_energy_database.php?auth=3a1b30712e4d86ba&key=3061c95fc954aa1f";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("volley", "Response is: " + response.substring(0, 100) + "...");
                        Log.d("volley", "Response length is: "+ response.length());
                        MainActivity.csvData = response;

                        MainActivity mainActivity = (MainActivity)context;
                        mainActivity.updateView();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("volley", "That didn't work!");
                    }
                });

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);


    }
}

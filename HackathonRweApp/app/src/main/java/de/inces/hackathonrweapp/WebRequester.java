package de.inces.hackathonrweapp;

/**
 * Created by Lennart on 20.11.2015.
 */

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class WebRequester {

    public  WebRequester() {
        // constructor
    }

    public void loadData(Context activity) {

        // async http request
        // Instantiate the RequestQueue.
//        RequestQueue queue = Volley.newRequestQueue(this);
        HttpsTrustManager.allowAllSSL();

        String url ="https://transparency.entsoe.eu/generation/r2/actualGenerationPerProductionType/show";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("volley", "Response is: " + response.substring(0, 500));
                        Log.d("volley", "Response length is: "+ response.length());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("volley", "That didn't work!");
                    }
                });

        // Request a string response from the provided URL.
        StringRequest stringRequestPost = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("volley", "Response is: " + response.substring(0, 500));
                        Log.d("volley", "Response length is: "+ response.length());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("volley", "That didn't work!");
                    }
                });

        // Add a request (in this example, called stringRequest) to your RequestQueue.
        MySingleton.getInstance(activity).addToRequestQueue(stringRequest);



    }
}

package com.shareyourself.cab;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shareyourself.R;
import com.shareyourself.cab.RideAvailability.RideAvailablityResponse;
import com.shareyourself.share_volley.VolleyNetwork;
import com.shareyourself.share_volley.VolleyRequest;

public class CabStarters extends Activity {

    private String TAG = "CabStarters";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cab_starters);

        VolleyRequest<RideAvailablityResponse> rideAvailablityRequest = new VolleyRequest<RideAvailablityResponse>(Request.Method.GET, "http://192.168.0.109:8080/TestWeb/RideAvailability.json", RideAvailablityResponse.class, null, null, null, new Response.Listener<RideAvailablityResponse>() {
            @Override
            public void onResponse(RideAvailablityResponse response) {
                Log.i(TAG, "Got volley response");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage());
            }
        });

        VolleyNetwork.getInstance(getApplicationContext()).addToRequestQueue(rideAvailablityRequest);


        VolleyRequest<RideEstimateResponse> volleyRequest = new VolleyRequest<RideEstimateResponse>(Request.Method.GET, "http://192.168.0.109:8080/TestWeb/RideEstimate.json", RideEstimateResponse.class, null, null, null, new Response.Listener<RideEstimateResponse>() {
            @Override
            public void onResponse(RideEstimateResponse response) {
                Log.i(TAG, "Got volley response");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage());
            }
        });

        VolleyNetwork.getInstance(getApplicationContext()).addToRequestQueue(volleyRequest);
    }

}

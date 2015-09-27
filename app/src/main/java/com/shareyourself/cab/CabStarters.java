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

import java.util.HashMap;

public class CabStarters extends Activity {

    private String TAG = "CabStarters";
    private HashMap<String, String> headers;

    private String RIDE_AVAILABLITY = "http://sandbox-­t.olacabs.com/v1/products?pickup_lat=12.9491716&pickup_lng=77.6447288&category=sedan";
    private String RIDE_ESTIMATION = "http://sandbox-­t.olacabs.com/v1/products?pickup_lat=12.9491716&pickup_lng=77.6447288&drop_lat=12.9522443&drop_lng=77.6365754&category=sedan";
    private String BOOK_A_CAB = "http://sandbox-­t.olacabs.com/v1/bookings/create?pickup_lat=12.9491716&pickup_lng=77.6447288&pickup_mode=NOW&category=sedan";
    private String TRACK_RIDE = "http://sandbox-­t.olacabs.com/v1/bookings/track_ride";
    private String TRACK_STATUS = "http://ts1-­phoenix-­proxy-­api-­1848854956.us-­east­-1.elb.amazonaws.com/v1/sandbox/client_located?crn=";
    private String START_TRIP = "http://sandbox-­t.olacabs.com/v1/sandbox/start_trip?crn=";
    private String END_TRIP = "http://sandbox-­t.olacabs.com/v1/sandbox/end_trip?crn=";
    private String RELEASE_CAB = "http://sandbox-­t.olacabs.com/v1/sandbox/available_for_booking?imei=";
    private String CANCEL_CAB = "http://sandbox­-t.olacabs.com/v1/bookings/cancel?crn=";

    private BookCabResponse bookCabResponse;
    private TrackStatusResponse trackStatusResponse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cab_starters);

        headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer 22e88efaa38b46a5957e022980624a52");
        headers.put("X-APP-Token", "a6c0bb5be4d74d54b01020f42075f45f");

        // RIDE_AVAILABLITY

        VolleyRequest<RideAvailablityResponse> rideAvailablityRequest = new VolleyRequest<RideAvailablityResponse>(Request.Method.GET, RIDE_AVAILABLITY, RideAvailablityResponse.class, headers, null, null, new Response.Listener<RideAvailablityResponse>() {
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

        // RIDE_ESTIMATION

        VolleyRequest<RideEstimateResponse> rideEstimationRequest = new VolleyRequest<RideEstimateResponse>(Request.Method.GET, RIDE_ESTIMATION, RideEstimateResponse.class, headers, null, null, new Response.Listener<RideEstimateResponse>() {
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

        VolleyNetwork.getInstance(getApplicationContext()).addToRequestQueue(rideEstimationRequest);

        // BOOK_A_CAB

        VolleyRequest<BookCabResponse> bookCabRequest = new VolleyRequest<BookCabResponse>(Request.Method.GET, BOOK_A_CAB, BookCabResponse.class, headers, null, null, new Response.Listener<BookCabResponse>() {
            @Override
            public void onResponse(BookCabResponse response) {
                Log.i(TAG, "Got volley response");
                bookCabResponse = response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage());
            }
        });

        VolleyNetwork.getInstance(getApplicationContext()).addToRequestQueue(bookCabRequest);

        //**********
        trackRide();
        //**********


        // TRACK_STATUS

        VolleyRequest<TrackStatusResponse> trackStatusRequest = new VolleyRequest<TrackStatusResponse>(Request.Method.GET, TRACK_STATUS+bookCabResponse.getCrn(), TrackStatusResponse.class, headers, null, null, new Response.Listener<TrackStatusResponse>() {
            @Override
            public void onResponse(TrackStatusResponse response) {
                Log.i(TAG, "Got volley response");
                trackStatusResponse = response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage());
            }
        });

        VolleyNetwork.getInstance(getApplicationContext()).addToRequestQueue(trackStatusRequest);

        // START_TRIP

        VolleyRequest<TrackStatusResponse> startTripRequest = new VolleyRequest<TrackStatusResponse>(Request.Method.GET, START_TRIP+bookCabResponse.getCrn(), TrackStatusResponse.class, headers, null, null, new Response.Listener<TrackStatusResponse>() {
            @Override
            public void onResponse(TrackStatusResponse response) {
                Log.i(TAG, "Got volley response");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage());
            }
        });

        VolleyNetwork.getInstance(getApplicationContext()).addToRequestQueue(startTripRequest);

        //**********
        trackRide();
        //**********

        // END_TRIP

        VolleyRequest<TrackStatusResponse> endTripRequest = new VolleyRequest<TrackStatusResponse>(Request.Method.GET, END_TRIP+bookCabResponse.getCrn(), TrackStatusResponse.class, headers, null, null, new Response.Listener<TrackStatusResponse>() {
            @Override
            public void onResponse(TrackStatusResponse response) {
                Log.i(TAG, "Got volley response");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage());
            }
        });

        VolleyNetwork.getInstance(getApplicationContext()).addToRequestQueue(endTripRequest);

        //**********
        trackRide();
        //**********

        // RELEASE_CAB

        VolleyRequest<TrackStatusResponse> releaseCabRequest = new VolleyRequest<TrackStatusResponse>(Request.Method.GET, RELEASE_CAB+trackStatusResponse.getImei(), TrackStatusResponse.class, headers, null, null, new Response.Listener<TrackStatusResponse>() {
            @Override
            public void onResponse(TrackStatusResponse response) {
                Log.i(TAG, "Got volley response");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage());
            }
        });

        VolleyNetwork.getInstance(getApplicationContext()).addToRequestQueue(releaseCabRequest);

        // CANCEL_CAB

        VolleyRequest<CancelCabResponse> cancelCabRequest = new VolleyRequest<CancelCabResponse>(Request.Method.GET, CANCEL_CAB+bookCabResponse.getCrn(), CancelCabResponse.class, headers, null, null, new Response.Listener<CancelCabResponse>() {
            @Override
            public void onResponse(CancelCabResponse response) {
                Log.i(TAG, "Got volley response");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage());
            }
        });

        VolleyNetwork.getInstance(getApplicationContext()).addToRequestQueue(cancelCabRequest);

    }

    private void trackRide(){

        // TRACK_RIDE

        VolleyRequest<TrackRideResponse> trackRideRequest = new VolleyRequest<TrackRideResponse>(Request.Method.GET, TRACK_RIDE, TrackRideResponse.class, headers, null, null, new Response.Listener<TrackRideResponse>() {
            @Override
            public void onResponse(TrackRideResponse response) {
                Log.i(TAG, "Got volley response");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage());
            }
        });

        VolleyNetwork.getInstance(getApplicationContext()).addToRequestQueue(trackRideRequest);
    }

}

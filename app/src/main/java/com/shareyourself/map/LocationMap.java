package com.shareyourself.map;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shareyourself.R;
import com.shareyourself.base.GcmSender;
import com.shareyourself.base.MessageView;
import com.shareyourself.eventbus.EventBusSingleton;
import com.shareyourself.eventbus.LocationEvent;
import com.squareup.otto.Subscribe;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;

public class LocationMap extends FragmentActivity implements OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener, LocationListener, ResultCallback<LocationSettingsResult>
{
    // <PhoneNumber> , <LocationEvent>
    private HashMap<String, LocationEvent> locationStore = new HashMap<String, LocationEvent>();
    // <TopicName> , <Phone Number List >
    private HashMap<String, HashSet<String>> topicStore = new HashMap<String, HashSet<String>>();

    private static final String TAG = "LocationMap";
    private String topicName = "global";
	private GoogleApiClient mGoogleApiClient;
	private Location mCurrentLocation;
	private GoogleMap mMap;
    private LocationRequest mLocationRequest;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_map);
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
		buildGoogleApiClient();
        googleApiClientConnect();
        createLocationRequest();

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(this);

	}	

	@Override
	public void onResume() {
		super.onResume();
        googleApiClientConnect();
		startLocationUpdates();
        EventBusSingleton.instance().register(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		stopLocationUpdates();
        googleApiClientDisconnect();
        EventBusSingleton.instance().unregister(this);
	}

	protected void stopLocationUpdates() {
		if (mGoogleApiClient.isConnected()) 
			LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
	}

	@Override
	public void onMapReady(GoogleMap map) {
		mMap = map;
	}

	protected synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(this)
		.addConnectionCallbacks(this)
		.addOnConnectionFailedListener(this)
		.addApi(LocationServices.API)
		.build();
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {}

	@Override
	public void onConnected(Bundle arg0) {
		mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
		if (mCurrentLocation != null) {
			//updateUI();
		}
		startLocationUpdates();
	}

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

	protected void startLocationUpdates() {
		if (mGoogleApiClient.isConnected()) {
			LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
		}
	}

	@Override
	public void onConnectionSuspended(int arg0) {}

	@Override
	public void onLocationChanged(Location location) {
		mCurrentLocation = location;
        try {
            SharedPreferences mSharedPreferences = getSharedPreferences("ShareYourself", Context.MODE_PRIVATE);
            JSONObject message = new JSONObject();
            message.put("phno", mSharedPreferences.getString("PHONE", "NONE"));
            message.put("topicName", "global");
            message.put("lat", mCurrentLocation.getLatitude());
            message.put("long", mCurrentLocation.getLongitude());
            JSONObject payload = new JSONObject();
            payload.put("location", message);
            (new GcmSender()).sendToGCM(this, payload);
        }
        catch (Exception ex){
            Log.e(TAG, ex.getMessage());
        }
		//updateUI();
	}

    @Override
    public void onResult(LocationSettingsResult result) {
        final Status status = result.getStatus();
        // final LocationSettingsStates = result.getLocationSettingsStates();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                // All location settings are satisfied. The client can initialize location
                // requests here.

                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                // Location settings are not satisfied. But could be fixed by showing the user
                // a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    status.startResolutionForResult(
                            LocationMap.this,
                            100);//REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    // Ignore the error.
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are not satisfied. However, we have no way to fix the
                // settings so we won't show the dialog.

                break;
        }
    }

    @Subscribe
    public void onLocationReceived(LocationEvent event) {
        if(event.topicName.equals(topicName)) {
            locationStore.put(event.phno, event);
            if(topicStore.containsKey(topicName)) {
                topicStore.get(topicName).add(event.phno);
            }
            else{
                HashSet<String> phoneSet = new HashSet<String>();
                phoneSet.add(event.phno);
                topicStore.put(topicName, phoneSet);
            }
            updateUI();
        }
    }

	private void updateUI() {
		if(mMap != null){
			LatLng location = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
			mMap.setMyLocationEnabled(true);
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13));
            mMap.clear();
            for(String phno : topicStore.get(topicName)){
                LocationEvent locationEvent = locationStore.get(phno);
                LatLng loc = new LatLng(locationEvent.latitude, locationEvent.longitude);
                mMap.addMarker(new MarkerOptions().position(loc).title(locationEvent.phno));
            }
		}
	}

    void googleApiClientConnect()
    {
        if (!mGoogleApiClient.isConnected())
        {
            mGoogleApiClient.connect();
        }
    }
	
	void googleApiClientDisconnect()
	{
		if (mGoogleApiClient.isConnected())
		{
	        mGoogleApiClient.disconnect();
	    }
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_message_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent messageIntent = new Intent(this, MessageView.class);
            startActivity(messageIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

package com.shareyourself.map;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.shareyourself.R;
import com.shareyourself.base.GcmSender;
import com.shareyourself.base.MessageView;
import com.shareyourself.eventbus.EventBusSingleton;
import com.shareyourself.eventbus.LocationEvent;
import com.shareyourself.location.sort.HttpConnection;
import com.shareyourself.location.sort.PathJSONParser;
import com.shareyourself.location.sort.Place;
import com.shareyourself.location.sort.SortPlaces;
import com.squareup.otto.Subscribe;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

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
    private Marker mPositionMarker;

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
        System.out.println("LocationMap.onLocationChanged");
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

        System.out.println("mPositionMarker = " + mPositionMarker);
        System.out.println("mMap = " + mMap);
       if (mPositionMarker == null) {
            mPositionMarker = mMap.addMarker(new MarkerOptions()
                    .flat(true)
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.mipmap.position_indicator))
                    .anchor(0.5f, 0.5f)
                    .position(new LatLng(location.getLatitude(), location.getLongitude())));
        }

        animateMarker(mPositionMarker, location); // Helper method for smooth animation
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 10));

//        sortPoints(new LatLng(location.getLatitude(), location.getLongitude()));
	}

    public void animateMarker(final Marker marker, final Location location) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final LatLng startLatLng = marker.getPosition();
        final double startRotation = marker.getRotation();
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);

                double lng = t * location.getLongitude() + (1 - t)
                        * startLatLng.longitude;
                double lat = t * location.getLatitude() + (1 - t)
                        * startLatLng.latitude;

                float rotation = (float) (t * location.getBearing() + (1 - t)
                        * startRotation);

                marker.setPosition(new LatLng(lat, lng));
                marker.setRotation(rotation);

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });
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
//			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13));

//            CameraPosition cameraPosition = new CameraPosition.Builder().target(
//                    location).zoom(12).build();
//
//            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//
//            mMap.clear();
//            for(String phno : topicStore.get(topicName)){
//                LocationEvent locationEvent = locationStore.get(phno);
//                LatLng loc = new LatLng(locationEvent.latitude, locationEvent.longitude);
//                mMap.addMarker(new MarkerOptions().position(loc).title(locationEvent.phno));
//            }
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

    private void sortPoints(LatLng currLoc){

        /**
         * Location sort code
         **/

        double lat = currLoc.latitude;
        double lng = currLoc.longitude;
        LatLng latLng = new LatLng(lat, lng);

        //set up list
        ArrayList<Place> places = new ArrayList<Place>();

        places.add(new Place("Kormangala", new LatLng(12.927923, 77.627108)));
        places.add(new Place("BTM", new LatLng(12.908240, 77.607409)));
        places.add(new Place("JayaNagar", new LatLng(12.925007, 77.593803)));
        places.add(new Place("Hebbal", new LatLng(13.035770, 77.597022)));
        places.add(new Place("KALYAN NAGAR", new LatLng(13.028005, 77.639971)));
        places.add(new Place("DOMLUR", new LatLng(12.960986, 77.638732)));
        places.add(new Place("BAnsankari", new LatLng(12.925453, 77.546757)));
        places.add(new Place("ITPL", new LatLng(12.986598, 77.743531)));
//        places.add(new Place("Gwalior", new LatLng(26.218287, 78.182831)));
//        places.add(new Place("Morena", new LatLng(26.493356, 77.990995)));

        /*for (Place p: places){
            Log.i("Places before sorting", "Place: " + p.name);
        }*/

        //sort the list, give the Comparator the current location
        Collections.sort(places, new SortPlaces(latLng));
        mMap.clear();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Place p: places){
//            Log.i("Places after sorting", "Place: " + p.name);
            Marker marker = mMap.addMarker(new MarkerOptions().position(p.latlng).title(p.name));
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();

        String url = getMapsApiDirectionsUrl(places);
        ReadTask downloadTask = new ReadTask();
        downloadTask.execute(url);

        int padding = 100; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        mMap.animateCamera(cu);
    }

    private String getMapsApiDirectionsUrl(ArrayList<Place> places) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("origin="+places.get(0).latlng.latitude + "," + places.get(0).latlng.longitude
            +"&destination="+places.get(places.size() -1).latlng.latitude + "," + places.get(places.size() -1).latlng.longitude+"&");
        stringBuilder.append("waypoints=optimize:true");

        for (Place place : places){
            stringBuilder.append("|" + place.latlng.latitude + "," + place.latlng.longitude);
        }

        String sensor = "sensor=false";
        String params = stringBuilder.toString() + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"+ output + "?" + params;
        return url;
    }

    public String makeURL(double sourcelat, double sourcelog, double destlat, double destlog) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString.append(Double.toString(sourcelog));
        urlString.append("&destination=");// to
        urlString.append(Double.toString(destlat));
        urlString.append(",");
        urlString.append(Double.toString(destlog));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        return urlString.toString();
    }

    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
//                System.out.println("jsonData[0] = " + jsonData[0]);
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(2);
                polyLineOptions.color(Color.BLUE);
            }

            mMap.addPolyline(polyLineOptions);
        }
    }
}

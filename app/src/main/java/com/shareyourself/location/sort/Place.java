package com.shareyourself.location.sort;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by akhare on 9/26/15.
 */
public class Place {
    public String name;
    public LatLng latlng;

    public Place(String name, LatLng latlng) {
        this.name = name;
        this.latlng = latlng;
    }
}

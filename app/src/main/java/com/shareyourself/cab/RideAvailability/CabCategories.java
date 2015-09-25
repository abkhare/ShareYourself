package com.shareyourself.cab.RideAvailability;

import java.util.ArrayList;

/**
 * Created by akhare on 9/26/15.
 */
public class CabCategories {
    private String id;
    private String display_name;
    private String currency;
    private String distance_unit;
    private String time_unit;
    private double eta;
    private String distance;
    private String image;
    private ArrayList<FareDetail> fare_breakup;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDistance_unit() {
        return distance_unit;
    }

    public void setDistance_unit(String distance_unit) {
        this.distance_unit = distance_unit;
    }

    public String getTime_unit() {
        return time_unit;
    }

    public void setTime_unit(String time_unit) {
        this.time_unit = time_unit;
    }

    public double getEta() {
        return eta;
    }

    public void setEta(double eta) {
        this.eta = eta;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<FareDetail> getFare_breakup() {
        return fare_breakup;
    }

    public void setFare_breakup(ArrayList<FareDetail> fare_breakup) {
        this.fare_breakup = fare_breakup;
    }
}

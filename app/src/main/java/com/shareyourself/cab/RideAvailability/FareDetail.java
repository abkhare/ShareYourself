package com.shareyourself.cab.RideAvailability;

import java.util.ArrayList;

/**
 * Created by akhare on 9/26/15.
 */
public class FareDetail {
    private String type;
    private String minimum_distance;
    private String minimum_time;
    private String base_fare;
    private String cost_per_distance;
    private String waiting_cost_per_minute;
    private String ride_cost_per_minute;
    private ArrayList<SurchargeDetails> surcharge;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMinimum_distance() {
        return minimum_distance;
    }

    public void setMinimum_distance(String minimum_distance) {
        this.minimum_distance = minimum_distance;
    }

    public String getMinimum_time() {
        return minimum_time;
    }

    public void setMinimum_time(String minimum_time) {
        this.minimum_time = minimum_time;
    }

    public String getCost_per_distance() {
        return cost_per_distance;
    }

    public void setCost_per_distance(String cost_per_distance) {
        this.cost_per_distance = cost_per_distance;
    }

    public String getBase_fare() {
        return base_fare;
    }

    public void setBase_fare(String base_fare) {
        this.base_fare = base_fare;
    }

    public String getWaiting_cost_per_minute() {
        return waiting_cost_per_minute;
    }

    public void setWaiting_cost_per_minute(String waiting_cost_per_minute) {
        this.waiting_cost_per_minute = waiting_cost_per_minute;
    }

    public String getRide_cost_per_minute() {
        return ride_cost_per_minute;
    }

    public void setRide_cost_per_minute(String ride_cost_per_minute) {
        this.ride_cost_per_minute = ride_cost_per_minute;
    }
}

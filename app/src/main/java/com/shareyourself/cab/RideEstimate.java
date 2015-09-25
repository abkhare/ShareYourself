package com.shareyourself.cab;

/**
 * Created by akhare on 9/26/15.
 */
public class RideEstimate {
    private String category;
    private double distance;
    private double travel_time_in_minutes;
    private double amount_min;
    private double amount_max;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getTravel_time_in_minutes() {
        return travel_time_in_minutes;
    }

    public void setTravel_time_in_minutes(double travel_time_in_minutes) {
        this.travel_time_in_minutes = travel_time_in_minutes;
    }

    public double getAmount_min() {
        return amount_min;
    }

    public void setAmount_min(double amount_min) {
        this.amount_min = amount_min;
    }

    public double getAmount_max() {
        return amount_max;
    }

    public void setAmount_max(double amount_max) {
        this.amount_max = amount_max;
    }
}

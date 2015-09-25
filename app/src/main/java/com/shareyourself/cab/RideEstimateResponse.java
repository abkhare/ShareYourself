package com.shareyourself.cab;

import com.shareyourself.cab.RideAvailability.CabCategories;

import java.util.ArrayList;

/**
 * Created by akhare on 9/26/15.
 */
public class RideEstimateResponse {
    private ArrayList<CabCategories> categories;
    private ArrayList<RideEstimate> ride_estimate;

    public ArrayList<CabCategories> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<CabCategories> categories) {
        this.categories = categories;
    }

    public ArrayList<RideEstimate> getRide_estimate() {
        return ride_estimate;
    }

    public void setRide_estimate(ArrayList<RideEstimate> ride_estimate) {
        this.ride_estimate = ride_estimate;
    }
}

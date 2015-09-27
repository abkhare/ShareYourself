package com.shareyourself.cab;

/**
 * Created by akhare on 9/27/15.
 */
public class TrackRideResponse {

    private String status;
    private String request_type;
    private String booking_status;
    private String crn;

    private double driver_lat;
    private double driver_lng;

    private TripInfo trip_info;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }

    public String getCrn() {
        return crn;
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }

    public String getBooking_status() {
        return booking_status;
    }

    public void setBooking_status(String booking_status) {
        this.booking_status = booking_status;
    }

    public double getDriver_lat() {
        return driver_lat;
    }

    public void setDriver_lat(double driver_lat) {
        this.driver_lat = driver_lat;
    }

    public double getDriver_lng() {
        return driver_lng;
    }

    public void setDriver_lng(double driver_lng) {
        this.driver_lng = driver_lng;
    }

    public TripInfo getTrip_info() {
        return trip_info;
    }

    public void setTrip_info(TripInfo trip_info) {
        this.trip_info = trip_info;
    }
}

package com.shareyourself.eventbus;

/**
 * Created by akhare on 9/15/15.
 */
public class LocationEvent {
    public String topicName;
    public String phno;
    public double latitude;
    public double longitude;

    public LocationEvent(String topicName, String phno, double latitude, double longitude){
        this.topicName = topicName;
        this.phno = phno;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}

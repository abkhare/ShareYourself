package com.shareyourself.cab.RideAvailability;

/**
 * Created by akhare on 9/26/15.
 */
public class SurchargeDetails {
    private String name;
    private String type;
    private String description;
    private double value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}

package com.example.nelson.prototype_001.entity;

/**
 * Created by Nelson on 20/9/2017.
 */

public class Location {
    String name;
    String description;
    Coordinate locationCoordinate;
    String address;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Coordinate getLocationCoordinate() {
        return locationCoordinate;
    }

    public void setLocationCoordinate(Coordinate locationCoordinate) {
        this.locationCoordinate = locationCoordinate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

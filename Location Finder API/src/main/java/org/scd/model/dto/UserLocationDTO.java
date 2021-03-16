package org.scd.model.dto;

import org.bson.types.ObjectId;

import java.util.Date;

public class UserLocationDTO {

    private Long id;
    private double lat;
    private double lng;
    private String date;

    public UserLocationDTO(double lat, double lng, String date) {
        this.lat = lat;
        this.lng = lng;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

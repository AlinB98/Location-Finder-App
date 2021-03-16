package org.scd.model;


import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Document(collection = "Locations")
public class Location{

    @Id
    private String id;

    @Field("userID")
    private String userId;

    @Field("lat")
    private double lat;

    @Field("lng")
    private double lng;

    @Field("date")
    private String date;

    public Location(){}

    public Location(double lat, double lng, String date) {
        this.lat = lat;
        this.lng = lng;
        this.date = date;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public Date string2date(String dateString){

        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(dateString,new ParsePosition(0));

        return date;
    }

    public String date2string(Date date){

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String dateString = format.format(date);

        return dateString;
    }

}

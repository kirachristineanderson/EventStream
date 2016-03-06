package com.courtyard.afterafx.eventstream;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

/**
 * Created by Brian on 11/14/2015.
 */

@ParseClassName("Event")
public class Event extends ParseObject{

    public Event(){
        //default constructor
    }

    public int getEventId(){
        return getInt("eventId");
    }

    public void setEventId(int id){
        put("eventId", id);
    }

    public boolean isPrivate(){
        return getBoolean("isPrivate");
    }

    public void setIsPrivate(boolean b){
        put("isPrivate", b);
    }

    public void setEventCreator(ParseUser user){
        put("eventCreator", user);
    }

    public ParseUser getEventCreater(){
        return getParseUser("eventCreator");
    }

    public void setName(String n){
        put("eventName", n);
    }

    public String getName(){
        return getString("eventName");
    }

    public void setDescription(String s){
        put("eventDescription", s);
    }

    public String getDescription(){
        return getString("eventDescription");
    }

    public void setRadius(int r){
        put("eventRadius", r);
    }

    public int getRadius(){
        return getInt("eventRadius");
    }

    public ParseGeoPoint getLocation(){
        return getParseGeoPoint("location");
    }

    public void setLocation(ParseGeoPoint location){
        put("location", location);
    }

    public Date getStartDate(){
        return getDate("startDate");
    }

    public void setStartDate(Date d){
        put("startDate", d);
    }

    public Date getEndDate(){
        return getDate("endDate");
    }

    public void setEndDate(Date d){
        put("endDate", d);
    }

}

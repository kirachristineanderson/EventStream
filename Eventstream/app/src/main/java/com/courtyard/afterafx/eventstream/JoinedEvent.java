package com.courtyard.afterafx.eventstream;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by afterafx on 11/24/15.
 */

@ParseClassName("JoinedEvent")
public class JoinedEvent extends ParseObject {

    public JoinedEvent(){
        //default constructor
    }

    public void setObjectId(String objId){
        put("objectId", objId);
    }

    public String getObjectId(){
        return getString("objectId");
    }

    public void setUser(String user){
        put("UserId", user);
    }

    public String getUser(){
        return getString("UserId");
    }

    public void setEventId(int id){
        put("eventId", id);
    }

    public int getId(){
        return getInt("eventId");
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

}


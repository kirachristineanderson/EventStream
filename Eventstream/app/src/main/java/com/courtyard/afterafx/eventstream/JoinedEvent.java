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

    public void setUser(ParseUser user){
        put("User", user);
    }

    public ParseUser getUser(){
        return ParseUser.getCurrentUser();
    }

    public void setEventId(int id){
        put("eventId", id);
    }

    public int getEventId(){
        return getInt("eventId");
    }

    public String getEventName(){
        return getString("eventName");
    }

    public void setName(String name){
        put("eventName", name);
    }

    public String getEventDescription(){
        return getString("eventDescription");
    }

    public void setDescription(String description){
        put("eventDescription", description);
    }

}


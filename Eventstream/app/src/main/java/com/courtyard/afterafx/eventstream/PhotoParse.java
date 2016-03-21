package com.courtyard.afterafx.eventstream;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.File;

/**
 * Created by afterafx on 10/19/15.
 */

//An extension of ParseObject that makes it more convenient
//to  access information about a given photo.

@ParseClassName("PhotoParse")
public class PhotoParse extends ParseObject {

    public PhotoParse(){
        //default constructor
    }

    public void setEventId(int id){
        put("eventID", id);
    }

    public int getEventId(){
        return getInt("eventID");
    }

    public ParseFile getImage(){
        return getParseFile("image");
    }

    public void setImage(ParseFile file) {
        put("image", file);
    }

    public ParseUser getUser(){
        return getParseUser("user");
    }

    public void setUser(ParseUser user){
        put("user", user);
    }

    public ParseFile getThumbnail(){
        return getParseFile("thumbnail");
    }

    public void setThumbnail(ParseFile file){
        put("thumbnail", file);
    }

}

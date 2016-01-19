package com.courtyard.afterafx.eventstream;

import com.parse.ParseFile;
import com.parse.ParseUser;

/**
 * Created by afterafx on 11/19/15.
 */
public class User extends ParseUser {

    public String getObjectId(){
        return getString("objectId");
    }

    public String getUsername(){
        return getString("username");
    }

    public void setProfileImage(ParseFile file){
        put("profilepic", file);
    }

    public ParseFile getProfileImage(){
        return getParseFile("profilepic");
    }
}

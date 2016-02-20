package com.courtyard.afterafx.eventstream;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.content.Context;

import com.google.maps.model.LatLng;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Date;
import java.util.List;
import java.util.Locale;


public class CreateEvent extends AppCompatActivity {

    private EditText eventNameEditText;
    private EditText eventDescriptEditText;
    private EditText eventLocationEditText;
    private EditText eventStartEditText;
    private EditText eventEndEditText;
    private CheckBox eventIsPrivate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        eventNameEditText = (EditText) findViewById(R.id.eventBox);
        eventDescriptEditText = (EditText) findViewById(R.id.descriptBox);
        eventLocationEditText = (EditText) findViewById(R.id.locateBox);
        eventStartEditText = (EditText) findViewById(R.id.startBox);
        eventEndEditText = (EditText) findViewById(R.id.endBox);
        eventIsPrivate = (CheckBox) findViewById(R.id.privateBox);

        Button submitButton = (Button) findViewById(R.id.eventSubmit);
        submitButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        submit();
                        onBackPressed();
                        finish();
                    }
                }
        );
    }

    public void submit() {

        String eventName = eventNameEditText.getText().toString().trim();
        String eventDescription = eventDescriptEditText.getText().toString().trim();
        String eventLocation = eventLocationEditText.getText().toString().trim();
        String startDate = eventStartEditText.getText().toString().trim();
        String endDate = eventEndEditText.getText().toString().trim();
        boolean isPrivate = eventIsPrivate.isChecked();

        Event parseEvent = new Event();//Creates an event object and connects to the event table in database
        int eventId = (int) (Math.random() * 100000000);//generate random eventId for now

        //This uses the functions in the event class
        parseEvent.setName(eventName);
        parseEvent.setDescription(eventDescription);
        parseEvent.setEventId(eventId);
        parseEvent.setEventCreator(ParseUser.getCurrentUser());
        parseEvent.setIsPrivate(isPrivate);

        parseEvent.setLocation(getLocationFromAddress(this, eventLocation));
        //parseEvent.setStartDate(startDate);
        //parseEvent.setEndDate(endDate);

        parseEvent.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

            }
        });
    }

    public ParseGeoPoint getLocationFromAddress(Context context, String strAddress) {


        Geocoder coder = new Geocoder(context, Locale.getDefault());
        List<Address> address;
        ParseGeoPoint p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            //address = coder.getFromLocationName("10721 Fullbright Ave, Chatsworth, CA", 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new ParseGeoPoint(location.getLatitude(), location.getLongitude());

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    }


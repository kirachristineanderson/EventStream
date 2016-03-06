package com.courtyard.afterafx.eventstream;

import android.app.Activity;
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

    private double lat;
    private double lng;
    private ParseGeoPoint point;

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

        eventLocationEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toCreateEventMap = new Intent(getApplicationContext(), CreateEventMap.class );
                startActivityForResult(toCreateEventMap, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                lat = data.getDoubleExtra("lat", 0.0);
                lng = data.getDoubleExtra("lng",0.0);
                String tempLocation = data.getStringExtra("location");
                eventLocationEditText.setText(tempLocation);
            }
        }
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

//        lat = getIntent().getDoubleExtra("lat", 0.0);
//        lng = getIntent().getDoubleExtra("lng", 0.0);
        ParseGeoPoint point = new ParseGeoPoint(lat, lng);
        parseEvent.setLocation(point);
       // object.put("location", point);
        try {
            parseEvent.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        finish();


        parseEvent.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

            }
        });
    }






    }


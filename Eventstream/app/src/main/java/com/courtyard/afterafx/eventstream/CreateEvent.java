package com.courtyard.afterafx.eventstream;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Date;


public class CreateEvent extends AppCompatActivity {

    private EditText eventNameEditText;
    private EditText eventDescriptEditText;
    private EditText eventLocationEditText;
    private EditText eventStartEditText;
    private EditText eventEndEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        eventNameEditText = (EditText) findViewById(R.id.eventBox);
        eventDescriptEditText = (EditText) findViewById(R.id.descriptBox);
        eventLocationEditText = (EditText) findViewById(R.id.locateBox);
        eventStartEditText = (EditText) findViewById(R.id.startBox);
        eventEndEditText = (EditText) findViewById(R.id.endBox);

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

        Event parseEvent = new Event();//Creates an event object and connects to the event table in database
        int eventId = (int) (Math.random() * 100000000);//generate random eventId for now

        //This uses the functions in the event class
        parseEvent.setName(eventName);
        parseEvent.setDescription(eventDescription);
        parseEvent.setEventId(eventId);
        parseEvent.setEventCreator(ParseUser.getCurrentUser());
        //parseEvent.setLocation(eventLocation);
        //parseEvent.setStartDate(startDate);
        //parseEvent.setEndDate(endDate);

        parseEvent.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

            }
        });
    }
}

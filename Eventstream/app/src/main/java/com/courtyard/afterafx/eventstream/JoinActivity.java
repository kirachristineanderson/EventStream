package com.courtyard.afterafx.eventstream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class JoinActivity extends Activity {

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        Intent intent = getIntent();
        final String eventName = intent.getStringExtra("Name");
        final String eventDescription = intent.getStringExtra("Description");
        final int eventID = intent.getIntExtra("EventID", 0);

        TextView name = (TextView) findViewById(R.id.joinEventName);
        name.setText(eventName);

        TextView description = (TextView) findViewById(R.id.joinEventDescription);
        description.setText(eventDescription);

        Button joinButton = (Button) findViewById(R.id.joinEventButton);
        joinButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        new AlertDialog.Builder(context)
                                .setTitle("Join Event!")
                                .setMessage("Are you sure you want to join this event?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with Join
                                        JoinedEvent joinedEvent = new JoinedEvent();
                                        joinedEvent.setUser(ParseUser.getCurrentUser());
                                        joinedEvent.setEventId(eventID);
                                        joinedEvent.setName(eventName);
                                        joinedEvent.setDescription(eventDescription);

                                        joinedEvent.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {

                                            }
                                        });
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                    }
                }
        );
    }
}

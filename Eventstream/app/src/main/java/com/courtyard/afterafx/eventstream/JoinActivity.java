package com.courtyard.afterafx.eventstream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class JoinActivity extends Activity {

    final String TAG ="JoinedActivity";

    final Context context = this;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        Intent intent = getIntent();
        final String eventName = intent.getStringExtra("Name");
        final String eventDescription = intent.getStringExtra("Description");
        final int eventID = intent.getIntExtra("EventID", 0);
        final boolean isPrivate = intent.getBooleanExtra("Private", false);

        TextView name = (TextView) findViewById(R.id.joinEventName);
        name.setText(eventName);

        TextView description = (TextView) findViewById(R.id.joinEventDescription);
        description.setText(eventDescription);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, 1);

        Button joinButton = (Button) findViewById(R.id.joinEventButton);
        final JoinedEvent joinedEvent = new JoinedEvent();
        joinButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {

                        if(!isPrivate){
                            //joinedEvent = new JoinedEvent();
                            joinedEvent.setUser(ParseUser.getCurrentUser());
                            joinedEvent.setEventId(eventID);
                            joinedEvent.setName(eventName);

                            if(eventDescription != null){
                                joinedEvent.setDescription(eventDescription);
                            }

                            joinedEvent.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {

                                }
                            });

                            new AlertDialog.Builder(context).setMessage("You have successfully joined this event!")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener(){
                                        public void onClick(DialogInterface dialog, int which){
                                            //do nothing
                                        }
                                    })
                                    .show();
                        }
                        else{
                            new AlertDialog.Builder(context).setMessage("Sorry this is a private event!")
                                    .setIcon(android.R.drawable.ic_secure)
                                    .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            //do nothing
                                        }
                                    })
                                    .show();
                        }

                    }
                }
        );

        Button sendPhotoEvent = (Button) findViewById(R.id.sendPhotoEvent);
        sendPhotoEvent.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i(TAG, "eventID " + eventID);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("primaryEventID", eventID);
                        editor.commit();
                    }
                }
        );
    }
}

package com.courtyard.afterafx.eventstream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class EventAlbum extends Activity {

    final String TAG ="JoinedActivity";

    final Context context = this;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";


    //for grid view
    List<PhotoParse> listOfPhotoParseObjects=new ArrayList<>();


    GridView event_album_grid_view;
    ListAdapter imageAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Bitmap gridViewBitmap;
    int eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_album);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        final Intent intent = getIntent();
        final String eventName = intent.getStringExtra("Name");
        final String eventDescription = intent.getStringExtra("Description");
        final int eventID = intent.getIntExtra("EventID", 0);
        final boolean isPrivate = intent.getBooleanExtra("Private", false);

        eventId = eventID;

        TextView name = (TextView) findViewById(R.id.joinEventName);
        name.setText(eventName);

        TextView description = (TextView) findViewById(R.id.joinEventDescription);
       description.setText(eventDescription);

        event_album_grid_view = (GridView) findViewById(R.id.event_album_grid_view);

        populateGridView();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        imageGridView();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listOfPhotoParseObjects=new ArrayList<>();
                        populateGridView();

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        imageGridView();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2500);
            }
        });
    



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


        event_album_grid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PhotoParse photoParse = (PhotoParse) parent.getItemAtPosition(position);
                //System.out.println(photoParse);
                byte[] imageData = new byte[0];
                Intent intent1 = new Intent(EventAlbum.this, ImageViewActivity.class);
                try {
                    imageData = photoParse.getImage().getData();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                intent1.putExtra("image", imageData);
                System.out.println(imageData);

                startActivity(intent1);

            }
        });
    }

    public void imageGridView() {
                imageAdapter = new EventAlbumCustomAdapter(EventAlbum.this, listOfPhotoParseObjects);
                event_album_grid_view.setAdapter(imageAdapter);
    }


    public void populateGridView(){

//        Event event = new Event();
//        int tempEventId = event.getEventId(); //get the id of the event you just clicked on


        ParseQuery<ParseObject> query = ParseQuery.getQuery("PhotoParse");
        //query.include("JoinedEvent");
        query.whereEqualTo("eventID", eventId);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> results, ParseException e) {
                if (e == null) {
                    int numberOfResults = results.size();
                    ParseFile profilePhotoFile;
                    int EventId;
                    byte[] data;
                    Bitmap bMap;
                    //Toast.makeText(getApplicationContext(), "Number of results: " + numberOfResults, Toast.LENGTH_SHORT).show();

                    if (numberOfResults > 0) {
                        //Toast.makeText(getApplicationContext(), "ProfilePhoto ParseObject: " + profilePhotoFile, Toast.LENGTH_SHORT).show();

                        for (int i = 0; i < numberOfResults; i++) {
                            //profilePhotoFile = results.get(i).getParseFile("image");
                            //EventId = results.get(i).getInt("eventId");

                            PhotoParse photoParse = (PhotoParse) results.get(i);

                            listOfPhotoParseObjects.add(photoParse);
//                            if (profilePhotoFile != null) {
//                                try {
//                                    //Toast.makeText(getContext(), "Got Inside Try", Toast.LENGTH_SHORT).show();
    //                                data = profilePhotoFile.getData();
////                                    //Log.w("geteventid" + i + ": ", " " + data);
////                                    bMap = BitmapFactory.decodeByteArray(data, 0, data.length);
//                                    //Log.w("bMap #: " + i + ": ", bMap.toString());
//
//                                    // listOfPictures.add(bMap);
//
//                                    //create list of UserprofileActivity objects containing eventid and bitmap
//                                   // EventAlbum eventAlbum = new EventAlbum();
//                                    //eventAlbum.setEventId(EventId);
//                                    //eventAlbum.setGridViewBitmap(bMap);
//                                    //Log.w("geteventid" + i + ": ", " " + userProfileActivity.getEventId());
//                                    //Log.w("eventPhotoStrViews: " + i + ": ", " " + eventPhotoStreamGridViews.getGridViewBitmap());
//
//                                    photoParse.setImage(bMap);
//                                    photoParse.setEventId(EventId);
//                                    arrayOfEventAlbums.add(photoParse);
//                                    //Log.w("arrayofuserprofileac" + i + ": ", " " + arrayOfUserProfileActivities.get(i));
//
//
//                                    //only add the event id if there is one in the future there should always be one but now there isnt
////                                    if(EventId >0){
////
////                                        listOfEventIds.add(EventId);
////
////                                    }
//
//
//                                    //setListviewBitmap(bMap);
//                                    //single_image.setImageBitmap(bMap);
//                                } catch (ParseException e2) {
//                                    // TODO Auto-generated catch block
//                                    e2.printStackTrace();
//                                }
//                            }
//                        }
//                        //Toast.makeText(getApplicationContext(), "How many files in listOfPictures: " + listOfPictures.size(), Toast.LENGTH_SHORT).show();
//
//                    }
//                } else {
//                    Log.d("score", "Error: " + e.getMessage());
//                }
                        }
                    }
                }
            }
        });


    }//end of populateGridView method


    public Bitmap getGridViewBitmap() {
        return gridViewBitmap;
    }

    public void setGridViewBitmap(Bitmap gridViewBitmap) {
        this.gridViewBitmap = gridViewBitmap;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
}

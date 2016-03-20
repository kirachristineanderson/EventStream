package com.courtyard.afterafx.eventstream;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class EventPhotoStreamGridView extends AppCompatActivity {

    Bitmap gridViewBitmap;
    int eventId;
    List<EventPhotoStreamGridView> arrayOfEventPhotoStreamGridViews = new ArrayList<EventPhotoStreamGridView>();

    GridView photo_stream_grid_view;
    ListAdapter imageAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_photo_stream_grid_view);

        photo_stream_grid_view = (GridView) findViewById(R.id.photo_stream_grid_view);

        imageGridView();
        populateGridView();
    }

    public void imageGridView() {

        imageAdapter = new EventStreamCustomAdapter(EventPhotoStreamGridView.this, arrayOfEventPhotoStreamGridViews);
        photo_stream_grid_view.setAdapter(imageAdapter);

        photo_stream_grid_view.setClickable(true);
        photo_stream_grid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

//                Intent intent = new Intent(EventPhotoStreamGridView.this, EventStreamCustomAdapter.class);
//                startActivity(intent);
//                Object listViewObject = profile_list_view.getItemAtPosition(position);
            }
        });
    }


    public void populateGridView(){
            ParseQuery<ParseObject> query = ParseQuery.getQuery("ProfilePhoto");
            query.whereEqualTo("ImageName", "profilePicture");
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
                                profilePhotoFile = results.get(i).getParseFile("image");
                                EventId = results.get(i).getInt("EventId");
                                if (profilePhotoFile != null) {
                                    try {
                                        //Toast.makeText(getContext(), "Got Inside Try", Toast.LENGTH_SHORT).show();
                                        data = profilePhotoFile.getData();
                                        bMap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        //Log.w("bMap #: " + i + ": ", bMap.toString());

                                        // listOfPictures.add(bMap);

                                        //create list of UserprofileActivity objects containing eventid and bitmap
                                        EventPhotoStreamGridView eventPhotoStreamGridViews = new EventPhotoStreamGridView();
                                        eventPhotoStreamGridViews.setEventId(EventId);
                                        eventPhotoStreamGridViews.setGridViewBitmap(bMap);
                                        //Log.w("geteventid" + i + ": ", " " + userProfileActivity.getEventId());
                                        Log.w("eventPhotoStrViews: " + i + ": ", " " + eventPhotoStreamGridViews.getGridViewBitmap());
                                        arrayOfEventPhotoStreamGridViews.add(eventPhotoStreamGridViews);
                                        //Log.w("arrayofuserprofileac" + i + ": ", " " + arrayOfUserProfileActivities.get(i));


                                        //only add the event id if there is one in the future there should always be one but now there isnt
//                                    if(EventId >0){
//
//                                        listOfEventIds.add(EventId);
//
//                                    }


                                        //setListviewBitmap(bMap);
                                        //single_image.setImageBitmap(bMap);
                                    } catch (ParseException e2) {
                                        // TODO Auto-generated catch block
                                        e2.printStackTrace();
                                    }
                                }
                            }
                            //Toast.makeText(getApplicationContext(), "How many files in listOfPictures: " + listOfPictures.size(), Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Log.d("score", "Error: " + e.getMessage());
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

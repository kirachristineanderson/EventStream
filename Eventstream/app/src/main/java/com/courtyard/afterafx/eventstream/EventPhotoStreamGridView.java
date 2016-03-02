package com.courtyard.afterafx.eventstream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class EventPhotoStreamGridView extends AppCompatActivity {

    Bitmap singleBitmap;
    List<Bitmap> picturesFromEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_photo_stream_grid_view);

    populateGridView();
        //imageListView(); //implements the list view
    }

//    public void imageListView() { //the list view to view all the pictures
//        String[] captions = {"Thats a really cool picture!",
//                "Wow look at that!", "Thats awesome!","Thats a really cool picture!",
//                "Wow look at that!", "Thats awesome!","Thats a really cool picture!",
//                "Wow look at that!", "Thats awesome!"};
//
//        ListAdapter imageAdapter = new EventStreamCustomAdapter(EventPhotoStreamGridView.this,captions);
//
//        ListView photo_stream_list_view = (ListView) findViewById(R.id.photo_stream_list_view);
//        photo_stream_list_view.setAdapter(imageAdapter);
//    }

    public void populateGridView(){

        String[] captions = {"Thats a really cool picture!",
                "Wow look at that!", "Thats awesome!","Thats a really cool picture!",
                "Wow look at that!", "Thats awesome!","Thats a really cool picture!",
                "Wow look at that!", "Thats awesome!","Thats a really cool picture!",
                "Wow look at that!", "Thats awesome!","Thats a really cool picture!",
                "Wow look at that!", "Thats awesome!"};

        GridView photo_stream_list_view = (GridView) findViewById(R.id.photo_stream_list_view);
        EventStreamCustomAdapter adapter = new EventStreamCustomAdapter(EventPhotoStreamGridView.this, captions);
        photo_stream_list_view.setAdapter(adapter);

    }

}

package com.courtyard.afterafx.eventstream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 2/28/2016.
 */
public class EventStreamCustomAdapter extends ArrayAdapter<EventPhotoStreamGridView> {

    ImageView left_image;
    ImageView middle_image;
    ImageView right_image;
    View customView;
    Bitmap bitmap;

    EventPhotoStreamGridView eventPhotoStreamGridView;

    int counter = 0;

    TextView textView;


    public EventStreamCustomAdapter(Context context, List<EventPhotoStreamGridView> eventPhotoStreamGridView) {
        super(context, R.layout.event_photo_stream_custom_row, eventPhotoStreamGridView);
    }

    public View getView (int position, View convertView, ViewGroup parent){
        LayoutInflater imageDisplay = LayoutInflater.from(getContext());
        customView = imageDisplay.inflate(R.layout.event_photo_stream_custom_row, parent, false);


        middle_image = (ImageView) customView.findViewById(R.id.middle_image);

        //String singleCaption = getItem(position);
//        TextView caption = (TextView) customView.findViewById(R.id.caption);
//        caption.setText(singleCaption);


        //Bitmap left = getItem(position);
        eventPhotoStreamGridView = new EventPhotoStreamGridView(); //Create new instance of object
        eventPhotoStreamGridView = getItem(position); //get the Object from the list at the (position)


        middle_image.setImageBitmap(eventPhotoStreamGridView.getGridViewBitmap()); //set the image on the prof page from object
        Log.w("custoptergettingBMP: ", " " + eventPhotoStreamGridView.getGridViewBitmap());

        return customView;
    }


}

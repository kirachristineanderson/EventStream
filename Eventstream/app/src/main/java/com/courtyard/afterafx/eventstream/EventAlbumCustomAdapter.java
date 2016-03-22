package com.courtyard.afterafx.eventstream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
//import android.support.annotation.DrawableRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
public class EventAlbumCustomAdapter extends ArrayAdapter<EventAlbum> {

    ImageView left_image;
    ImageView single_image;
    ImageView right_image;
    View customView;
    Bitmap bitmap;

    EventAlbum eventAlbum;

    int counter = 0;

    TextView textView;


    public EventAlbumCustomAdapter(Context context, List<EventAlbum> eventAlbum) {
        super(context, R.layout.event_album_grid_view_custom_row, eventAlbum);
        Toast.makeText(getContext(), "Number of results: ", Toast.LENGTH_SHORT).show();
    }

    public View getView (int position, View convertView, ViewGroup parent){
        LayoutInflater imageDisplay = LayoutInflater.from(getContext());
        customView = imageDisplay.inflate(R.layout.event_album_grid_view_custom_row, parent, false);


        single_image = (ImageView) customView.findViewById(R.id.single_image);

        //String singleCaption = getItem(position);
//        TextView caption = (TextView) customView.findViewById(R.id.caption);
//        caption.setText(singleCaption);


        //Bitmap left = getItem(position);
        eventAlbum = new EventAlbum(); //Create new instance of object
        eventAlbum = getItem(position); //get the Object from the list at the (position)


        single_image.setImageBitmap(eventAlbum.getGridViewBitmap()); //set the image on the prof page from object
        //Log.w("custoptergettingBMP: ", " " + eventPhotoStreamGridView.getGridViewBitmap());

        return customView;
    }


}

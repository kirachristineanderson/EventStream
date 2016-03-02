package com.courtyard.afterafx.eventstream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;
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
public class EventStreamCustomAdapter extends ArrayAdapter<String> {

    ImageView left_image;
    ImageView middle_image;
    ImageView right_image;
    View customView;
    Bitmap bitmap;

    int counter = 0;

    TextView textView;


    public EventStreamCustomAdapter(Context context, String[] captions) {
        super(context, R.layout.event_photo_stream_custom_row, captions);
    }

    public View getView (int position, View convertView, ViewGroup parent){
        LayoutInflater imageDisplay = LayoutInflater.from(getContext());
        customView = imageDisplay.inflate(R.layout.event_photo_stream_custom_row, parent, false);


        middle_image = (ImageView) customView.findViewById(R.id.middle_image);

        //String singleCaption = getItem(position);
//        TextView caption = (TextView) customView.findViewById(R.id.caption);
//        caption.setText(singleCaption);


        //Bitmap left = getItem(position);

        int[] pictures = {R.drawable.bigbear1, R.drawable.bigbear2, R.drawable.bigbear3,
                R.drawable.bigbear4, R.drawable.bigbear5,
                R.drawable.bigbear6, R.drawable.bigbear7, R.drawable.bigbear8, R.drawable.bigbear9,
                R.drawable.bigbear10, R.drawable.bigbear11, R.drawable.bigbear12,
                R.drawable.bigbear13, R.drawable.bigbear14, R.drawable.bigbear15};



        middle_image.setImageResource(pictures[counter]);

        counter++;
        if(counter>=15){
            counter=0;
        }
        EventImageDisplay();
        return customView;
    }

    public void EventImageDisplay() {
        //*****attempting to get the image file back and set it as the profile picture
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ProfilePhoto");
        query.whereEqualTo("ImageName", "profilePicture");
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object != null) {

                    ParseFile file = (ParseFile) object.get("image");
                    file.getDataInBackground(new GetDataCallback() {


                        public void done(byte[] data, ParseException e) {
                            if (e == null) {

                                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                //middle_image.setImageBitmap(bitmap);

                            } else {
                                // something went wrong
                            }
                        }
                    });

                } else {
                    //Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
}

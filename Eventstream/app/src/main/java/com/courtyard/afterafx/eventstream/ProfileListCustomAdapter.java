package com.courtyard.afterafx.eventstream;

/**
 * Created by Chris on 2/17/2016.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.*;
import android.widget.*;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProfileListCustomAdapter extends ArrayAdapter<Bitmap> {

    ImageView single_image;
    View customView;
    Bitmap bitmap;
    int counter = 0;
    int[] pictures1;


    public ProfileListCustomAdapter(Context context, List<Bitmap> arrayListOfPictures){
        super(context, R.layout.profile_list_custom_row, arrayListOfPictures);
        //SimpleEventImageDisplay();
      //  EventImageDisplay();
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent){
        LayoutInflater imageDisplay = LayoutInflater.from(getContext());
        customView = imageDisplay.inflate(R.layout.profile_list_custom_row, parent, false);
        single_image = (ImageView) customView.findViewById(R.id.left_image);

        bitmap = getItem(position);
        TextView image_caption = (TextView) customView.findViewById(R.id.image_caption);

        String[] captions;
        //captions = singleUserProfileActivity.getCaptions();
        Toast.makeText(getContext(), "is there a bitmap?: " + bitmap.toString(), Toast.LENGTH_SHORT).show();

        //image_caption.setText(captions[position]);

        single_image.setImageBitmap(bitmap);
//            single_image.setImageBitmap(listOfPictures.get(1));
//            counter++;
//        if(counter>=3){
//            counter=0;
//        }

//old hardcoded stuff

//        single_image.setImageResource(pictures1[counter]);
//
//        counter++;
//        if(counter>=3){
//            counter=0;
//        }


        return customView;
    }



}

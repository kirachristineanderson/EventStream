package com.courtyard.afterafx.eventstream;

/**
 * Created by Chris on 2/17/2016.
 */
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.*;
import android.widget.*;
import android.graphics.Bitmap;

import com.parse.ParseException;

import java.util.List;

//Extends the arrayAdapter of the object type your passing over
public class ProfileListCustomAdapter extends ArrayAdapter<Event> {

    ImageView single_image;
    View customView;
    Bitmap bitmap;
    Event event;


    //takes in the list of UserProfileActivity Objects that are passed over
    public ProfileListCustomAdapter(Context context, List<Event> listOfEventObjects){
        super(context, R.layout.profile_list_custom_row, listOfEventObjects);
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent){
        LayoutInflater imageDisplay = LayoutInflater.from(getContext());
        customView = imageDisplay.inflate(R.layout.profile_list_custom_row, parent, false);


        event = new Event(); //Create new instance of object
        event = getItem(position); //get the Object from the list at the (position)
        single_image = (ImageView) customView.findViewById(R.id.left_image);
        if(event.getEventAvatar() != null) {
            try {
                byte[] data = event.getEventAvatar().getData();
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                single_image.setImageBitmap(bitmap); //set the image on the prof page from object
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else{
            single_image.setImageResource(R.drawable.ic_launcher);
        }





        TextView event_name = (TextView) customView.findViewById(R.id.event_name);
        String eventName = event.getName();
        event_name.setText(eventName); //I guess setText has to pass a string? I couldn't send only the int

        TextView event_description = (TextView) customView.findViewById(R.id.event_description);
        String eventDescription = event.getDescription();
        event_description.setText(eventDescription);

        return customView; //return info back to userProfileActivity to populate the listview
        //*******no longer using any of this********

        //captions = singleUserProfileActivity.getCaptions();
//        Toast.makeText(getContext(), "is there a bitmap?: " + bitmap.toString(), Toast.LENGTH_SHORT).show();

        //image_caption.setText(captions[position]);

        //single_image.setImageBitmap(bitmap);
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

    }
}

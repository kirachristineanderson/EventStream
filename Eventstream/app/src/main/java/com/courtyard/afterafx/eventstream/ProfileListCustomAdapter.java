package com.courtyard.afterafx.eventstream;

/**
 * Created by Chris on 2/17/2016.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.*;
import android.widget.*;
import java.util.List;

//Extends the arrayAdapter of the object type your passing over
public class ProfileListCustomAdapter extends ArrayAdapter<UserProfileActivity> {

    ImageView single_image;
    View customView;

    UserProfileActivity userProfileActivity;


    //takes in the list of UserProfileActivity Objects that are passed over
    public ProfileListCustomAdapter(Context context, List<UserProfileActivity> userProfileActivity){
        super(context, R.layout.profile_list_custom_row, userProfileActivity);
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent){
        LayoutInflater imageDisplay = LayoutInflater.from(getContext());
        customView = imageDisplay.inflate(R.layout.profile_list_custom_row, parent, false);


        userProfileActivity = new UserProfileActivity(); //Create new instance of object
        userProfileActivity = getItem(position); //get the Object from the list at the (position)


        single_image = (ImageView) customView.findViewById(R.id.left_image);
        single_image.setImageBitmap(userProfileActivity.getListviewBitmap()); //set the image on the prof page from object


        TextView image_caption = (TextView) customView.findViewById(R.id.image_caption);
        int eventIdInt = userProfileActivity.getEventId();
        image_caption.setText(""+eventIdInt); //I guess setText has to pass a string? I couldn't send only the int

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

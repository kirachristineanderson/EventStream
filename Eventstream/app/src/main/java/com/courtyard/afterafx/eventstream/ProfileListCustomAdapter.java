package com.courtyard.afterafx.eventstream;

/**
 * Created by Chris on 2/17/2016.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.*;
import android.widget.*;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ProfileListCustomAdapter extends ArrayAdapter<String> {

    ImageView single_image;
    View customView;
    Bitmap bitmap;

    public ProfileListCustomAdapter(Context context, String[] captions){
        super(context, R.layout.profile_list_custom_row, captions);


    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent){
        LayoutInflater imageDisplay = LayoutInflater.from(getContext());
        customView = imageDisplay.inflate(R.layout.profile_list_custom_row, parent, false);
        single_image = (ImageView) customView.findViewById(R.id.single_image);
        String singleCaption = getItem(position);
        TextView image_caption = (TextView) customView.findViewById(R.id.image_caption);


        image_caption.setText(singleCaption);
        single_image.setImageResource(R.drawable.usergraphic);
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
//                    String imgUrl = file.getUrl();
//                    final Uri imageUri =Uri.parse(imgUrl);
                    file.getDataInBackground(new GetDataCallback() {


                        public void done(byte[] data, ParseException e) {
                            if (e == null) {

                                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                //use this bitmap as you want
                                //Picasso.with(Profile.this).load(imageUri.toString()).into(profileImage);
                                single_image.setImageBitmap(bitmap);

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

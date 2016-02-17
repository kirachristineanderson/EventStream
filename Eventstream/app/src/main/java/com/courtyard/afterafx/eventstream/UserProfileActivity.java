package com.courtyard.afterafx.eventstream;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UserProfileActivity extends AppCompatActivity {//did extend ListActivity

    ParseQueryAdapter<ParseObject> profileAdapter;
    CustomAdapter customAdapter;

    //new variables for new list view
    private ImageView profilePicture;
    private Bitmap bitmap;

    //put images in parse
    private String imageObjectId;
    private Uri uri;

    //get images from parse for listview
    private ImageView single_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profilePicture = (ImageView) findViewById(R.id.profilePicture);


        setContentView(R.layout.user_profile);

        loadProfilePicture(); //puts profile picture in parse
        ProfileImageDisplay(); //assigns picture to profilePicture image view
        //EventImageDisplay(); //assigns pictures to event images
        imageListView(); //implements and sets the list view

        TextView accountName = (TextView) findViewById(R.id.username);
        accountName.setText(ParseUser.getCurrentUser().getUsername());

        //Initialize main ParseQueryAdapter
        //profileAdapter = new ParseQueryAdapter<ParseObject>(this, JoinedEvent.class);
        //**profileAdapter.setTextKey("eventName");


        //Initialize the subclass of ParseQueryAdapter
        //customAdapter = new CustomAdapter(this);
        //LevelAdapter testAdapter = new LevelAdapter(this);

        //Initialize ListView and set initial view to mainAdapter uses the old customadapter class
        //setListAdapter(customAdapter);
        //setListAdapter(testAdapter);


        //new way of initializing listview and query adapter


//  ******      getListView().setClickable(true);
//        getListView().setOnItemClickListener(
//                new AdapterView.OnItemClickListener() {
//                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                        // your code is here on item click
//                        Object object = getListView().getItemAtPosition(position);
//                        JoinedEvent event = (JoinedEvent) object;
//                        String eventName = event.getEventName();
//                        String eventDescription = event.getEventDescription();
//                        int eventId = event.getEventId();
//                        Intent intent = new Intent(UserProfileActivity.this, JoinActivity.class);
//                        intent.putExtra("Name", eventName);
//                        intent.putExtra("Description", eventDescription);
//                        intent.putExtra("EventID", eventId);
//                        startActivity(intent);
//                    }
//                });


        /*
        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent galleyIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleyIntent, RESULT_OK);
                    }
                }
        );
        */
    }

    public void imageListView() {
        String[] captions = {"Thats a really cool picture!",
                "Wow look at that!", "Thats awesome!"};

        ListAdapter imageAdapter = new ProfileListCustomAdapter(this, captions);

        ListView profile_list_view = (ListView) findViewById(R.id.profile_list_view);
        profile_list_view.setAdapter(imageAdapter);
    }

    public void ProfileImageDisplay() {
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
                                profilePicture.setImageBitmap(bitmap);
                                //single_image.setImageBitmap(bitmap);

                            } else {
                                // something went wrong
                            }
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    public void loadProfilePicture() {
        profilePicture = (ImageView) findViewById(R.id.profilePicture);
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
    }

    //put the picture in parse
    public void putImageInParse() {
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            // Log.d(TAG, String.valueOf(bitmap));
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Compress image to lower quality scale 1 - 100
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] image = stream.toByteArray();
            // Create the ParseFile
            ParseFile pictureFile = new ParseFile("profilePictureFile", image); //name assigned to image file in parseDB, byte[] image
            // Upload the image into Parse Cloud
            pictureFile.saveInBackground();

            // Create a New Class called "ImageUpload" in Parse
            final ParseObject imgupload = new ParseObject("ProfilePhoto"); //name of class you want to upload to in parseDB

            // Create a column named "ImageName" and set the string
            imgupload.put("ImageName", "profilePicture"); //new column ImageName, arbitrary image name

            // Create a column named "ImageFile" and insert the image
            imgupload.put("image", pictureFile); //column of file, ParseFile pictureFile(assigned above)

            // Create the class and the columns
            imgupload.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        // Success!
                        imageObjectId = imgupload.getObjectId();

                    } else {
                        // Failure!
                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //    private void updateList() {
//        customAdapter.loadObjects();
//        setListAdapter(customAdapter);
//
//    }
//
//    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            putImageInParse();
        }
//        if (resultCode == Activity.RESULT_OK) {
//            // If a new post has been added, update
//            // the list of posts
//            updateList();
//        }
//    }
    }


}
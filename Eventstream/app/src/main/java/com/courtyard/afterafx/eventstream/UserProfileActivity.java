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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
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
import java.util.ArrayList;
import java.util.List;

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

    private ListView profile_list_view; //the list view on profile page (isclickable and leads to eventphotostreamgridview)

    //pictures array to send to ProfileListCustomAdapter
    List<Bitmap> listOfPictures = new ArrayList<>();

    //list of corresponding event ids
    List<Integer> listOfEventIds = new ArrayList<Integer>();

    //USING THIS
    //I dont think i need a list of bitmaps i think individual ones are needed to create more instances of this object
    Bitmap listviewBitmap;
    int eventId;
    List<UserProfileActivity> arrayOfUserProfileActivities = new ArrayList<UserProfileActivity>();

    //made this global so EventImageDisplayListView can access it
    ListAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        profilePicture = (ImageView) findViewById(R.id.profilePicture);
        profile_list_view = (ListView) findViewById(R.id.profile_list_view);

        loadProfilePicture(); //puts profile picture in parse

        //EventImageDisplay(); //assigns pictures to event images
        imageListView(); //implements and sets the list view

        //Populate the listOfPictures array
        EventImageDisplayListView();



        TextView accountName = (TextView) findViewById(R.id.username);
        accountName.setText(ParseUser.getCurrentUser().getUsername());


        ProfileImageDisplay(); //assigns picture to profilePicture image view
    }


    public void imageListView() {

        imageAdapter = new ProfileListCustomAdapter(UserProfileActivity.this, arrayOfUserProfileActivities);
        profile_list_view.setAdapter(imageAdapter);

        profile_list_view.setClickable(true);
        profile_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Intent intent = new Intent(UserProfileActivity.this, EventPhotoStreamGridView.class);
                startActivity(intent);
                Object listViewObject = profile_list_view.getItemAtPosition(position);
            }
        });
    }

    //Get the Event Images from parse and put them in the listOfPictures array to send to ProfileListCustomAdapter
    public void EventImageDisplayListView(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ProfilePhoto");
        query.whereEqualTo("ImageName", "profilePicture");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> results, ParseException e) {
                if (e == null) {
                    int numberOfResults = results.size();
                    for (int i = 0; i < numberOfResults; i++) {
                        Log.w("results array: ", results.get(i).toString());
                    }
                    ParseFile profilePhotoFile = null;
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
                                    UserProfileActivity userProfileActivity = new UserProfileActivity();
                                    userProfileActivity.setEventId(EventId);
                                    userProfileActivity.setListviewBitmap(bMap);
                                    //Log.w("geteventid" + i + ": ", " " + userProfileActivity.getEventId());
                                    //Log.w("getlistviewbitmap: " + i + ": ", " " + userProfileActivity.getListviewBitmap());
                                    arrayOfUserProfileActivities.add(userProfileActivity);
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


    }//end of SimpleEventImageDisplay method



    public void ProfileImageDisplay() {
        //*****attempting to get the image file back and set it as the profile picture
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ProfilePhoto");
        query.whereEqualTo("ImageName", "ShaunProfPic");
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
            imgupload.put("ImageName", "ShaunProfPic"); //new column ImageName, arbitrary image name

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
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ProfileImageDisplay();
        }
//        if (resultCode == Activity.RESULT_OK) {
//            // If a new post has been added, update
//            // the list of posts
//            updateList();
//        }
//    }
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public Bitmap getListviewBitmap() {
        return listviewBitmap;
    }

    public void setListviewBitmap(Bitmap listviewBitmap) {
        this.listviewBitmap = listviewBitmap;
    }



}
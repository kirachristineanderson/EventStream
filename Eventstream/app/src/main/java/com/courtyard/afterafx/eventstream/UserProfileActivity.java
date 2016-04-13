package com.courtyard.afterafx.eventstream;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import com.parse.Parse;
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

//public class UserProfileActivity extends AppCompatActivity {//did extend ListActivity
public class UserProfileActivity extends ListActivity {

    ParseQueryAdapter<ParseObject> profileAdapter;
    CustomAdapter customAdapter;
    //LevelAdapter levelAdapter;

    //new variables for new list view
    private ImageView profilePicture;
    private Bitmap bitmap;

//    //put images in parse
    private String imageObjectId;
    private Uri uri;

    //get images from parse for listview
    //private ImageView single_image;

    private ListView profile_list_view; //the list view on profile page (isclickable and leads to eventphotostreamgridview)


    //USING THIS
    //I dont think i need a list of bitmaps i think individual ones are needed to create more instances of this object
    Bitmap listviewBitmap;
    int eventId;
    

    //List<Event> listOfEventObjects = new ArrayList<Event>();
    //List<JoinedEvent> joinedEventList = new ArrayList<>();

    //made this global so EventImageDisplayListView can access it
    ListAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        profileAdapter = new ParseQueryAdapter<>(this, JoinedEvent.class);
        profileAdapter.setTextKey("eventName");
        profileAdapter.setTextKey("eventDescription");

        customAdapter = new CustomAdapter(this);

        setListAdapter(customAdapter);


        profilePicture = (ImageView) findViewById(R.id.profilePicture);
//        profile_list_view = (ListView) findViewById(R.id.profile_list_view);

        loadProfilePicture(); //puts profile picture in parse

//        GetJoinedEvents();
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        //Populate the listOfPictures array
//        EventImageDisplayListView();
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        //EventImageDisplay(); //assigns pictures to event images
//        imageListView(); //implements and sets the list view



        TextView accountName = (TextView) findViewById(R.id.username);
        accountName.setText(ParseUser.getCurrentUser().getUsername());


        ProfileImageDisplay(); //assigns picture to profilePicture image view
    }


//    public void imageListView() {
//
//        imageAdapter = new ProfileListCustomAdapter(UserProfileActivity.this, listOfEventObjects);
//        profile_list_view.setAdapter(imageAdapter);
//
//        profile_list_view.setClickable(true);
//        profile_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//
//                Intent intent = new Intent(UserProfileActivity.this, EventPhotoStreamGridView.class);
//                startActivity(intent);
//                Object listViewObject = profile_list_view.getItemAtPosition(position);
//            }
//        });
//    }
//
//    public void GetJoinedEvents(){
//
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("JoinedEvent");
//        String id = ParseUser.getCurrentUser().getObjectId();
//
//        //query.whereEqualTo("UserId", id);
//        //Toast.makeText(getApplicationContext(), "inside joinedevents ", Toast.LENGTH_SHORT).show();
//
//        query.findInBackground(new FindCallback<ParseObject>() {
//            public void done(List<ParseObject> results, ParseException e) {
//                Toast.makeText(getApplicationContext(), "inside done, results size:" + results.size(), Toast.LENGTH_SHORT).show();
//
//                if (e == null) {
//                    //Toast.makeText(getApplicationContext(), "inside if==null ", Toast.LENGTH_SHORT).show();
//
//                    int numberOfResults = results.size();
//
//                    if (numberOfResults > 0) {
//
//                        for (int i = 0; i < results.size(); i++) {
//                            joinedEventList.add((JoinedEvent) results.get(i));
//                        }
//
//                    }
//                } else {
//                    Log.w("score", "Error: " + e.getMessage());
//                }
//            }
//        });
//
//    }//end of SimpleEventImageDisplay method




    //Get the Event Images from parse and put them in the listOfPictures array to send to ProfileListCustomAdapter
//    public void EventImageDisplayListView(){
//
//
//
//        for(int i = 0; i < joinedEventList.size(); i++){
//            final int eventID = joinedEventList.get(i).getEventId();
//            final String eventDescription = joinedEventList.get(i).getEventDescription();
//            final String eventName = joinedEventList.get(i).getEventName();
//
//            ParseQuery<ParseObject> query = ParseQuery.getQuery("PhotoParse");
//            query.whereEqualTo("eventId", eventID);
//            query.getFirstInBackground(new GetCallback<ParseObject>() {
//                public void done(ParseObject result, ParseException e) {
//                    Event event = new Event();
//                    event.setDescription(eventDescription);
//                    event.setName(eventName);
//                    event.setEventId(eventID);
//                    if (e == null) {
//                        PhotoParse photo = (PhotoParse) result;
//                        event.setEventAvatar(photo.getImage());
//                    } else {
//                        Log.d("score", "Error: " + e.getMessage());
//                    }
//                    listOfEventObjects.add(event);
//                }
//            });
//        }
//
//
//
//
//    }//end of SimpleEventImageDisplay method



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

    @Override
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
        if (resultCode == Activity.RESULT_OK) {
            // If a new post has been added, update
            // the list of posts
            //updateList();
        }
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
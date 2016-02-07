package com.courtyard.afterafx.eventstream;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

public class UserProfileActivity extends ListActivity {

    ParseQueryAdapter<ParseObject> profileAdapter;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        TextView accountName = (TextView) findViewById(R.id.username);
        accountName.setText(ParseUser.getCurrentUser().getUsername());

        //Initialize main ParseQueryAdapter
        profileAdapter = new ParseQueryAdapter<ParseObject>(this, JoinedEvent.class);
        profileAdapter.setTextKey("eventName");


        //Initialize the subclass of ParseQueryAdapter
        customAdapter = new CustomAdapter(this);
        //LevelAdapter testAdapter = new LevelAdapter(this);

        //Initialize ListView and set initial view to mainAdapter
        setListAdapter(customAdapter);
        //setListAdapter(testAdapter);

        getListView().setClickable(true);
        getListView().setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        // your code is here on item click
                        Object object = getListView().getItemAtPosition(position);
                        JoinedEvent event = (JoinedEvent) object;
                        String eventName = event.getEventName();
                        String eventDescription = event.getEventDescription();
                        int eventId = event.getEventId();
                        Intent intent = new Intent(UserProfileActivity.this, JoinActivity.class);
                        intent.putExtra("Name", eventName);
                        intent.putExtra("Description", eventDescription);
                        intent.putExtra("EventID", eventId);
                        startActivity(intent);
                    }
                });
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

    private void updateList() {
        customAdapter.loadObjects();
        setListAdapter(customAdapter);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            // If a new post has been added, update
            // the list of posts
            updateList();
        }
    }
}
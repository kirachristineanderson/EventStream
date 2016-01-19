package com.courtyard.afterafx.eventstream;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

public class UserProfileActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        TextView accountName = (TextView) findViewById(R.id.username);
        accountName.setText(ParseUser.getCurrentUser().getUsername());

        //Initialize main ParseQueryAdapter
        ParseQueryAdapter<JoinedEvent> profileAdapter = new ParseQueryAdapter<>(this, JoinedEvent.class);
        profileAdapter.setTextKey("eventName");


        //Initialize the subclass of ParseQueryAdapter
        CustomAdapter customAdapter = new CustomAdapter(this);
        LevelAdapter testAdapter = new LevelAdapter(this);

        //Initialize ListView and set initial view to mainAdapter
        //setListAdapter(customAdapter);
        setListAdapter(testAdapter);

        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent galleyIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleyIntent, RESULT_OK);
                    }
                }
        );
    }
}
package com.courtyard.afterafx.eventstream;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

public class UserProfileActivity extends ListActivity {

    private ParseQueryAdapter<ParseObject> secAdapter;
    private LevelAdapter customAdapter;

    //private String TAG = "EventsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        TextView accountName = (TextView) findViewById(R.id.username);
        accountName.setText(ParseUser.getCurrentUser().getUsername());
        //Initialize main ParseQueryAdapter
        secAdapter = new ParseQueryAdapter<ParseObject>(this, JoinedEvent.class);
       // mainAdapter.setTextKey("eventName");
        //mainAdapter.setImageKey("eventAvatar");


        //Initialize the subclass of ParseQueryAdapter
        customAdapter = new LevelAdapter(this);

        //Initialize ListView and set initial view to mainAdapter
        setListAdapter(customAdapter);

        updateList();


        getListView().setClickable(true);
//        getListView().setOnItemClickListener(
//                new AdapterView.OnItemClickListener() {
//                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                        // your code is here on item click
//                        Object object = getListView().getItemAtPosition(position);
//                        Event event = (Event) object;
//                        String eventName = event.getName();
//                        String eventDescription = event.getDescription();
//                        int eventId = event.getEventId();
//                        Intent intent = new Intent(EventsActivity.this, JoinActivity.class);
//                        intent.putExtra("Name", eventName);
//                        intent.putExtra("Description", eventDescription);
//                        intent.putExtra("EventID", eventId);
//                        startActivity(intent);
//                    }
//                });
//
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

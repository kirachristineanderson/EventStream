package com.courtyard.afterafx.eventstream;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;


public class EventsActivity extends ListActivity {

    private ParseQueryAdapter<ParseObject> mainAdapter;
    private LevelAdapter levelAdapter;

    private String TAG = "EventsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        //Initialize main ParseQueryAdapter
        mainAdapter = new ParseQueryAdapter<ParseObject>(this, Event.class);
        mainAdapter.setTextKey("eventName");
        mainAdapter.setImageKey("eventAvatar");


        //Initialize the subclass of ParseQueryAdapter
        levelAdapter = new LevelAdapter(this);

        //Initialize ListView and set initial view to mainAdapter
        setListAdapter(levelAdapter);

        updateList();


        getListView().setClickable(true);
        getListView().setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        // your code is here on item click
                        Object object = getListView().getItemAtPosition(position);
                        Event event = (Event) object;
                        String eventName = event.getName();
                        String eventDescription = event.getDescription();
                        int eventId = event.getEventId();
                        Intent intent = new Intent(EventsActivity.this, JoinActivity.class);
                        intent.putExtra("Name", eventName);
                        intent.putExtra("Description", eventDescription);
                        intent.putExtra("EventID", eventId);
                        startActivity(intent);
                    }
                });

    }


    private void updateList() {
        levelAdapter.loadObjects();
        setListAdapter(levelAdapter);

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


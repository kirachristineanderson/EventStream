package com.courtyard.afterafx.eventstream;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

public class CustomAdapter extends ParseQueryAdapter<JoinedEvent> {


    public CustomAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<JoinedEvent>() {
            public ParseQuery<JoinedEvent> create() {
                ParseQuery query = new ParseQuery("JoinedEvent");
                query.whereEqualTo("eventCreator", ParseUser.getCurrentUser());
                return query;
            }
        });
    }

    @Override
    public View getItemView(JoinedEvent event, View v, ViewGroup parent) {

        if (v == null) {
            v = View.inflate(getContext(), R.layout.activity_events, null);
        }

        super.getItemView(event, v, parent);

//        ParseImageView eventImage = (ParseImageView) v.findViewById(R.id.icon);
//        ParseFile photoFile = event.getParseFile("photo");
//        if (photoFile != null) {
//            eventImage.setParseFile(photoFile);
//            eventImage.loadInBackground(new GetDataCallback() {
//                @Override
//                public void done(byte[] data, ParseException e) {
//                    // nothing to do
//                }
//            });
//        }

        TextView titleTextView = (TextView) v.findViewById(R.id.nameEvent);
        titleTextView.setText(event.getEventName());

        TextView ratingTextView = (TextView) v.findViewById(R.id.descriptionEventUser);
        ratingTextView.setText(event.getEventDescription());
        return v;
    }
}



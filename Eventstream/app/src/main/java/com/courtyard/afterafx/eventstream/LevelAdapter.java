package com.courtyard.afterafx.eventstream;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

public class LevelAdapter extends ParseQueryAdapter<Event> {

    public LevelAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<Event>() {
            public ParseQuery<Event> create() {
                ParseQuery query = new ParseQuery("Event");
                //query.whereEqualTo("eventCreator", ParseUser.getCurrentUser());
                return query;
            }
        });
    }

    @Override
    public View getItemView(Event event, View v, ViewGroup parent) {

        if (v == null) {
            v = View.inflate(getContext(), R.layout.events_list_custom_row, null);
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

        TextView titleTextView = (TextView) v.findViewById(R.id.event_name);
        titleTextView.setText(event.getName());

        TextView ratingTextView = (TextView) v.findViewById(R.id.event_description);
        ratingTextView.setText(event.getDescription());
        return v;
    }
}

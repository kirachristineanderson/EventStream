package com.courtyard.afterafx.eventstream;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class CustomAdapter extends ParseQueryAdapter<ProfileEvent> {


    public CustomAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<ProfileEvent>() {
            public ParseQuery<ProfileEvent> create() {
                ParseQuery query = new ParseQuery("ProfileEvent");
                return query;
            }
        });
    }

    @Override
        public View getItemView(ProfileEvent profileEvent, View v, ViewGroup parent) {

        if (v == null) {
            v = View.inflate(getContext(), R.layout.user_profile, null);
        }

        super.getItemView(profileEvent, v, parent);

        TextView titleTextView = (TextView) v.findViewById(R.id.name);
        titleTextView.setText(profileEvent.getName());

        TextView ratingTextView = (TextView) v.findViewById(R.id.description);
        ratingTextView.setText(profileEvent.getDescription());

        return v;
    }
}

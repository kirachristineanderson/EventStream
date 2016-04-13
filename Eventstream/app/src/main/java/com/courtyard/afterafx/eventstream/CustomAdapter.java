package com.courtyard.afterafx.eventstream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
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
            v = View.inflate(getContext(), R.layout.profile_list_custom_row, null);
        }

        super.getItemView(profileEvent, v, parent);
        Typeface myTipeFace2 = Typeface.createFromAsset(getContext().getAssets(), "openlight.ttf");
        TextView titleTextView = (TextView) v.findViewById(R.id.event_name);
        titleTextView.setText(profileEvent.getName());
        titleTextView.setTypeface(myTipeFace2);


        TextView ratingTextView = (TextView) v.findViewById(R.id.event_description);
        ratingTextView.setText(profileEvent.getDescription());
        ratingTextView.setTypeface(myTipeFace2);

        return v;
    }



}

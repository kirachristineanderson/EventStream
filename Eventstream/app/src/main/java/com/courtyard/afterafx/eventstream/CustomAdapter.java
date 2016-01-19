package com.courtyard.afterafx.eventstream;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class CustomAdapter extends ParseQueryAdapter<JoinedEvent> {


    public CustomAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<JoinedEvent>() {
            public ParseQuery<JoinedEvent> create() {
                ParseQuery query = new ParseQuery("JoinedEvent");
                return query;
            }
        });
    }

    @Override
    public View getItemView(JoinedEvent joinedEvent, View v, ViewGroup parent) {

        if (v == null) {
            v = View.inflate(getContext(), R.layout.user_profile, null);
        }

        super.getItemView(joinedEvent, v, parent);

        TextView eventNameUser = (TextView) v.findViewById(R.id.nameEventUser);
        eventNameUser.setText(joinedEvent.getEventName());

        TextView eventDescriptionUser = (TextView) v.findViewById(R.id.descriptionEventUser);
        eventDescriptionUser.setText(joinedEvent.getEventDescription());

        return v;
    }
}


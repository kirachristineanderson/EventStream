package com.courtyard.afterafx.eventstream;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.courtyard.afterafx.eventstream.CreateEvent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.UiSettings;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.io.IOException;
import java.util.List;

public class CreateEventMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private Address address;
    private EditText locationTextField;
    private String location;
    private LatLng latLng;
    private Button acceptButton;


    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event_map);

        acceptButton = (Button) findViewById(R.id.acceptButton);
        acceptButton.setEnabled(false); //can't accept before results available

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void onSearch(View view) { //when you click the search button on the map
        locationTextField = (EditText) findViewById(R.id.locationTextField);
        location = locationTextField.getText().toString();
        List<Address> addressList = null;  //array on possible address matches

        if (location != null && !location.equals("")) { //if the textfield isn't blank
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1); //get geopoints from location and return 1 possible match
                if(addressList.size()>0)
                {
                    address = addressList.get(0); //get the 1 match in slot 0
                    latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    googleMap.addCircle(new CircleOptions()
                            .center(latLng)
                            .radius(700)
                            .strokeColor(Color.BLUE)
                            .strokeWidth(8)
                            .fillColor(Color.TRANSPARENT));


                    googleMap.addMarker(new MarkerOptions().position(latLng).title("Marker")); //put the red marker at location
                    //googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng)); //move the camera there
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));
                    acceptButton.setEnabled(true);//can now accept result
                }
                else{
                    Toast.makeText(getApplicationContext(), "Couldn't locate specified location",
                            Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                e.printStackTrace(); //didn't find a match - need to update this with an error
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Enter location and press search",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void acceptButton(View view) { //accept and go back to CreateEvent.class
        Intent returnToCreateEvent = new Intent(this, CreateEvent.class);
        double lat = address.getLatitude();
        double lng = address.getLongitude();

        returnToCreateEvent.putExtra("lat", lat);
        returnToCreateEvent.putExtra("lng", lng);
        returnToCreateEvent.putExtra("location", location);
        setResult(Activity.RESULT_OK, returnToCreateEvent);
        finish();
    }


    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;


        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);


        try {
            googleMap.setMyLocationEnabled(true);
        } catch (SecurityException e) {

        }

        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }
}

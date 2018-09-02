package com.example.user.testapplication;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by User on 21-Oct-17.
 */

public class OtherStreetViews extends FragmentActivity
        implements OnMapReadyCallback {
    RadioButton normal,hybird,satellite,terrein;
    GoogleMap map;
    String placename;
    double latitude,longitude;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_streetviews);
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment1);
        normal=(RadioButton)findViewById(R.id.radiolayout1);
        hybird=(RadioButton)findViewById(R.id.radiolayout2);
        satellite=(RadioButton)findViewById(R.id.radiolayout3);
        terrein=(RadioButton)findViewById(R.id.radiolayout4);
        Intent intent = getIntent();
        placename = intent.getExtras().getString("epuzzle");
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                placename=(String) place.getName();
                getLatitudeAndLongitudeFromGoogleMapForAddress((String) place.getName());
//                Toast.makeText(getApplicationContext(),place.getName(),Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatitudeLongiget(placename);
        LatLng sydney = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title(placename));
        LatLng coordinate = new LatLng(latitude, longitude); //Store these lat lng values somewhere. These should be constant.
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                coordinate, 15);
        googleMap.animateCamera(location);
        map=googleMap;

    }
    public  void Normal(View v)
    {
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        hybird.setChecked(false);
        satellite.setChecked(false);
        terrein.setChecked(false);
        normal.setEnabled(false);
        hybird.setEnabled(true);
        satellite.setEnabled(true);
        terrein.setEnabled(true);

    }
    public  void Hybird(View v)
    {
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        normal.setChecked(false);
        satellite.setChecked(false);
        terrein.setChecked(false);
        normal.setEnabled(true);
        hybird.setEnabled(false);
        satellite.setEnabled(true);
        terrein.setEnabled(true);

    }
    public  void Satellite(View v)
    {
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        hybird.setChecked(false);
        normal.setChecked(false);
        terrein.setChecked(false);
        normal.setEnabled(true);
        hybird.setEnabled(true);
        satellite.setEnabled(false);
        terrein.setEnabled(true);

    }
    public  void Terrein(View v)
    {
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        hybird.setChecked(false);
        satellite.setChecked(false);
        normal.setChecked(false);
        normal.setEnabled(true);
        hybird.setEnabled(true);
        satellite.setEnabled(true);
        terrein.setEnabled(false);

    }

    public void LatitudeLongiget(String searchedAddress){

        String addressStr = searchedAddress;
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addresses =
                    geoCoder.getFromLocationName(addressStr, 1);
            if (addresses.size() >  0) {
                latitude = addresses.get(0).getLatitude();
                longitude =
                        addresses.get(0).getLongitude();





            }

        } catch (IOException e) { // TODO Auto-generated catch block
            e.printStackTrace(); }
    }
    public void getLatitudeAndLongitudeFromGoogleMapForAddress(String searchedAddress){

        String addressStr = searchedAddress;
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());

        try {
            List<android.location.Address> addresses =
                    geoCoder.getFromLocationName(addressStr, 1);
            if (addresses.size() >  0) {
                double latitude = addresses.get(0).getLatitude();
                double  longtitude =
                        addresses.get(0).getLongitude();
                LatitudeLongiget(placename);
                LatLng sydney = new LatLng(latitude, longtitude);
                map.addMarker(new MarkerOptions().position(sydney)
                        .title(searchedAddress));

                LatLng coordinate = new LatLng(latitude, longtitude); //Store these lat lng values somewhere. These should be constant.
                CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                        coordinate, 15);
                map.animateCamera(location);


            }

        } catch (IOException e) { // TODO Auto-generated catch block
            e.printStackTrace(); }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, LivestreetViewstart.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}

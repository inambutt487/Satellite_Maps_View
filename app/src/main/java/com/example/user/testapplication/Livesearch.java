package com.example.user.testapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.StreetViewPanoramaView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;
import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by User on 21-Oct-17.
 */

public class Livesearch extends AppCompatActivity implements RewardedVideoAdListener {

    StreetViewPanoramaFragment streetViewPanoramaFragment;
    UnityAds unityAds;
    public LatLng SAN_FRAN = new LatLng(37.765927, -122.449972);
    StreetViewPanoramaView mStreetViewPanoramaView;
    private StreetViewPanorama mPanorama;
    View dialogLayout1;
    AlertDialog dialog2;
    AlertDialog dialog,dialog1,dialog3;
    String plcaename,streetname;
    double latitude,longitude;
    int adload;
    private ProgressBar spinner;
    private RewardedVideoAd rewardedVideoAd;
    public static InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.livesearch);
        mInterstitialAd=new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3444255945927869/1011968739");
        requestNewInterstitial();
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                Alertdialog1();
                requestNewInterstitial();
            }
        });

        final Livesearch.UnityAdsListener unityAdsListener = new Livesearch.UnityAdsListener();

        unityAds=new UnityAds();
        unityAds.initialize(this, "1588491", unityAdsListener); // Company ID
//        unityAds.initialize(this, "1014764", unityAdsListener);  // Testing ID
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        rewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        /*streetViewPanoramaFragment =
                (StreetViewPanoramaFragment) getFragmentManager()
                        .findFragmentById(R.id.streetviewpanorama);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);*/
        mStreetViewPanoramaView = (StreetViewPanoramaView) findViewById(R.id.steet_view_panorama);


        mStreetViewPanoramaView.onCreate(savedInstanceState);
        dialog2=new AlertDialog.Builder(this).create();
        LayoutInflater inflater1 = getLayoutInflater();
        dialogLayout1 = inflater1.inflate(R.layout.custom1, null);
        Intent intent = getIntent();
        streetname = intent.getExtras().getString("epuzzlee");
        plcaename=streetname;
        CharSequence cs=streetname;
        spinner=(ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);
        autocompleteFragment.setText(cs);
        autocompleteFragment.setHint("Search Here ... ");
        mStreetViewPanoramaView.getStreetViewPanoramaAsync(new OnStreetViewPanoramaReadyCallback() {
            @Override
            public void onStreetViewPanoramaReady(StreetViewPanorama panorama) {
                LatitudeLongiget(streetname);

                panorama.setPosition(new LatLng(latitude, longitude));
                panorama.setOnStreetViewPanoramaChangeListener(new StreetViewPanorama.OnStreetViewPanoramaChangeListener() {
                    @Override
                    public void onStreetViewPanoramaChange(StreetViewPanoramaLocation streetViewPanoramaLocation) {
                        if (streetViewPanoramaLocation != null && streetViewPanoramaLocation.links != null) {

                        } else {
                            Alertdialog();

                        }
                    }
                });

                mPanorama=panorama;
                mPanorama.setOnStreetViewPanoramaChangeListener(new StreetViewPanorama.OnStreetViewPanoramaChangeListener() {
                    @Override
                    public void onStreetViewPanoramaChange(StreetViewPanoramaLocation streetViewPanoramaLocation) {
                        if (streetViewPanoramaLocation != null && streetViewPanoramaLocation.links != null) {

                        } else {
                            Alertdialog();
                        }
                    }
                });

            }
        });

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                plcaename=(String) place.getName();
                getLatitudeAndLongitudeFromGoogleMapForAddress((String) place.getName());
                adload=1;
//                Toast.makeText(getApplicationContext(),place.getName(),Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void requestNewInterstitial() {
        // TODO Auto-generated method stub
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mStreetViewPanoramaView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mStreetViewPanoramaView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mStreetViewPanoramaView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mStreetViewPanoramaView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mStreetViewPanoramaView.onSaveInstanceState(outState);
    }

    public void getLatitudeAndLongitudeFromGoogleMapForAddress(String searchedAddress){

        String addressStr = searchedAddress;
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addresses =
                    geoCoder.getFromLocationName(addressStr, 1);
            if (addresses.size() >  0) {
                double latitude = addresses.get(0).getLatitude();
                double  longtitude =
                        addresses.get(0).getLongitude();
                mPanorama.setPosition(new LatLng(latitude, longtitude));

            }

        } catch (IOException e) { // TODO Auto-generated catch block
            e.printStackTrace(); }
    }
    public  void OK(View view)
    {
        dialog2.dismiss();

        Intent i = new Intent(this, OtherStreetViews.class);
        i.putExtra("epuzzle", plcaename);
        startActivity(i);
        finish();

    }
    public  void NO(View view)
    {
        dialog2.dismiss();
        Intent i = new Intent(this, LivestreetViewstart.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();

    }
//    @Override
//    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
//        streetViewPanorama.setPosition(new LatLng(40.730610, -73.935242));
//    }

    @Override
    public void onBackPressed() {
        if(dialog2.isShowing())
        {
            dialog2.dismiss();
            Intent i = new Intent(this, LivestreetViewstart.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
        else
        {
            Intent i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
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

    public  void Alertdialog()
    {
        Toast.makeText(getApplicationContext(),"Street View for this city is not Available...",Toast.LENGTH_LONG).show();
        dialog=    new AlertDialog.Builder(this)
                .setIcon(R.drawable.iconmap)
                .setTitle("Live Street View is not Available")
                .setMessage("Watch Add to see the other map views of this city ")
                .setPositiveButton("Watch Add", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(UnityAds.isReady()){
                            UnityAds.show(Livesearch.this, "video");

                        }
                        else
                        {
                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                            }
                        }
//                        spinner.setVisibility(View.VISIBLE);
//                       Timermap();

                    }

                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent j = new Intent(getApplicationContext(), LivestreetViewstart.class);

                        startActivity(j);
                        finish();
                    }
                })
                .setCancelable(false)
                .show();


        // if you do the following it will be left aligned, doesn't look
        // correct
        // button.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_media_play,
        // 0, 0, 0);

        /*Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        button.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(
                R.drawable.iconmap), null, null, null);*/
        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        textView.setTextSize(20);
    }

    public  void Alertdialog1()
    {

        dialog1=    new AlertDialog.Builder(this)
                .setIcon(R.drawable.iconmap)
                .setTitle("Satellite Maps View Street Live")
                .setMessage("Do you want to continue... ")
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent i = new Intent(getApplicationContext(), OtherStreetViews.class);
                        i.putExtra("epuzzle", plcaename);
                        startActivity(i);
                        finish();
                    }

                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent j = new Intent(getApplicationContext(), LivestreetViewstart.class);

                        startActivity(j);
                        finish();
                    }
                })
                .setCancelable(false)
                .show();


        // if you do the following it will be left aligned, doesn't look
        // correct
        // button.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_media_play,
        // 0, 0, 0);


        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        textView.setTextSize(20);
    }

  /*  public  void Alertdialog2()
    {

        Toast.makeText(getApplicationContext(),"Street View for this city is not Available...",Toast.LENGTH_LONG).show();
      dialog3=    new AlertDialog.Builder(this)
                .setIcon(R.drawable.iconmap)
                .setTitle("Loading Video Failed")
                .setMessage("Do you want to Reload.. ")
                .setPositiveButton("Reload", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       loadRewardedVideoAd();
                    }

                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent j = new Intent(getApplicationContext(), com.jalsawasiapps.live.street.view.maps.navigation.LivestreetViewstart.class);

                        startActivity(j);
                        finish();
                    }
                })
.setCancelable(false)
                .show();


        // if you do the following it will be left aligned, doesn't look
        // correct
        // button.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_media_play,
        // 0, 0, 0);


        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        textView.setTextSize(20);
    }*/

    @Override
    public void onRewarded(RewardItem reward) {
        Toast.makeText(this, "other map views are unlocked" , Toast.LENGTH_SHORT).show();
        // Reward the user.
    }

    // The following listener methods are optional.
    @Override
    public void onRewardedVideoAdLeftApplication() {
        Toast.makeText(this, "onRewardedVideoAdLeftApplication",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        Alertdialog1();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {


    }

    @Override
    public void onRewardedVideoAdLoaded() {
//        Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdOpened() {
//        Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
//        Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
    }


    private void loadRewardedVideoAd() {
        rewardedVideoAd.loadAd("ca-app-pub-3444255945927869/6361886426", new AdRequest.Builder().build());
    }
    public void Timermap()
    {
        Intent i = new Intent(getApplicationContext(), OtherStreetViews.class);
        i.putExtra("epuzzle", plcaename);
        startActivity(i);
    }
    private class UnityAdsListener implements IUnityAdsListener {
        @Override
        public void onUnityAdsReady(String s) {

        }

        @Override
        public void onUnityAdsStart(String s) {
            //Called when a video begins playing
        }

        @Override
        public void onUnityAdsFinish(String s, UnityAds.FinishState finishState) {
            finish();
            System.exit(0);
        }

        @Override
        public void onUnityAdsError(UnityAds.UnityAdsError unityAdsError, String s) {
            //Called when the Unity Ads detects an error
        }
    }
}


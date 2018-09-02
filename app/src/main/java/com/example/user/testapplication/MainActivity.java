package com.example.user.testapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.NativeExpressAdView;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;
import com.startapp.android.publish.splash.SplashConfig;

public class MainActivity  extends Activity {

    private StartAppAd startapp = new StartAppAd(this);
    Button livesearch,rateus,more,share;
    public static InterstitialAd mInterstitialAd;
    int clickno;
    public static int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (count == 0) {
            //               Developer ID    App ID
            StartAppSDK.init(this,"168784222", "208703372", false);
            StartAppAd.showSplash(this, savedInstanceState, new SplashConfig()
                    .setTheme(SplashConfig.Theme.GLOOMY).setLogo(R.drawable.iconmap)
                    .setAppName("Satellite Maps View"));
            count++;
        }
        setContentView(R.layout.activity_main);
        livesearch=(Button)findViewById(R.id.live_search);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{android.Manifest.permission.INTERNET,android.Manifest.permission.ACCESS_NETWORK_STATE,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION
                },
                1);
        NativeExpressAdView adView = (NativeExpressAdView)findViewById(R.id.adView);

        AdRequest request = new AdRequest.Builder().build();
        adView.loadAd(request);
//        rateus=(Button)findViewById(R.id.rate_us) ;
        more=(Button)findViewById(R.id.more);
        share=(Button)findViewById(R.id.share);
        //==========================InterstialAd ========================
        mInterstitialAd = new InterstitialAd(this);

        /*from Main menu*/
        mInterstitialAd.setAdUnitId("ca-app-pub-3444255945927869/1011968739");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                if (clickno==2)
                {
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    clickno = 0;
                }
                else if(clickno==3)
                {
                  System.exit(0);
                    clickno=0;
                }

                requestNewInterstitial();
            }
        });

        requestNewInterstitial();

        //================================================
        /*if (clickno==2) {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();

            }
        }*/
        livesearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),LivestreetViewstart.class);
                startActivity(intent);
            }
        });
       /* rateus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id="
                                + "com.jalsawasiapps.super.bright.led.tiny.flashlight")));

clickno=1;
            }
        });*/
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://play.google.com/store/apps/developer?id=Dream+kodez"));
                startActivity(i);
                clickno=2;
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "");
                    String sAux = "\n\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=com.dreamkodez.satellitemapsview.livestreet.housetripplanner \n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });
    }
    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("SEE_YOUR_LOGCAT_TO_GET_YOUR_DEVICE_ID")
                .build();

        mInterstitialAd.loadAd(adRequest);

    }
    public void onshowAd()
    {
        startapp.showAd();
        startapp.loadAd();
    }
//    @Override
//    public void onBackPressed() {
//        Eixitrequest();
//    }

    @Override
    protected void onPause() {
        super.onPause();
        startapp.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (clickno==2) {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();

            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Handle the back button
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            clickno=3;
            if (clickno==3) {
                Eixitrequest();
            }


            //Ask the user if they want to quit

            return true;
        }
        else {
            return super.onKeyDown(keyCode, event);
        }

    }
    /*public void marshmallowpermissions()
    {

    }*/
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void Eixitrequest()
    {
        AlertDialog dialog=    new AlertDialog.Builder(this)
                .setIcon(R.drawable.iconmap)
                .setTitle("Rate Us")
                .setMessage("Do you want to rate us ? ")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Stop the activity
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("market://details?id="
                                        + "com.dreamkodez.satellitemapsview.livestreet.housetripplanner")));
                        clickno=2;

                    }

                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNeutralButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        }
                        else {
                            System.exit(0);
                        }
                    }
                })
                .show();
        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        textView.setTextSize(20);

    }
}

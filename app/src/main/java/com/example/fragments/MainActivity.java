package com.example.fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

public class MainActivity extends AppCompatActivity
{
    private static final int REQUEST_LOCATION =1 ;
    TextView tvLatitude;
    TextView tvLongitude;
    LocationManager lm;
    Button mapMe;
    Button textMe;
    String longitude;
    String latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         mapMe = findViewById(R.id.mapMe);
         textMe = findViewById(R.id.textMe);
        FragmentManager fragmentManager = getSupportFragmentManager();
        tvLatitude = (TextView) findViewById(R.id.tvLatitude);
        tvLongitude = (TextView) findViewById(R.id.tvLongitude);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else
        {

            Location loc = lm.getLastKnownLocation("gps");
            tvLatitude.setText(Double.toString(loc.getLatitude()));
            tvLongitude.setText(Double.toString(loc.getLongitude()));
            latitude =tvLatitude.getText().toString();
            longitude= tvLatitude.getText().toString();

            DisplayLocationListener locListenD= new DisplayLocationListener();
            lm.requestLocationUpdates("gps",30000L,10.0f,locListenD);
            mapMe.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    Bundle bundle = new Bundle();
                    bundle.putString("latitude", String.valueOf(loc.getLatitude()));
                    bundle.putString("longitude",String.valueOf(loc.getLongitude()));

                    GoogleMapLocation gl = new GoogleMapLocation();
                    gl.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView4, gl,null)
                            .setReorderingAllowed(true)
                            .addToBackStack("")
                            .commit();
                }
            });
            if(latitude.length() != 0 && longitude.length() !=0 ) {
                textMe.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("latitude", latitude);
                        bundle.putString("longitude",longitude);
                        TextMe textMe = new TextMe();
                        textMe.setArguments(bundle);
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragmentContainerView4, textMe, null)
                                .setReorderingAllowed(true)
                                .addToBackStack("")
                                .commit();
                    }
                });
            }
            else
            {
                Toast.makeText(this,"Press map me first !",Toast.LENGTH_SHORT).show();
            }
        }
       

    }

    public class DisplayLocationListener implements LocationListener
    {
        @Override
        public void onLocationChanged(@NonNull Location location)
        {
            tvLatitude.setText(Double.toString(location.getLatitude()));
            tvLongitude.setText(Double.toString(location.getLongitude()));
        }
    }
}
package com.example.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class GoogleMapLocation extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_google_map, container, false);
        Bundle bundle = getArguments();
        double lat = Double.parseDouble(bundle.getString("latitude"));
        double lng= Double.parseDouble(bundle.getString("longitude"));
        LatLng myCoordinates= new LatLng(lat,lng);

        SupportMapFragment supportMapFragment=(SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map_location);

        supportMapFragment.getMapAsync(new OnMapReadyCallback()
        {
            @Override
            public void onMapReady(@NonNull com.google.android.gms.maps.GoogleMap googleMap)
            {
                googleMap.clear();
                googleMap.addMarker(
                        new MarkerOptions().position(myCoordinates)
                );
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myCoordinates,15));
            }
        });
        return view;
    }
}
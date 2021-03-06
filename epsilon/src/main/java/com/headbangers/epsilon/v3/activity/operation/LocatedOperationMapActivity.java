package com.headbangers.epsilon.v3.activity.operation;

import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.model.Operation;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.text.DecimalFormat;
import java.text.ParseException;

@EActivity(R.layout.located_operation_map)
public class LocatedOperationMapActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;

    @Extra("operation")
    Operation operation;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    MapFragment mMapFragment;
    DecimalFormat gpsDf = new DecimalFormat("0.0000000");
    DecimalFormat df = new DecimalFormat("0.00");

    @AfterViews
    void init() {
        toolbar.setTitle(R.string.operation_location);
        toolbar.setSubtitle(operation.getCategory() + " - " + operation.getTiers() + " - " + df.format(operation.getAmount()) + "€");

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(this);

        mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container, mMapFragment);
        fragmentTransaction.commit();

        mMapFragment.getMapAsync(this);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        init();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            Number latitude = gpsDf.parse(operation.getLatitude());
            Number longitude = gpsDf.parse(operation.getLongitude());

            LatLng opLocation = new LatLng(latitude.doubleValue(), longitude.doubleValue());
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(opLocation)
                    .title(operation.getFormatedDateApplication())
                    .snippet(operation.getCategory() + " - " + operation.getTiers() + " - " + df.format(operation.getAmount()) + "€"));

            CircleOptions circleOptions = new CircleOptions()
                    .center(new LatLng(latitude.doubleValue(), longitude.doubleValue()))
                    .strokeColor(Color.parseColor("#3F51B5"))
                    .fillColor(Color.argb(100, 127, 173, 242))
                    .radius(100);//meters
            mMap.addCircle(circleOptions);

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(opLocation, 16f));
            marker.showInfoWindow();

        } catch (ParseException e) {
            // wrong locations
            Toast.makeText(this, "GPS data failed.", Toast.LENGTH_LONG).show();
            finish();
        }


    }

    @Override
    public void onClick(View v) {
        this.finish();
    }
}

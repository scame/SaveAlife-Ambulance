package com.example.scame.savealife.presentation.activities;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.scame.savealife.FusedLocationService;
import com.example.scame.savealife.R;
import com.example.scame.savealife.presentation.di.components.ApplicationComponent;
import com.example.scame.savealife.presentation.di.components.PointLocationComponent;
import com.example.scame.savealife.presentation.di.modules.PointLocationModule;
import com.example.scame.savealife.presentation.presenters.IPointLocationPresenter;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PointLocationActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private Marker currentPosition;

    private PointLocationComponent component;

    @Inject
    IPointLocationPresenter<IPointLocationPresenter.PointLocationView> presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.point_location_activity);

        ButterKnife.bind(this);

        configureMap();
        configureAutocomplete();
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter(FusedLocationService.BROADCAST_ACTION_UPDATE);
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(locationBroadcastReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(locationBroadcastReceiver);
    }

    private void configureMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void configureAutocomplete() {
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i("LOG_TAG", "Place: " + place.getName());
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 14));
            }

            @Override
            public void onError(Status status) {
                Log.i("LOG_TAG", "An error occurred: " + status);
            }
        });
    }

    @Override
    protected void inject(ApplicationComponent component) {
        this.component = component.getPointLocationComponent(new PointLocationModule());
        this.component.inject(this);
    }

    @OnClick(R.id.location_fab)
    public void onLocationFabClick() {
        startService(new Intent(this, FusedLocationService.class));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        googleMap.setOnMapClickListener(latLng ->
                googleMap.addMarker(new MarkerOptions().position(latLng)));
    }

    private final BroadcastReceiver locationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (currentPosition != null) {
                currentPosition.remove();
            }

            double lat = intent.getDoubleExtra(getString(R.string.lat_key), 0);
            double longitude = intent.getDoubleExtra(getString(R.string.long_key), 0);
            currentPosition = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, longitude)));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, longitude), 14));
        }
    };
}

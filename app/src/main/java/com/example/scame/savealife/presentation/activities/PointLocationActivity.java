package com.example.scame.savealife.presentation.activities;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;

import com.example.scame.savealife.FusedLocationService;
import com.example.scame.savealife.R;
import com.example.scame.savealife.data.entities.LatLongPair;
import com.example.scame.savealife.presentation.di.components.ApplicationComponent;
import com.example.scame.savealife.presentation.di.components.PointLocationComponent;
import com.example.scame.savealife.presentation.di.modules.PointLocationModule;
import com.example.scame.savealife.presentation.fragments.ConfirmDialogFragment;
import com.example.scame.savealife.presentation.presenters.IPointLocationPresenter;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PointLocationActivity extends BaseActivity implements OnMapReadyCallback,
                                            ConfirmDialogFragment.ConfirmDialogListener,
                                            IPointLocationPresenter.PointLocationView {

    public static final String BROADCAST_SEND_DESTINATION = "sendDestination";

    private static final int CIRCLE_RADIUS = 250;

    @BindView(R.id.start_fab) FloatingActionButton startFab;
    @BindView(R.id.stop_fab) FloatingActionButton stopFab;

    private GoogleMap googleMap;

    private LatLng destination;
    private LatLng currentPosition;
    private Marker currentPositionMarker;
    private Circle currentPositionCircle;

    private PointLocationComponent component;

    @Inject
    IPointLocationPresenter<IPointLocationPresenter.PointLocationView> presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.point_location_activity);

        ButterKnife.bind(this);
        presenter.setView(this);

        configureMap();
        configureAutocomplete();

        /*if (!isMyServiceRunning(FusedLocationService.class)) {
            startService(new Intent(this, FusedLocationService.class));
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.startLocationUpdates();

        IntentFilter filter = new IntentFilter(FusedLocationService.BROADCAST_UPDATE_LOCATION);
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(locationBroadcastReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        presenter.pause();

        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(locationBroadcastReceiver);
    }

    @Override
    public void updateCurrentLocation(LatLongPair latLongPair) {
        if (googleMap != null) {

            if (currentPositionMarker != null) {
                currentPositionMarker.remove();
                currentPositionCircle.remove();
            }

            currentPosition = new LatLng(latLongPair.getLatitude(), latLongPair.getLongitude());
            currentPositionMarker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latLongPair.getLatitude(), latLongPair.getLongitude())));
            currentPositionCircle = googleMap.addCircle(new CircleOptions()
                    .fillColor(R.color.colorPrimary)
                    .strokeColor(R.color.colorPrimaryDark)
                    .center(new LatLng(latLongPair.getLatitude(), latLongPair.getLongitude()))
                    .radius(CIRCLE_RADIUS));
        }
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
                updateDestinationPoint(place.getLatLng());
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

    @OnClick(R.id.my_location_fab)
    public void onLocationFabClick() {
        if (currentPosition != null & googleMap != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 14));
        }
    }

    @OnClick(R.id.start_fab)
    public void onStartFabClick() {
        showConfirmDialog();
    }

    @OnClick(R.id.stop_fab)
    public void onStopFabClick() {
        stopService(new Intent(this, FusedLocationService.class)); // FIXME: 23/08/16
        startFab.setVisibility(View.VISIBLE);
        stopFab.setVisibility(View.GONE);
    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        googleMap.setOnMapLongClickListener(this::updateDestinationPoint);
    }

    private void updateDestinationPoint(LatLng latLng) {
        googleMap.clear();
        googleMap.addMarker(new MarkerOptions().position(latLng));

        presenter.computeDirection(new LatLongPair(50.3923508, 30.4787373),
                new LatLongPair(latLng.latitude, latLng.longitude));

        this.destination = latLng;
    }

    private void showConfirmDialog() {
        if (destination != null) {
            presenter.geocodeToHumanReadableFormat(destination.latitude + "," + destination.longitude);
        } else {
            buildNoDestinationSelectedSnackbar();
        }
    }

    private void buildNoDestinationSelectedSnackbar() {
        CoordinatorLayout coordinatorLayout = ButterKnife.findById(this, R.id.map_container);
        Snackbar.make(coordinatorLayout, "Please, choose the destination", Snackbar.LENGTH_LONG).show();
    }

    // presenter's callback
    @Override
    public void showHumanReadableAddress(String address) {
        ConfirmDialogFragment confirmDialog =
                ConfirmDialogFragment.newInstance("Destination confirmation", address);
        confirmDialog.setConfirmationListener(this);
        confirmDialog.show(getFragmentManager(), "ok");
    }

    @Override
    public void destinationPointConfirmed() {
        startFab.setVisibility(View.GONE);
        stopFab.setVisibility(View.VISIBLE);

        presenter.computeDirection(new LatLongPair(currentPosition.latitude, currentPosition.longitude),
                                    new LatLongPair(destination.latitude, destination.longitude));

        startSendingLocationToServer();
    }

    private void startSendingLocationToServer() {
        Intent locationIntent = new Intent(BROADCAST_SEND_DESTINATION);
        locationIntent.putExtra(getString(R.string.lat_key), destination.latitude);
        locationIntent.putExtra(getString(R.string.long_key), destination.longitude);

        LocalBroadcastManager.getInstance(this).sendBroadcast(locationIntent);
    }

    @Override
    public void drawDirectionPolyline(PolylineOptions polyline) {
        googleMap.addPolyline(polyline);
    }


    private final BroadcastReceiver locationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            double latitude = intent.getDoubleExtra(getString(R.string.lat_key), 0);
            double longitude = intent.getDoubleExtra(getString(R.string.long_key), 0);
            currentPosition = new LatLng(latitude, longitude);

            if (googleMap != null) {
                // TODO: update position
            }
            Log.i("onxCurrentPos", currentPosition.latitude + "," + currentPosition.longitude);
        }
    };
}

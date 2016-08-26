package com.example.scame.savealife.presentation.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PointLocationActivity extends BaseActivity implements OnMapReadyCallback,
                                            ConfirmDialogFragment.ConfirmDialogListener,
                                            IPointLocationPresenter.PointLocationView {

    private static final int CIRCLE_RADIUS = 250;

    @BindView(R.id.start_fab) FloatingActionButton startFab;
    @BindView(R.id.stop_fab) FloatingActionButton stopFab;

    private GoogleMap googleMap;

    private LatLng destination;
    private LatLng currentPosition;

    private Marker currentPositionMarker;
    private Circle currentPositionCircle;
    private Polyline currentPolyline;

    private Marker destinationMarker;

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
    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();

        presenter.pause();
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
        stopService(new Intent(this, FusedLocationService.class));
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
        this.destination = latLng;

        if (destinationMarker != null) {
            destinationMarker.remove();
        }

        destinationMarker = googleMap.addMarker(new MarkerOptions().position(latLng));

        presenter.computeDirection(new LatLongPair(currentPosition.latitude, currentPosition.longitude),
                new LatLongPair(destination.latitude, destination.longitude));
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

    // dialog fragment's callback
    @Override
    public void destinationPointConfirmed() {
        startFab.setVisibility(View.GONE);
        stopFab.setVisibility(View.VISIBLE);

        presenter.computeDirection(new LatLongPair(currentPosition.latitude, currentPosition.longitude),
                                    new LatLongPair(destination.latitude, destination.longitude));

        startService(new Intent(this, FusedLocationService.class));
    }


    @Override
    public void drawDirectionPolyline(PolylineOptions polyline) {
        if (currentPolyline != null) {
            currentPolyline.remove();
        }

        currentPolyline = googleMap.addPolyline(polyline);
    }
}

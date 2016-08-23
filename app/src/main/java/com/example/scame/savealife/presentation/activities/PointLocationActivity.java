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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PointLocationActivity extends BaseActivity implements OnMapReadyCallback,
                                            ConfirmDialogFragment.ConfirmDialogListener,
                                            IPointLocationPresenter.PointLocationView {

    @BindView(R.id.start_fab) FloatingActionButton startFab;
    @BindView(R.id.stop_fab) FloatingActionButton stopFab;

    private GoogleMap googleMap;
    private LatLng destination;

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

        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMapClickListener(this::updateDestinationPoint);
    }

    private void updateDestinationPoint(LatLng latLng) {
        googleMap.clear();
        googleMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));

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
        startService(new Intent(this, FusedLocationService.class));
        startFab.setVisibility(View.GONE);
        stopFab.setVisibility(View.VISIBLE);

        // TODO: call presenter with origin and destination points
        presenter.computeDirection(new LatLongPair(50.3923508, 30.4787373),
                                    new LatLongPair(destination.latitude, destination.longitude));
    }

    @Override
    public void drawDirectionPolyline(PolylineOptions polyline) {
        googleMap.addPolyline(polyline);
    }
}

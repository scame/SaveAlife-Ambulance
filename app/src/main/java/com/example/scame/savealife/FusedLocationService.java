package com.example.scame.savealife;


import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.scame.savealife.data.entities.LatLongPair;
import com.example.scame.savealife.data.repository.ILocationDataManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

public class FusedLocationService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleApiClient googleApiClient;

    private long UPDATE_INTERVAL = 10 * 1000;
    private long FASTEST_INTERVAL = 2000;
    private long SMALLEST_DISPLACEMENT = 300;

    @Inject
    ILocationDataManager locationDataManager;

    @Override
    public void onCreate() {
        super.onCreate();

        SaveAlifeApp.getAppComponent().inject(this);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        googleApiClient.connect();

        return START_STICKY;
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("DEBUG", "onConnectionSuspended");
    }


    @Override
    @SuppressWarnings({"MissingPermission"})
    public void onConnected(@Nullable Bundle bundle) {

        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        sendLocationToServer(currentLocation);

        if (currentLocation != null) {
            Log.i("DEBUG", "current location: " + currentLocation.toString());
        }

        startLocationUpdates();
    }


    @SuppressWarnings({"MissingPermission"})
    private void startLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setFastestInterval(FASTEST_INTERVAL)
                .setInterval(UPDATE_INTERVAL)
                .setSmallestDisplacement(SMALLEST_DISPLACEMENT);

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("DEBUG", "failedToConnect");
    }

    @Override
    public void onLocationChanged(Location location) {
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();


        sendLocationToServer(location);
    }

    private void sendLocationToServer(Location location) {
        locationDataManager.sendLocationToServer(new LatLongPair(location.getLatitude(), location.getLongitude()));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (googleApiClient.isConnected() || googleApiClient.isConnecting()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

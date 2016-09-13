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
import com.example.scame.savealife.data.repository.IFirebaseTokenManager;
import com.example.scame.savealife.data.repository.ILocationDataManager;
import com.example.scame.savealife.data.repository.IMessagesDataManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FusedLocationService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleApiClient googleApiClient;

    private long UPDATE_INTERVAL = 10 * 1000;
    private long FASTEST_INTERVAL = 2000;
    private long SMALLEST_DISPLACEMENT = 300;

    @Inject ILocationDataManager locationDataManager;

    @Inject IMessagesDataManager messagesDataManager;

    @Inject IFirebaseTokenManager tokenManager;

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

        if (currentLocation != null) {
            handleLocationUpdate(currentLocation.getLatitude(), currentLocation.getLongitude());
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


        handleLocationUpdate(location.getLatitude(), location.getLongitude());
    }

    private void handleLocationUpdate(double latitude, double longitude) {
        LatLongPair latLongPair = new LatLongPair(latitude, longitude);

        locationDataManager.saveCurrentLocation(latLongPair);

        // send only if token is already generated
        if (!tokenManager.getActiveToken().equals("")) {
            messagesDataManager.sendLocationMessage()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
            //    .subscribe(responseBody -> Log.i("onNextSend", "done"));
        }
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

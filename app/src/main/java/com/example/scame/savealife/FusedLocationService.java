package com.example.scame.savealife;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.scame.savealife.presentation.activities.PointLocationActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

public class FusedLocationService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final String BROADCAST_UPDATE_LOCATION = "updateLatLong";

    private GoogleApiClient googleApiClient;

    private LocationRequest locationRequest;

    private long UPDATE_INTERVAL = 10 * 1000;
    private long FASTEST_INTERVAL = 2000;
    private long SMALLEST_DISPLACEMENT = 100;

    private boolean sendLocationToServer = false;
    private Location previousLocation;
    private LatLng destination;

    @Override
    public void onCreate() {
        super.onCreate();

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        IntentFilter filter = new IntentFilter(PointLocationActivity.BROADCAST_SEND_DESTINATION);
        LocalBroadcastManager.getInstance(this).registerReceiver(startSendingLocationToServer, filter);
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
        sendLocationToUser(currentLocation);

        if (currentLocation != null) {
            Log.i("DEBUG", "current location: " + currentLocation.toString());
        }

        startLocationUpdates();
    }


    @SuppressWarnings({"MissingPermission"})
    private void startLocationUpdates() {
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setFastestInterval(FASTEST_INTERVAL)
                .setInterval(UPDATE_INTERVAL);

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
        sendLocationToUser(location);
    }

    private void sendLocationToServer(Location location) {

        if (sendLocationToServer && isSmallestDisplacement(location)) {
            // TODO: send location info to server
        }
    }

    private boolean isSmallestDisplacement(Location currentLocation) {
        if (previousLocation == null || previousLocation.distanceTo(currentLocation) > SMALLEST_DISPLACEMENT) {
            previousLocation = currentLocation;
            return true;
        }

        return false;
    }

    private void sendLocationToUser(Location location) {
        Intent i = new Intent(BROADCAST_UPDATE_LOCATION);
        i.putExtra(getString(R.string.lat_key), location.getLatitude());
        i.putExtra(getString(R.string.long_key), location.getLongitude());

        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
        lbm.sendBroadcast(i);
    }

    // fired when user accepts destination point
    private final BroadcastReceiver startSendingLocationToServer = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            double latitude = intent.getDoubleExtra(getString(R.string.lat_key), 0);
            double longitude = intent.getDoubleExtra(getString(R.string.long_key), 0);

            destination = new LatLng(latitude, longitude);
            sendLocationToServer = true;
            Log.i("onxReceived", destination.latitude + "," + destination.longitude);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(startSendingLocationToServer);

        if (googleApiClient != null) {
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

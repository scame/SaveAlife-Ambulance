package com.example.scame.savealife.data.repository;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.scame.savealife.R;
import com.example.scame.savealife.SaveAlifeApp;
import com.example.scame.savealife.data.entities.DestinationEntity;
import com.example.scame.savealife.data.entities.LatLongPair;
import com.example.scame.savealife.data.entities.LocationMessageEntity;
import com.example.scame.savealife.data.entities.TokenUpdateEntity;
import com.example.scame.savealife.data.rest.ServerApi;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import rx.Observable;

public class MessagesDataManagerImp implements IMessagesDataManager {

    private static final String SENDER_TYPE = "ambulance";

    private Retrofit retrofit;
    private ServerApi serverApi;

    private Context context;
    private SharedPreferences sharedPrefs;

    private IFirebaseTokenManager tokenManager;

    public MessagesDataManagerImp() {
        retrofit = SaveAlifeApp.getAppComponent().getRetrofit();
        serverApi = retrofit.create(ServerApi.class);
        tokenManager = new FirebaseTokenManagerImp();

        context = SaveAlifeApp.getAppComponent().getApp();
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public Observable<ResponseBody> sendLocationMessage() {
        LatLongPair latLong = getCurrentLatLong();

        LocationMessageEntity locationEntity = new LocationMessageEntity();

        locationEntity.setRole(SENDER_TYPE);
        locationEntity.setCurrentToken(tokenManager.getActiveToken());
        locationEntity.setCurrentLat(latLong.getLatitude());
        locationEntity.setCurrentLon(latLong.getLongitude());

        return serverApi.sendLocationToServer(locationEntity);
    }

    @Override
    public Observable<ResponseBody> sendUpdateTokenRequest() {
        TokenUpdateEntity tokenUpdateEntity = new TokenUpdateEntity();

        tokenUpdateEntity.setCurrentToken(tokenManager.getActiveToken());
        tokenUpdateEntity.setOldToken(tokenManager.getOldToken());

        return serverApi.tokenUpdateRequest(tokenUpdateEntity);
    }

    @Override
    public Observable<ResponseBody> sendDestinationMessage(LatLongPair destination) {
        LatLongPair currentLatLong = getCurrentLatLong();

        DestinationEntity destinationEntity = new DestinationEntity();

        destinationEntity.setCurrentLat(currentLatLong.getLatitude());
        destinationEntity.setCurrentLon(currentLatLong.getLongitude());
        destinationEntity.setDestinationLat(destination.getLatitude());
        destinationEntity.setDestinationLon(destination.getLongitude());
        destinationEntity.setCurrentToken(tokenManager.getActiveToken());
        destinationEntity.setRole(SENDER_TYPE);

        return serverApi.sendDestination(destinationEntity);
    }

    // TODO: current lat long can be not available, fix this
    private LatLongPair getCurrentLatLong() {
        double latitude = Double.valueOf(sharedPrefs.getString(context.getString(R.string.current_latitude), ""));
        double longitude = Double.valueOf(sharedPrefs.getString(context.getString(R.string.current_longitude), ""));

        return new LatLongPair(latitude, longitude);
    }
}

package com.example.scame.savealife.data.di;

import com.example.scame.savealife.data.repository.DirectionsDataManagerImp;
import com.example.scame.savealife.data.repository.FirebaseTokenManagerImp;
import com.example.scame.savealife.data.repository.GeocodingDataManagerImp;
import com.example.scame.savealife.data.repository.IDirectionsDataManager;
import com.example.scame.savealife.data.repository.IFirebaseTokenManager;
import com.example.scame.savealife.data.repository.IGeocodingDataManager;
import com.example.scame.savealife.data.repository.ILocationDataManager;
import com.example.scame.savealife.data.repository.IMessagesDataManager;
import com.example.scame.savealife.data.repository.LocationDataManagerImp;
import com.example.scame.savealife.data.repository.MessagesDataManagerImp;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class DataModule {

    @Singleton
    @Provides
    OkHttpClient provideOkHttp() {
        return new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    IGeocodingDataManager provideGeocodingDataManager() {
        return new GeocodingDataManagerImp();
    }

    @Singleton
    @Provides
    IDirectionsDataManager provideDirectionsDatamanager() {
        return new DirectionsDataManagerImp();
    }

    @Singleton
    @Provides
    ILocationDataManager provideLocationDataManager() {
        return new LocationDataManagerImp();
    }

    @Singleton
    @Provides
    IMessagesDataManager provideMessagesDataManager() {
        return new MessagesDataManagerImp();
    }

    @Singleton
    @Provides
    IFirebaseTokenManager provideFirebaseTokenManager() {
        return new FirebaseTokenManagerImp();
    }
}

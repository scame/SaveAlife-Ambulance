package com.example.scame.savealife.data.di;

import com.example.scame.savealife.data.repository.FileDataManagerImp;
import com.example.scame.savealife.data.repository.IFileDataManager;
import com.example.scame.savealife.data.repository.IMapsDataManager;
import com.example.scame.savealife.data.repository.MapsDataManagerImp;
import com.graphhopper.util.Downloader;

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
                .baseUrl("https://gooogle.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    Downloader provideDownloader() {
        return new Downloader("Graphhopper android");
    }

    @Singleton
    @Provides
    IFileDataManager provideFileDataManager() {
        return new FileDataManagerImp();
    }

    @Singleton
    @Provides
    IMapsDataManager provideMapsDataManager() {
        return new MapsDataManagerImp();
    }
}

package com.example.scame.savealife.data.di;

import com.example.scame.savealife.data.repository.FileDataManagerImp;
import com.example.scame.savealife.data.repository.IFileDataManager;
import com.graphhopper.util.Downloader;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class DataModule {

    OkHttpClient provideOkHttp() {
        return new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    Retrofit provideRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl("https://gooogle.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    Downloader provideDownloader() {
        return new Downloader("Graphhopper android");
    }

    IFileDataManager provideFileDataManager() {
        return new FileDataManagerImp();
    }
}

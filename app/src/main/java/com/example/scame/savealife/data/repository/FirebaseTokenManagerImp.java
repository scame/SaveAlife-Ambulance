package com.example.scame.savealife.data.repository;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.scame.savealife.R;
import com.example.scame.savealife.SaveAlifeApp;

public class FirebaseTokenManagerImp implements IFirebaseTokenManager {

    private SharedPreferences sharedPrefs;
    private Context context;

    public FirebaseTokenManagerImp() {
        context = SaveAlifeApp.getAppComponent().getApp();
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public void saveRefreshedToken(String token) {
        sharedPrefs.edit().putString(getString(R.string.refreshed_token_key), token).apply();
    }

    @Override
    public void saveOldToken(String token) {
        sharedPrefs.edit().putString(getString(R.string.old_token_key), token).apply();
    }

    @Override
    public String getActiveToken() {
        return sharedPrefs.getString(getString(R.string.refreshed_token_key), "");
    }

    @Override
    public String getOldToken() {
        return sharedPrefs.getString(getString(R.string.old_token_key), "");
    }

    private String getString(int stringId) {
        return context.getString(stringId);
    }
}

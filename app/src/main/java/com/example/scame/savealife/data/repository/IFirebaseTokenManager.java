package com.example.scame.savealife.data.repository;

public interface IFirebaseTokenManager {

    void saveRefreshedToken(String token);

    void saveOldToken(String token);

    String getActiveToken();

    String getOldToken();
}

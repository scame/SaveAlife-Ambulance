package com.example.scame.savealife.firebase;


import android.util.Log;

import com.example.scame.savealife.data.repository.FirebaseTokenManagerImp;
import com.example.scame.savealife.data.repository.IFirebaseTokenManager;
import com.example.scame.savealife.data.repository.IMessagesDataManager;
import com.example.scame.savealife.data.repository.MessagesDataManagerImp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "logTokenRefresh";

    private IFirebaseTokenManager tokenManager;

    private IMessagesDataManager messagesDataManager;

    public MyFirebaseInstanceIDService() {
        tokenManager = new FirebaseTokenManagerImp();
        messagesDataManager = new MessagesDataManagerImp();
    }

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.i(TAG, "Refreshed token: " + refreshedToken);

        cacheToken(refreshedToken);

        if (!tokenManager.getOldToken().equals("")) {
            messagesDataManager.sendUpdateTokenRequest();
        }
    }


    private void cacheToken(String token) {
        tokenManager.saveOldToken(tokenManager.getActiveToken());
        tokenManager.saveRefreshedToken(token);
    }
}

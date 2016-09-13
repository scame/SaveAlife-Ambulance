package com.example.scame.savealife.data.repository;


import com.example.scame.savealife.data.entities.LatLongPair;

import okhttp3.ResponseBody;
import rx.Observable;

public interface IMessagesDataManager {

    Observable<ResponseBody> sendDestinationMessage(LatLongPair latLongPair);

    Observable<ResponseBody> sendLocationMessage();

    Observable<ResponseBody> sendUpdateTokenRequest();
}

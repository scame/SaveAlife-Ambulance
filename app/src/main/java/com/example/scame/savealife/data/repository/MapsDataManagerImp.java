package com.example.scame.savealife.data.repository;


import com.example.scame.savealife.SaveAlifeApp;
import com.graphhopper.util.Constants;
import com.graphhopper.util.Downloader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;

public class MapsDataManagerImp implements IMapsDataManager {

    public static final int LOCAL_AREA = 0;
    public static final int REMOTE_AREA = 1;

    private String fileListURL = "http://download2.graphhopper.com/public/maps/"
            + Constants.getMajorVersion() + "/";
    private String prefixURL = fileListURL;

    @Override
    public Observable<List<String>> getRemoteAreaList() {
        Downloader downloader = SaveAlifeApp.getAppComponent().provideDownloader();
        String[] lines;
        try {
            lines = downloader
                    .downloadAsString(fileListURL, false)
                    .split("\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<String> res = new ArrayList<>();

        for (String str : lines) {
            int index = str.indexOf("href=\"");
            if (index >= 0) {
                index += 6;
                int lastIndex = str.indexOf(".ghz", index);
                if (lastIndex >= 0)
                    res.add(prefixURL + str.substring(index, lastIndex)
                            + ".ghz");
            }
        }

        return Observable.just(res);
    }

    @Override
    public Observable<List<String>> getLocalAreaList() {
        File mapsFolder = SaveAlifeApp.getAppComponent()
                .getFileDataManager()
                .getMapsFolder();

        List<String> nameList = new ArrayList<>();
        String[] files = mapsFolder.list((dir, filename) ->
                filename != null && (filename.endsWith(".ghz")
                        || filename.endsWith("-gh")));

        Collections.addAll(nameList, files);

        return Observable.just(nameList);
    }
}

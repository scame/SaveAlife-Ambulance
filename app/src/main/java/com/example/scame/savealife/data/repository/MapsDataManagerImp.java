package com.example.scame.savealife.data.repository;


import com.example.scame.savealife.SaveAlifeApp;
import com.example.scame.savealife.data.entities.LatLongPair;
import com.example.scame.savealife.data.mappers.NameToFullNameMapper;
import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.PathWrapper;
import com.graphhopper.util.Constants;
import com.graphhopper.util.Downloader;
import com.graphhopper.util.Helper;
import com.graphhopper.util.Parameters;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class MapsDataManagerImp implements IMapsDataManager {

    public static final int LOCAL_AREA = 0;
    public static final int REMOTE_AREA = 1;

    private NameToFullNameMapper mapper;

    private String fileListURL = "http://download2.graphhopper.com/public/maps/"
            + Constants.getMajorVersion() + "/";
    private String prefixURL = fileListURL;

    // provide with dagger later
    public MapsDataManagerImp() {
        mapper = new NameToFullNameMapper();
    }

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

    @Override
    public Observable<Void> downloadMap(String downloadURL) {
        File mapsFolder = SaveAlifeApp.getAppComponent().getFileDataManager().getMapsFolder();
        Downloader downloader = SaveAlifeApp.getAppComponent().provideDownloader();

        return Observable.create(subscriber -> {
            String localFolder = Helper.pruneFileEnd(FileDataManagerImp.getFileName(downloadURL));
            localFolder = new File(mapsFolder, localFolder + "-gh").getAbsolutePath();

            downloader.setTimeout(30000);

            try {
                downloader.downloadAndUnzip(downloadURL, localFolder, val -> {
                    //publishProgress((int) val);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

            subscriber.onCompleted();
        });
    }

    @Override
    public Observable<PathWrapper> calculatePath(LatLongPair pair) {
        GraphHopper hopper = SaveAlifeApp.getAppComponent().provideGraphhopper();

        GHRequest req = new GHRequest(pair.getFromLat(), pair.getFromLon(),
                pair.getToLat(), pair.getToLon()).
                setAlgorithm(Parameters.Algorithms.DIJKSTRA_BI);
        req.getHints().put(Parameters.Routing.INSTRUCTIONS, "false");
        GHResponse resp = hopper.route(req);

        return Observable.just(resp.getBest());
    }

    @Override
    public Observable<Void> loadGraphStorage(String path) {
        GraphHopper hopper = SaveAlifeApp.getAppComponent().provideGraphhopper();

        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                String directoryPath = path.substring(0, path.lastIndexOf("/"));
                hopper.load(new File(directoryPath).getAbsolutePath());
                subscriber.onCompleted();
            }
        });
    }
}

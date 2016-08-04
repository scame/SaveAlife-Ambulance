package com.example.scame.savealife.presentation.presenters;


import android.util.Log;

import com.example.scame.savealife.SaveAlifeApp;
import com.example.scame.savealife.data.repository.MapsDataManagerImp;
import com.example.scame.savealife.domain.usecases.DefaultSubscriber;
import com.example.scame.savealife.domain.usecases.DownloadMapUseCase;
import com.example.scame.savealife.domain.usecases.GetLocalAreasUseCase;
import com.example.scame.savealife.domain.usecases.GetRemoteAreasUseCase;
import com.example.scame.savealife.presentation.AndroidHelper;
import com.graphhopper.util.Helper;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MapSelectionPresenterImp<T extends IMapSelectionPresenter.MapSelectionView>
                                                implements IMapSelectionPresenter<T> {

    private GetRemoteAreasUseCase remoteUseCase;
    private GetLocalAreasUseCase localUseCase;
    private DownloadMapUseCase downloadMapUseCase;

    private T view;
    private File mapsFolder;

    private String downloadURL;

    private String currentArea;

    public MapSelectionPresenterImp(GetRemoteAreasUseCase remoteUseCase,
                                    GetLocalAreasUseCase localAreasUseCase,
                                    DownloadMapUseCase downloadMapUseCase) {

        this.remoteUseCase = remoteUseCase;
        this.localUseCase = localAreasUseCase;
        this.downloadMapUseCase = downloadMapUseCase;
    }

    public interface MySpinnerListener {
        void onSelect(String selectedArea, String selectedFile);
    }

    @Override
    public void setView(T view) {
        this.view = view;
    }

    @Override
    public void resume() {
        mapsFolder = SaveAlifeApp.getAppComponent().getFileDataManager().getMapsFolder();

        if (mapsFolder.exists()) {
            mapsFolder.mkdirs();
        }

        localUseCase.execute(new LocalMapsSubscriber());
        remoteUseCase.execute(new RemoteMapsSubscriber());
    }

    public void initFiles(String area) {
        currentArea = area;
        downloadingFiles();
    }

    MySpinnerListener spinnerListener = buildSpinnerListener();

    private void onFetchedAreaList(List<String> nameList, int areaType) {
        if (nameList == null || nameList.isEmpty()) {
            //logUser("No maps created for your version!? " + fileListURL);
            return;
        }


        Map<String, String> nameToFullName = getNameToFullNameMap(nameList);

        if (areaType == MapsDataManagerImp.LOCAL_AREA) {
            view.initUIcomponents(nameList, nameToFullName, MapsDataManagerImp.LOCAL_AREA, spinnerListener);
        } else if (areaType == MapsDataManagerImp.REMOTE_AREA) {
            view.initUIcomponents(nameList, nameToFullName, MapsDataManagerImp.REMOTE_AREA, spinnerListener);
        }
    }

    private MySpinnerListener buildSpinnerListener() {
        return (selectedArea, selectedFile) -> {
            Log.i("inSpinner", selectedArea);
            if (selectedFile == null
                    || new File(mapsFolder, selectedArea + ".ghz").exists()
                    || new File(mapsFolder, selectedArea + "-gh").exists()) {
                downloadURL = null;
            } else {
                downloadURL = selectedFile;

                initFiles(selectedArea);
            }
        };
    }

    private Map<String, String> getNameToFullNameMap(List<String> nameList) {
        final Map<String, String> nameToFullName = new TreeMap<>();

        for (String fullName : nameList) {
            String tmp = Helper.pruneFileEnd(fullName);
            if (tmp.endsWith("-gh"))
                tmp = tmp.substring(0, tmp.length() - 3);

            tmp = AndroidHelper.getFileName(tmp);
            nameToFullName.put(tmp, fullName);
        }

        nameList.clear();
        nameList.addAll(nameToFullName.keySet());

        return nameToFullName;
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    void downloadingFiles() {
        final File areaFolder = new File(mapsFolder, currentArea + "-gh");
        if (downloadURL == null || areaFolder.exists()) {
            //loadMap(areaFolder);
            return;
        }

        view.startDownloading(downloadURL);
        downloadMapUseCase.setDownloadUrl(downloadURL);
        downloadMapUseCase.execute(new MapsDownloaderSubscriber());
    }

    private final class LocalMapsSubscriber extends DefaultSubscriber<List<String>> {

        @Override
        public void onNext(List<String> strings) {
            super.onNext(strings);

            MapSelectionPresenterImp.this.onFetchedAreaList(strings, MapsDataManagerImp.LOCAL_AREA);
        }
    }

    private final class RemoteMapsSubscriber extends DefaultSubscriber<List<String>> {

        @Override
        public void onNext(List<String> strings) {
            super.onNext(strings);

            MapSelectionPresenterImp.this.onFetchedAreaList(strings, MapsDataManagerImp.REMOTE_AREA);
        }
    }

    private final class MapsDownloaderSubscriber extends DefaultSubscriber<Integer> {

        @Override
        public void onCompleted() {
            super.onCompleted();

            view.hideDownloading();
            //loadMap(areaFolder);
        }
    }

}

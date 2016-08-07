package com.example.scame.savealife.presentation.presenters;


import com.example.scame.savealife.SaveAlifeApp;
import com.example.scame.savealife.data.repository.MapsDataManagerImp;
import com.example.scame.savealife.domain.usecases.DefaultSubscriber;
import com.example.scame.savealife.domain.usecases.DownloadMapUseCase;
import com.example.scame.savealife.domain.usecases.GetLocalAreasUseCase;
import com.example.scame.savealife.domain.usecases.GetRemoteAreasUseCase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapSelectionPresenterImp<T extends IMapSelectionPresenter.MapSelectionView>
                                                implements IMapSelectionPresenter<T> {

    private GetRemoteAreasUseCase remoteUseCase;
    private GetLocalAreasUseCase localUseCase;
    private DownloadMapUseCase downloadMapUseCase;

    private T view;

    private File mapsFolder;

    private String downloadURL;

    private String currentArea;

    private MySpinnerListener spinnerListener;

    public MapSelectionPresenterImp(GetRemoteAreasUseCase remoteUseCase,
                                    GetLocalAreasUseCase localAreasUseCase,
                                    DownloadMapUseCase downloadMapUseCase) {

        this.remoteUseCase = remoteUseCase;
        this.localUseCase = localAreasUseCase;
        this.downloadMapUseCase = downloadMapUseCase;

        spinnerListener = buildSpinnerListener();
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

    private void onFetchedAreaList(Map<String, String> nameMap, int areaType) {
        if (nameMap == null || nameMap.isEmpty()) {
            //logUser("No maps created for your version!? " + fileListURL);
            return;
        }

        List<String> nameList = new ArrayList<>(nameMap.keySet());
        if (areaType == MapsDataManagerImp.LOCAL_AREA) {
            view.initUIcomponents(nameList, nameMap,
                    MapsDataManagerImp.LOCAL_AREA, spinnerListener);
        } else if (areaType == MapsDataManagerImp.REMOTE_AREA) {
            view.initUIcomponents(nameList, nameMap,
                    MapsDataManagerImp.REMOTE_AREA, spinnerListener);
        }
    }

    private MySpinnerListener buildSpinnerListener() {
        return (selectedArea, selectedFile) -> {
            if (selectedFile == null
                    || new File(mapsFolder, selectedArea + ".ghz").exists()
                    || new File(mapsFolder, selectedArea + "-gh").exists()) {
                downloadURL = null;
            } else {
                downloadURL = selectedFile;
            }

            initFiles(selectedArea);
        };
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    void downloadingFiles() {
        final File areaFolder = new File(mapsFolder, currentArea + "-gh/" + currentArea + ".map");
        if (downloadURL == null || areaFolder.exists()) {
            view.loadMap(areaFolder.toString());
            return;
        }

        view.startDownloading(downloadURL);
        downloadMapUseCase.setDownloadUrl(downloadURL);
        downloadMapUseCase.execute(new MapsDownloaderSubscriber());
    }

    private final class LocalMapsSubscriber extends DefaultSubscriber<Map<String, String>> {

        @Override
        public void onNext(Map<String, String> nameMap) {
            super.onNext(nameMap);

            MapSelectionPresenterImp.this.onFetchedAreaList(nameMap, MapsDataManagerImp.LOCAL_AREA);
        }
    }

    private final class RemoteMapsSubscriber extends DefaultSubscriber<Map<String, String>> {

        @Override
        public void onNext(Map<String, String> nameMap) {
            super.onNext(nameMap);

            MapSelectionPresenterImp.this.onFetchedAreaList(nameMap, MapsDataManagerImp.REMOTE_AREA);
        }
    }

    private final class MapsDownloaderSubscriber extends DefaultSubscriber<Void> {

        @Override
        public void onCompleted() {
            super.onCompleted();

            String area = mapsFolder.toString() + currentArea + "-gh/" + currentArea + ".map";
            view.hideDownloading();
            view.loadMap(area);
        }
    }
}

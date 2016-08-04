package com.example.scame.savealife.presentation.presenters;


import com.example.scame.savealife.data.repository.IFileDataManager;
import com.example.scame.savealife.data.repository.MapsDataManagerImp;
import com.example.scame.savealife.domain.usecases.DefaultSubscriber;
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

    private T view;
    private File mapsFolder;

    private String downloadURL;

    private String currentArea;

    private LocalMapsSubscriber subscriber;

    public MapSelectionPresenterImp(GetRemoteAreasUseCase remoteUseCase,
                                    GetLocalAreasUseCase localAreasUseCase) {

        this.remoteUseCase = remoteUseCase;
        this.localUseCase = localAreasUseCase;
        subscriber = new LocalMapsSubscriber();
    }

    public interface MySpinnerListener {
        void onSelect(String selectedArea, String selectedFile);
    }

    @Override
    public void setView(T view) {
        this.view = view;
    }

    @Override
    public void resume(IFileDataManager dataManager) {
        mapsFolder = dataManager.getMapsFolder();

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


    private void onFetchedAreaList(List<String> nameList, int areaType) {
        if (nameList == null || nameList.isEmpty()) {
            //logUser("No maps created for your version!? " + fileListURL);
            return;
        }

        MySpinnerListener spinnerListener = buildSpinnerListener();
        Map<String, String> nameToFullName = getNameToFullNameMap(nameList);

        if (areaType == MapsDataManagerImp.LOCAL_AREA) {
            view.initUIcomponents(nameList, nameToFullName, MapsDataManagerImp.LOCAL_AREA, spinnerListener);
        } else if (areaType == MapsDataManagerImp.REMOTE_AREA) {
            view.initUIcomponents(nameList, nameToFullName, MapsDataManagerImp.REMOTE_AREA, spinnerListener);
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

       /* final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Downloading and uncompressing " + downloadURL);
        dialog.setIndeterminate(false);
        dialog.setMax(100);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.show();

        new GHAsyncTask<Void, Integer, Object>() {
            protected Object saveDoInBackground( Void... _ignore )
                    throws Exception {
                String localFolder = Helper.pruneFileEnd(AndroidHelper.getFileName(downloadURL));
                localFolder = new File(mapsFolder, localFolder + "-gh").getAbsolutePath();
                //log("downloading & unzipping " + downloadURL + " to " + localFolder);
                Downloader downloader = new Downloader("AndroidGraphhopper");
                downloader.setTimeout(30000);
                downloader.downloadAndUnzip(downloadURL, localFolder,
                        new ProgressListener() {
                            @Override
                            public void update( long val ) {
                                publishProgress((int) val);
                            }
                        });
                return null;
            }

            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                dialog.setProgress(values[0]);
            }

            protected void onPostExecute( Object _ignore ) {
                dialog.dismiss();
                if (hasError()) {
                    String str = "An error happened while retrieving maps:" + getErrorMessage();
                    //log(str, getError());
                    //logUser(str);
                } else {
                    //loadMap(areaFolder);
                }
            }
        }.execute();*/
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

}

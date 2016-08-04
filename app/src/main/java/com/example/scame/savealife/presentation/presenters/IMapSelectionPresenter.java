package com.example.scame.savealife.presentation.presenters;


import java.util.List;
import java.util.Map;

public interface IMapSelectionPresenter<T> extends Presenter<T> {

    interface MapSelectionView {

        void initUIcomponents(List<String> nameList, Map<String, String> nameToFullName,
                              int areaType, MapSelectionPresenterImp.MySpinnerListener listener);

    }

    void initFiles(String area);

}

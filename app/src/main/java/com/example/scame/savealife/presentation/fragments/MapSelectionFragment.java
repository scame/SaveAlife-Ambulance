package com.example.scame.savealife.presentation.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.scame.savealife.R;
import com.example.scame.savealife.data.repository.MapsDataManagerImp;
import com.example.scame.savealife.presentation.di.components.MapSelectionComponent;
import com.example.scame.savealife.presentation.presenters.IMapSelectionPresenter;
import com.example.scame.savealife.presentation.presenters.MapSelectionPresenterImp;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapSelectionFragment extends BaseFragment implements IMapSelectionPresenter.MapSelectionView {

    @BindView(R.id.local_spinner) Spinner localSpinner;
    @BindView(R.id.remote_spinner) Spinner remoteSpinner;

    @BindView(R.id.ok_btn) Button localButton;
    @BindView(R.id.download_btn) Button remoteButton;

    @Inject IMapSelectionPresenter<IMapSelectionPresenter.MapSelectionView> presenter;

    private MapSelectionListener listener;

    private ProgressDialog dialog;

    public interface MapSelectionListener {
        void loadMap(String area);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MapSelectionListener) {
            listener = (MapSelectionListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.map_selection_fragment, container, false);
        ButterKnife.bind(this, fragmentView);

        getComponent(MapSelectionComponent.class).inject(this);
        presenter.setView(this);
        presenter.resume();

        return fragmentView;
    }

    @Override
    public void startDownloading(String downloadURL) {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Downloading and uncompressing " + downloadURL);
        dialog.setIndeterminate(true);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
    }

    @Override
    public void hideDownloading() {
        dialog.hide();
    }

    @Override
    public void initUIcomponents(List<String> nameList, Map<String,
            String> nameToFullName, int areaType, MapSelectionPresenterImp.MySpinnerListener listener) {

        if (areaType == MapsDataManagerImp.LOCAL_AREA) {
            initUIcomponents(localButton, localSpinner, nameList, nameToFullName, listener);
        } else if (areaType == MapsDataManagerImp.REMOTE_AREA) {
            initUIcomponents(remoteButton, remoteSpinner, nameList, nameToFullName, listener);
        }
    }

    private void initUIcomponents(Button button, Spinner spinner,
                                  List<String> nameList, Map<String, String> nameToFullName,
                                  MapSelectionPresenterImp.MySpinnerListener myListener) {

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, nameList);
        spinner.setAdapter(spinnerArrayAdapter);

        button.setOnClickListener(v -> {
            Object item = spinner.getSelectedItem();
            if (item != null && item.toString().length() > 0 && !nameToFullName.isEmpty()) {
                String area = item.toString();
                myListener.onSelect(area, nameToFullName.get(area));
            } else {
                myListener.onSelect(null, null);
            }
        });
    }

    @Override
    public void loadMap(String area) {
        listener.loadMap(area);
    }
}

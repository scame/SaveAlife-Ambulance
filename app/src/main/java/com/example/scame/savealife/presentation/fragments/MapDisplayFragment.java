package com.example.scame.savealife.presentation.fragments;


import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.scame.savealife.R;
import com.example.scame.savealife.SaveAlifeApp;
import com.example.scame.savealife.presentation.di.components.MapViewComponent;
import com.example.scame.savealife.presentation.presenters.IMapViewPresenter;
import com.graphhopper.PathWrapper;
import com.graphhopper.util.PointList;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.Layers;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.overlay.Marker;
import org.mapsforge.map.layer.overlay.Polyline;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapDisplayFragment extends BaseFragment implements IMapViewPresenter.MapViewView {

    @Inject MapView mapView;

    @Inject TileCache tileCache;

    @Inject IMapViewPresenter<IMapViewPresenter.MapViewView> presenter;

    @BindView(R.id.map_container) FrameLayout mapContainer;

    private String area;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidGraphicFactory.createInstance(SaveAlifeApp.getApp(getContext()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.map_display_fragment, container, false);

        getComponent(MapViewComponent.class).inject(this);
        ButterKnife.bind(this, fragmentView);
        area = getArguments().getString(getString(R.string.AREA_KEY));
        presenter.setView(this);
        presenter.setFilePath(area);
        presenter.resume();
        loadMap(new File(area));

        return fragmentView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mapView.destroyAll();
        AndroidGraphicFactory.clearResourceMemoryCache();
    }

    void loadMap(File areaFolder) {
        MapDataStore mapDataStore = new MapFile(areaFolder);

        mapView.getLayerManager().getLayers().clear();

        TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, mapDataStore,
                mapView.getModel().mapViewPosition, false, true, false, AndroidGraphicFactory.INSTANCE) {
            @Override
            public boolean onLongPress(LatLong tapLatLong, Point layerXY, Point tapXY) {
                return presenter.onMapTap(tapLatLong);
            }
        };

        tileRendererLayer.setTextScale(1.5f);
        tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.OSMARENDER);
        mapView.getModel().mapViewPosition.setMapPosition(new MapPosition(mapDataStore.boundingBox().getCenterPoint(), (byte) 15));
        mapView.getLayerManager().getLayers().add(tileRendererLayer);

        mapContainer.addView(mapView);
        presenter.loadGraphStorage();
    }


    @Override
    public void addPolyline(PathWrapper pathWrapper) {
        Polyline polyline = createPolyline(pathWrapper);
        mapView.getLayerManager().getLayers().add(polyline);
    }

    @Override
    public void updateLayers(LatLong p, int resource) {
        Layers layers = mapView.getLayerManager().getLayers();
        Marker marker = createMarker(p, resource);

        if (marker != null) {
            layers.add(marker);
        }
    }

    @Override
    public void removeLayers() {
        Layers layers = mapView.getLayerManager().getLayers();

        while (layers.size() > 1) {
            layers.remove(1);
        }
    }

    private Marker createMarker(LatLong p, int resource) {
        Drawable drawable = getResources().getDrawable(resource);
        Bitmap bitmap = AndroidGraphicFactory.convertToBitmap(drawable);
        return new Marker(p, bitmap, 0, -bitmap.getHeight() / 2);
    }

    private Polyline createPolyline(PathWrapper response ) {
        Paint paintStroke = AndroidGraphicFactory.INSTANCE.createPaint();
        paintStroke.setStyle(Style.STROKE);
        paintStroke.setColor(Color.argb(128, 209, 24, 24));
        paintStroke.setDashPathEffect(new float[] { 25, 15 });
        paintStroke.setStrokeWidth(8);

        Polyline line = new Polyline(paintStroke, AndroidGraphicFactory.INSTANCE);
        List<LatLong> geoPoints = line.getLatLongs();
        PointList tmp = response.getPoints();

        for (int i = 0; i < response.getPoints().getSize(); i++) {
            geoPoints.add(new LatLong(tmp.getLatitude(i), tmp.getLongitude(i)));
        }

        return line;
    }
}

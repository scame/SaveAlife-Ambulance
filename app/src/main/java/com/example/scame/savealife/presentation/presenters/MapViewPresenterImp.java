package com.example.scame.savealife.presentation.presenters;

import android.graphics.Color;
import android.util.Log;

import com.example.scame.savealife.R;
import com.example.scame.savealife.SaveAlifeApp;
import com.example.scame.savealife.data.entities.LatLongPair;
import com.example.scame.savealife.domain.usecases.CalculatePathUseCase;
import com.example.scame.savealife.domain.usecases.DefaultSubscriber;
import com.example.scame.savealife.domain.usecases.LoadGraphStorageUseCase;
import com.graphhopper.GraphHopper;
import com.graphhopper.PathWrapper;
import com.graphhopper.util.PointList;

import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.layer.overlay.Polyline;

import java.util.List;

import javax.inject.Inject;

public class MapViewPresenterImp<T extends IMapViewPresenter.MapViewView>
                                        implements IMapViewPresenter<T> {

    private volatile boolean shortestPathRunning = false;

    private String filePath;

    private LatLong start;
    private LatLong end;

    private T view;

    private CalculatePathUseCase calculatePathUseCase;
    private LoadGraphStorageUseCase loadGraphStorageUseCase;

    @Inject GraphHopper hopper;

    public MapViewPresenterImp(CalculatePathUseCase calculatePathUseCase,
                               LoadGraphStorageUseCase loadGraphStorageUseCase) {

        this.calculatePathUseCase = calculatePathUseCase;
        this.loadGraphStorageUseCase = loadGraphStorageUseCase;
    }

    @Override
    public boolean onMapTap(LatLong tapLatLong) {

        if (shortestPathRunning || hopper == null) {
            return false;
        }

        if (start != null && end == null) {
            end = tapLatLong;
            shortestPathRunning = true;
            view.updateLayers(end, R.drawable.ic_launcher);

            calcPath(start.latitude, start.longitude, end.latitude, end.longitude);
        } else {
            start = tapLatLong;
            end = null;
            view.removeLayers();
            view.updateLayers(start, R.drawable.ic_launcher);
        }

        return true;
    }


    @Override
    public void loadGraphStorage() {
        loadGraphStorageUseCase.setPath(filePath);
        loadGraphStorageUseCase.execute(new LoadStorageSubscriber());
    }


    @Override
    public void setView(T view) {
        this.view = view;
    }

    @Override
    public void resume() {
        hopper = SaveAlifeApp.getAppComponent().provideGraphhopper();
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        hopper = null;
    }

    private Polyline createPolyline(PathWrapper response ) {
        Paint paintStroke = AndroidGraphicFactory.INSTANCE.createPaint();
        paintStroke.setStyle(Style.STROKE);
        paintStroke.setColor(Color.argb(128, 0, 0xCC, 0x33));
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


    public void calcPath(final double fromLat, final double fromLon,
                          final double toLat, final double toLon) {

        calculatePathUseCase.setPair(new LatLongPair(fromLat, fromLon, toLat, toLon));
        calculatePathUseCase.execute(new MapViewPathSubscriber());
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    private final class MapViewPathSubscriber extends DefaultSubscriber<PathWrapper> {

        @Override
        public void onNext(PathWrapper pathWrapper) {
            super.onNext(pathWrapper);

            shortestPathRunning = false;
            MapViewPresenterImp.this.view.addPolyline(createPolyline(pathWrapper));
        }
    }

    private final class LoadStorageSubscriber extends DefaultSubscriber<Void> {

        @Override
        public void onNext(Void aVoid) {
            super.onNext(aVoid);

            Log.i("inOnNext", "true");
        }

        @Override
        public void onCompleted() {
            super.onCompleted();


            Log.i("load", "found graph: " + hopper.getGraphHopperStorage().toString() + ", nodes " +
                    hopper.getGraphHopperStorage().getNodes());
        }
    }
}

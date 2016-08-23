package com.example.scame.savealife.presentation.models;


import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public class DirectionModel {

    private List<LatLng> points;

    private PolylineOptions polyline;

    public void setPoints(List<LatLng> points) {
        this.points = points;
    }

    public void setPolyline(PolylineOptions polyline) {
        this.polyline = polyline;
    }

    public List<LatLng> getPoints() {
        return points;
    }

    public PolylineOptions getPolyline() {
        return polyline;
    }
}

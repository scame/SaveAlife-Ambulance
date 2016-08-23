package com.example.scame.savealife.data.entities;

import com.google.gson.annotations.SerializedName;

public class RouteEntity {

    @SerializedName("overview_polyline")
    private PointsEntity overviewPolyline;

    public PointsEntity getOverviewPolyline() {
        return overviewPolyline;
    }

    public void setOverviewPolyline(PointsEntity overviewPolyline) {
        this.overviewPolyline = overviewPolyline;
    }
}

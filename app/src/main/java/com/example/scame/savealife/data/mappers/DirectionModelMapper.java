package com.example.scame.savealife.data.mappers;


import android.graphics.Color;

import com.example.scame.savealife.data.entities.DirectionEntity;
import com.example.scame.savealife.presentation.models.DirectionModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.List;

public class DirectionModelMapper {

    private static final int FIRST_ROUTE = 0;

    public DirectionModel convert(DirectionEntity directionEntity) {
        DirectionModel directionModel = new DirectionModel();

        String encodedPoints = directionEntity.getRoutes().get(FIRST_ROUTE).getOverviewPolyline().getPoints();
        List<LatLng> pointList = PolyUtil.decode(encodedPoints);
        PolylineOptions polylineOptions = getPolyline(pointList);


        directionModel.setPoints(pointList);
        directionModel.setPolyline(polylineOptions);

        return directionModel;
    }

    private PolylineOptions getPolyline(List<LatLng> pointList) {

        return new PolylineOptions()
                .color(Color.BLUE)
                .width((float) 7.0)
                .addAll(pointList);
    }
}

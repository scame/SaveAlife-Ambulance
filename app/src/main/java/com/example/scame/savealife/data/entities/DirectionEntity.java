package com.example.scame.savealife.data.entities;

import java.util.ArrayList;
import java.util.List;

public class DirectionEntity {

    private List<RouteEntity> routes;

    public DirectionEntity() {
        routes = new ArrayList<>();
    }

    public void setRoutes(List<RouteEntity> routes) {
        this.routes = routes;
    }

    public List<RouteEntity> getRoutes() {
        return routes;
    }
}

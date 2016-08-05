package com.example.scame.savealife.data.entities;


public class LatLongPair {

    private final double fromLat;
    private final double fromLon;
    private final double toLat;
    private final double toLon;

    public LatLongPair(double fromLat, double fromLon, double toLat, double toLon) {
        this.fromLat = fromLat;
        this.fromLon = fromLon;
        this.toLat = toLat;
        this.toLon = toLon;
    }

    public double getFromLat() {
        return fromLat;
    }

    public double getFromLon() {
        return fromLon;
    }

    public double getToLat() {
        return toLat;
    }

    public double getToLon() {
        return toLon;
    }
}

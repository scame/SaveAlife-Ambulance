package com.example.scame.savealife.data.entities;


public class LatLongPair {

    private double fromLat;
    private double fromLon;
    private double toLat;
    private double toLon;

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

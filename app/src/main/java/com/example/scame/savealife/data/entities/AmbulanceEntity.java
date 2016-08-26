package com.example.scame.savealife.data.entities;


public class AmbulanceEntity {

    private String role;

    private double currentLat;

    private double currentLon;

    public void setRole(String role) {
        this.role = role;
    }

    public void setCurrentLat(double currentLat) {
        this.currentLat = currentLat;
    }

    public void setCurrentLon(double currentLon) {
        this.currentLon = currentLon;
    }

    public String getRole() {
        return role;
    }

    public double getCurrentLat() {
        return currentLat;
    }

    public double getCurrentLon() {
        return currentLon;
    }
}

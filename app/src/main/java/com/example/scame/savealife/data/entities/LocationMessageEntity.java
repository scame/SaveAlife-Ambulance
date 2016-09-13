package com.example.scame.savealife.data.entities;


public class LocationMessageEntity {

    private String currentToken;

    private String role;

    private double currentLat;

    private double currentLon;

    public void setCurrentToken(String currentToken) {
        this.currentToken = currentToken;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setCurrentLat(double currentLat) {
        this.currentLat = currentLat;
    }

    public void setCurrentLon(double currentLon) {
        this.currentLon = currentLon;
    }

    public String getCurrentToken() {
        return currentToken;
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

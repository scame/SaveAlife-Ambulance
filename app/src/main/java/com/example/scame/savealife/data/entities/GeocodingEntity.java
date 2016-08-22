package com.example.scame.savealife.data.entities;

import java.util.ArrayList;
import java.util.List;

public class GeocodingEntity {

    private List<GeocodingResult> results;

    public GeocodingEntity() {
        results = new ArrayList<>();
    }

    public void setResults(List<GeocodingResult> results) {
        this.results = results;
    }

    public List<GeocodingResult> getResults() {
        return results;
    }
}

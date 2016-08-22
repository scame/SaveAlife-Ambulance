package com.example.scame.savealife.data.entities;


import com.google.gson.annotations.SerializedName;

public class GeocodingResult {

    @SerializedName("formatted_address")
    private String formattedAddress;

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }
}

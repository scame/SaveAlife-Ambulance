package com.example.scame.savealife.data.mappers;


import com.example.scame.savealife.data.entities.GeocodingEntity;
import com.example.scame.savealife.presentation.models.AddressModel;

public class AddressModelMapper {

    public AddressModel convert(GeocodingEntity geocodingEntity) {

        AddressModel addressModel = new AddressModel();
        String formattedAddress = geocodingEntity.getResults().get(0).getFormattedAddress();
        addressModel.setFormattedAddress(formattedAddress);

        return addressModel;
    }
}

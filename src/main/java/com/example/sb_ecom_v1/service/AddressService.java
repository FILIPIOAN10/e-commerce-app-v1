package com.example.sb_ecom_v1.service;

import com.example.sb_ecom_v1.model.User;
import com.example.sb_ecom_v1.payload.AddressDTO;

import java.util.List;

public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO, User user);

    List<AddressDTO> getAddresses();

    AddressDTO getAddressesById(Long addressId);
}

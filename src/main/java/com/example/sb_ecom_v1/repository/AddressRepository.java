package com.example.sb_ecom_v1.repository;

import com.example.sb_ecom_v1.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Long> {
}

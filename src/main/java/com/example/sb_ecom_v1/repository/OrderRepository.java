package com.example.sb_ecom_v1.repository;

import com.example.sb_ecom_v1.model.OrderItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderItem,Long> {
}

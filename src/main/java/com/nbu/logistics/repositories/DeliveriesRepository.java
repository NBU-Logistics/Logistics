package com.nbu.logistics.repositories;

import com.nbu.logistics.entities.Delivery;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveriesRepository extends JpaRepository<Delivery, Long> {
    
}
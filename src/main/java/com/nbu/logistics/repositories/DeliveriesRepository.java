package com.nbu.logistics.repositories;

import java.util.List;

import com.nbu.logistics.entities.*;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveriesRepository extends JpaRepository<Delivery, Long> {
    Delivery findByName(String name);

    List<Delivery> findByStatus(DeliveryStatus status);

    List<Delivery> findByStatusNot(DeliveryStatus status);

    boolean existsByName(String name);
}
package com.nbu.logistics.repositories;

import com.nbu.logistics.entities.Office;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OfficesRepository extends JpaRepository<Office, Long> {
    
}
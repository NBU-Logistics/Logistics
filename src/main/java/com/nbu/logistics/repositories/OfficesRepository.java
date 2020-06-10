package com.nbu.logistics.repositories;

import com.nbu.logistics.entities.Office;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfficesRepository extends JpaRepository<Office, Long> {

    public List<Office> findAllByOrderByName();

}
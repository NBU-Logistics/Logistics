package com.nbu.logistics.repositories;

import com.nbu.logistics.entities.Company;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    boolean existsByEmail(String email);
}
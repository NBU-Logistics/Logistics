package com.nbu.logistics.repositories;

import com.nbu.logistics.entities.OfficeEmployee;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OfficeEmployeesRepository extends JpaRepository<OfficeEmployee, Long> {
    OfficeEmployee findByUserId(long id);

    OfficeEmployee findByUserEmail(String email);
}
package com.nbu.logistics.repositories;

import com.nbu.logistics.entities.BaseEntity;
import com.nbu.logistics.entities.Office;
import com.nbu.logistics.entities.OfficeEmployee;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfficeEmployeesRepository extends JpaRepository<OfficeEmployee, Long> {

    OfficeEmployee findByUserId(long id);

}
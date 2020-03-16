package com.nbu.logistics.repositories;

import com.nbu.logistics.entities.UserRole;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<UserRole, Long> {
    boolean existsByName(String name);

    UserRole findByName(String name);
}
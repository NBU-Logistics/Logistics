package com.nbu.logistics.repositories;

import com.nbu.logistics.entities.*;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    User findByEmail(String email);

    boolean existsByRoles(UserRole role);
}
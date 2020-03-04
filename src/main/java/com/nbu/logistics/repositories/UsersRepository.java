package com.nbu.logistics.repositories;

import com.nbu.logistics.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<User, Long> {
    
}
package com.nbu.logistics.repositories;

import com.nbu.logistics.entities.Courier;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CouriersRepository extends JpaRepository<Courier, Long> {
    Courier findByUserId(long id);

    Courier findByUserEmail(String email);
}
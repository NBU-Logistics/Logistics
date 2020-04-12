package com.nbu.logistics.repositories;

import com.nbu.logistics.entities.Client;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientsRepository extends JpaRepository<Client, Long> {
    Client findByUserEmail(String email);
}
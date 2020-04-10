package com.nbu.logistics.repositories;

import com.nbu.logistics.entities.Client;

import com.nbu.logistics.entities.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientsRepository extends JpaRepository<Client, Long> {
    Client findByUserEmail(String email);
}
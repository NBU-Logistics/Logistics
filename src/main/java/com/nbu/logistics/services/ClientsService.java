package com.nbu.logistics.services;

import com.nbu.logistics.repositories.ClientsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientsService {
    @Autowired
    private ClientsRepository clientsRepository;
}
package com.nbu.logistics.services;

import com.nbu.logistics.entities.Client;
import com.nbu.logistics.exceptions.InvalidDataException;
import com.nbu.logistics.repositories.ClientsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private ClientsRepository clientsRepository;

    public void registerClient(Client client) throws InvalidDataException {
        if (this.clientsRepository.existsByEmail(client.getEmail())) {
            throw new InvalidDataException("Client already exists!");
        }

        this.clientsRepository.save(client);
    }
}
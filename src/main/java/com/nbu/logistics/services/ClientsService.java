package com.nbu.logistics.services;

import com.nbu.logistics.entities.Client;
import com.nbu.logistics.entities.User;
import com.nbu.logistics.repositories.ClientsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientsService {
    @Autowired
    private ClientsRepository clientsRepository;

    public void createClient(User user) {
        this.clientsRepository.save(new Client(user, null));
    }
}
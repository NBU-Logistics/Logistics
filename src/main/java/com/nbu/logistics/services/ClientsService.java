package com.nbu.logistics.services;

import com.nbu.logistics.entities.*;
import com.nbu.logistics.repositories.ClientsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This is the clients service.
 */
@Service
public class ClientsService {
    @Autowired
    private ClientsRepository clientsRepository;

    /**
     * Creates a client.
     * 
     * @param user the user entity.
     */
    public void createClient(User user) {
        this.clientsRepository.save(new Client(user, null, null));
    }

    /**
     * Gathers all clients in the database.
     * 
     * @return a list of all clients
     */
    public List<Client> getAllClients() {
        return this.clientsRepository.findAll();
    }
}
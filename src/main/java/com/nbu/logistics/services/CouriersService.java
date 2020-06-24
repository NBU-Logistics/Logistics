package com.nbu.logistics.services;

import com.nbu.logistics.entities.*;
import com.nbu.logistics.exceptions.InvalidDataException;
import com.nbu.logistics.repositories.CouriersRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The couries service.
 */
@Service
public class CouriersService {
    @Autowired
    private CouriersRepository couriersRepository;

    @Autowired
    private AuthService authService;

    /**
     * Creates a courier.
     * 
     * @param user the user entity
     */
    public void createCourier(User user) {
        this.couriersRepository.save(new Courier(user, null));
    }

    /**
     * Deletes a courier by given id.
     * 
     * @param id the courier's id
     * @throws InvalidDataException when the courier does not exist
     */
    public void deleteCourier(long id) throws InvalidDataException {
        Courier existingCourier = this.couriersRepository.findByUserId(id);
        if (existingCourier == null) {
            throw new InvalidDataException("Courier does not exist!");
        }

        existingCourier.setDeleted(true);
        this.couriersRepository.save(existingCourier);

        this.authService.deleteUser(existingCourier.getUser().getEmail());
    }

    /**
     * Returns all couriers in the database.
     * 
     * @return a list of all couriers
     */
    public List<Courier> getAllCouriers() {
        return this.couriersRepository.findAll();
    }
}
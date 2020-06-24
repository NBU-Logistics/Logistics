package com.nbu.logistics.services;

import com.nbu.logistics.entities.Office;
import com.nbu.logistics.exceptions.InvalidDataException;
import com.nbu.logistics.repositories.OfficesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * This is the offices service.
 */
@Service
public class OfficesService {
    @Autowired
    private OfficesRepository officesRepository;

    /**
     * Gets all offices in the database.
     * 
     * @return a list of all offices
     */
    public List<Office> getAllOffices() {
        return officesRepository.findAllByOrderByName();
    }

    /**
     * Gets an office by given id.
     * 
     * @param id the id
     * @return the office
     * @throws InvalidDataException when the office does not exist
     */
    public Office getOffice(long id) throws InvalidDataException {
        Optional<Office> office = officesRepository.findById(id);
        if (!office.isPresent()) {
            throw new InvalidDataException("Office does not exist!");
        }

        return office.get();
    }

    /**
     * Creates an office.
     * 
     * @param newOffice the office's entity
     */
    public void save(Office newOffice) {
        officesRepository.save(newOffice);
    }

    /**
     * Deletes an office by given id.
     * 
     * @param id the office's id
     * @throws InvalidDataException when the office does not exist
     */
    public void deleteOffice(long id) throws InvalidDataException {
        Optional<Office> office = this.officesRepository.findById(id);

        if (!office.isPresent()) {
            throw new InvalidDataException("Office does not exist!");
        }

        office.get().setDeleted(true);
        this.officesRepository.save(office.get());
    }

    /**
     * Changes an office's details.
     * 
     * @param changedOffice the modified office data
     * @throws InvalidDataException when an office has not been provided. Also
     *                              throws when the office does not exist.
     */
    public void modifyOffice(Office changedOffice) throws InvalidDataException {
        if (changedOffice == null) {
            throw new InvalidDataException("Missing office!");
        }

        Office office = this.getOffice(changedOffice.getId());
        if (office == null) {
            throw new InvalidDataException("Office does not exist!");
        }

        if (!changedOffice.getName().equals(office.getName())) {
            office.setName(changedOffice.getName());
        }

        if (!changedOffice.getAddress().equals(office.getAddress())) {
            office.setAddress(changedOffice.getAddress());
        }

        this.officesRepository.save(office);
    }
}
package com.nbu.logistics.services;

import com.nbu.logistics.entities.Office;
import com.nbu.logistics.exceptions.InvalidDataException;
import com.nbu.logistics.repositories.OfficesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OfficesService {
    @Autowired
    private OfficesRepository officesRepository;

    public List<Office> getAllOffices() {
        return officesRepository.findAllByOrderByName();
    }

    public Office getOffice(long id) throws InvalidDataException {
        Optional<Office> office = officesRepository.findById(id);
        if (!office.isPresent()) {
            throw new InvalidDataException("Office does not exist!");
        }

        return office.get();
    }

    public void save(Office newOffice) {
        officesRepository.save(newOffice);
    }

    public void deleteOffice(long id) throws InvalidDataException {
        Optional<Office> office = this.officesRepository.findById(id);

        if (!office.isPresent()) {
            throw new InvalidDataException("Office does not exist!");
        }

        office.get().setDeleted(true);
        this.officesRepository.save(office.get());
    }

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
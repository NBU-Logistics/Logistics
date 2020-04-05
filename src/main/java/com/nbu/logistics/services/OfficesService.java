package com.nbu.logistics.services;

import com.nbu.logistics.entities.Office;
import com.nbu.logistics.exceptions.InvalidDataException;
import com.nbu.logistics.repositories.OfficesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OfficesService {
    @Autowired
    private OfficesRepository officesRepository;

    public List<Office> getAllOffices() {
        // return officesRepository.findAll();
        return officesRepository.findAllByOrderByName();
    }

    public Office getOffice(long id) {
        Office office = officesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid office Id:" + id));
        return office;
    }

    public Office getByIdOffice(long id) {
        Optional<Office> result = officesRepository.findById(id);

        Office theOffice = null;

        if (result.isPresent()) {
            // office.getName();
            theOffice = result.get();
        } else {
            throw new RuntimeException("Didn't find any office id - " + id);
        }
        return theOffice;
    }

    public void save(Office newOffice) {
        officesRepository.save(newOffice);
    }

    public void deleteOffice(long id) {
        Optional<Office> office = this.officesRepository.findById(id);

        if (office.isPresent()) {
            office.get().setDeleted(true);
            this.officesRepository.save(office.get());
        }
    }

    public void modifyOffice(Office changedOffice) throws InvalidDataException {
        if (changedOffice == null) {
            throw new InvalidDataException("Missing office!");
        }

        Office office = this.officesRepository.getOne(changedOffice.getId());

        if (office == null) {
            throw new InvalidDataException("Office does not exist!");
        }

        if (changedOffice.getName().length() == 0) {
            throw new InvalidDataException("Office name can not be empty!");
        }

        office.setName(changedOffice.getName());
        this.officesRepository.save(office);
    }
}
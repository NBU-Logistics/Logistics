package com.nbu.logistics.services;

import com.nbu.logistics.entities.Office;
import com.nbu.logistics.repositories.OfficesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OfficesService {
    @Autowired
    private OfficesRepository officesRepository;

    //methods

//    private List<Office> offices = new ArrayList<>();

    public List<Office> getAllOffices() {
        List<Office> offices = new ArrayList<>();
        officesRepository.findAll().forEach(offices::add);
        return offices;
    }

    public Office getOffice(long id) {
        Office office = officesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Office id: " + id));
        return office;
    }

    public void addOffice(Office id) {
        officesRepository.save(id);
    }

    public void deleteOffice(long id) {
        Office office = officesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid office Id:" + id));
        officesRepository.delete(office);
    }


}
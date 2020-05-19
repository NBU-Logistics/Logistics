package com.nbu.logistics.services;

import com.nbu.logistics.entities.Client;
import com.nbu.logistics.entities.OfficeEmployee;
import com.nbu.logistics.entities.User;
import com.nbu.logistics.repositories.OfficeEmployeesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfficeEmployeesService {
    @Autowired
    private OfficeEmployeesRepository officeEmployeesRepository;

    public void createOfficeEmployee(User user) {
        this.officeEmployeesRepository.save(new OfficeEmployee(user, null));
    }

    public List<OfficeEmployee> getAllOfficeEmployees(){
        return this.officeEmployeesRepository.findAll();
    }
}
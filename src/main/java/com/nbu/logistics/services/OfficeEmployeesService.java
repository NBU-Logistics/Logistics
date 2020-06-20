package com.nbu.logistics.services;

import com.nbu.logistics.entities.OfficeEmployee;
import com.nbu.logistics.entities.User;
import com.nbu.logistics.repositories.OfficeEmployeesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nbu.logistics.exceptions.InvalidDataException;

import java.util.List;

@Service
public class OfficeEmployeesService {
    @Autowired
    private OfficeEmployeesRepository officeEmployeesRepository;
    @Autowired
    private AuthService authService;

    public void createOfficeEmployee(User user) {
        this.officeEmployeesRepository.save(new OfficeEmployee(user, null));
    }

    public void deleteEmployee(long id) throws InvalidDataException {
        if (id == 0) {
            throw new InvalidDataException("Invalid data!");
        }

        OfficeEmployee existingEmployee = this.officeEmployeesRepository.findByUserId(id);
        if (existingEmployee == null) {
            throw new InvalidDataException("Employee does not exist!");
        }

        existingEmployee.setDeleted(true);
        this.officeEmployeesRepository.save(existingEmployee);

        this.authService.deleteUser(existingEmployee.getUser().getEmail());
    }

    public List<OfficeEmployee> getAllOfficeEmployees() {
        return this.officeEmployeesRepository.findAll();
    }
}

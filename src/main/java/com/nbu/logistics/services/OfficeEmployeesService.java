package com.nbu.logistics.services;

import com.nbu.logistics.entities.*;
import com.nbu.logistics.repositories.OfficeEmployeesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nbu.logistics.exceptions.InvalidDataException;

import java.util.List;

/**
 * This is the office employees service.
 */
@Service
public class OfficeEmployeesService {
    @Autowired
    private OfficeEmployeesRepository officeEmployeesRepository;
    @Autowired
    private AuthService authService;

    /**
     * Creates an office employee.
     * 
     * @param user the office employee's user entity
     */
    public void createOfficeEmployee(User user) {
        this.officeEmployeesRepository.save(new OfficeEmployee(user, null));
    }

    /**
     * Deletes an employee by given id.
     * 
     * @param id the id
     * @throws InvalidDataException when the employee does not exist
     */
    public void deleteEmployee(long id) throws InvalidDataException {
        OfficeEmployee existingEmployee = this.officeEmployeesRepository.findByUserId(id);
        if (existingEmployee == null) {
            throw new InvalidDataException("Employee does not exist!");
        }

        existingEmployee.setDeleted(true);
        this.officeEmployeesRepository.save(existingEmployee);

        this.authService.deleteUser(existingEmployee.getUser().getEmail());
    }

    /**
     * Gets all office employees.
     * 
     * @return a list of all office employees
     */
    public List<OfficeEmployee> getAllOfficeEmployees() {
        return this.officeEmployeesRepository.findAll();
    }
}

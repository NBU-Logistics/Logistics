package com.nbu.logistics.services;

import com.nbu.logistics.entities.Courier;
import com.nbu.logistics.entities.User;
import com.nbu.logistics.exceptions.InvalidDataException;
import com.nbu.logistics.repositories.CouriersRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouriersService {
    @Autowired
    private CouriersRepository couriersRepository;

    @Autowired
    private AuthService authService;

    public void createCourier(User user) {
        this.couriersRepository.save(new Courier(user, null));
    }

    public void deleteCourier(long id) throws InvalidDataException {
        // OfficeEmployee existingEmployee =
        // this.officeEmployeesRepository.findByUserById(officeEmployeesRepository.getOne(id));
        if (id == 0) {
            throw new InvalidDataException("Invalid data!");
        }

        Courier existingCourier = this.couriersRepository.findByUserId(id);
        if (existingCourier == null) {
            throw new InvalidDataException("Employee does not exist!");
        }

        existingCourier.setDeleted(true);
        this.couriersRepository.save(existingCourier);

        this.authService.deleteUser(existingCourier.getUser().getEmail());
    }

    public List<Courier> getAllCouriers() {
        return this.couriersRepository.findAll();
    }
}
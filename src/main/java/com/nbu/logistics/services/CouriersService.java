package com.nbu.logistics.services;

import com.nbu.logistics.entities.Courier;
import com.nbu.logistics.entities.User;
import com.nbu.logistics.repositories.CouriersRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CouriersService {
    @Autowired
    private CouriersRepository couriersRepository;

    public void createCourier(User user) {
        this.couriersRepository.save(new Courier(user, null));
    }
}
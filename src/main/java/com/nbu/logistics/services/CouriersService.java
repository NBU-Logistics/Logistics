package com.nbu.logistics.services;

import com.nbu.logistics.repositories.CouriersRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CouriersService {
    @Autowired
    private CouriersRepository couriersRepository;
}
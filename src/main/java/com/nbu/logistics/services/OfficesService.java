package com.nbu.logistics.services;

import com.nbu.logistics.repositories.OfficesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OfficesService {
    @Autowired
    private OfficesRepository officesRepository;
}
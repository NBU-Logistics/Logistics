package com.nbu.logistics.services;

import com.nbu.logistics.repositories.DeliveriesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveriesService {
    @Autowired
    private DeliveriesRepository deliveriesRepository;
}
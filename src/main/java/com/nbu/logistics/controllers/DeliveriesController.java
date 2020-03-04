package com.nbu.logistics.controllers;

import com.nbu.logistics.services.DeliveriesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DeliveriesController {
    @Autowired
    private DeliveriesService deliveriesService;

    @RequestMapping("/deliveries")
    public String showDeliveries() {
        return "deliveries";
    }
}
package com.nbu.logistics.controllers;

import com.nbu.logistics.services.OfficesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OfficesController {
    @Autowired
    private OfficesService officesService;

    @RequestMapping("/offices")
    public String showOffices() {
        return "offices";
    }
}
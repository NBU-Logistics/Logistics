package com.nbu.logistics.controllers;

import com.nbu.logistics.services.CouriersService;
import com.nbu.logistics.services.OfficeEmployeesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class EmployeesController {
    @Autowired
    private CouriersService couriersService;

    @Autowired
    private OfficeEmployeesService officeEmployeesService;

    @RequestMapping("/employees")
    public String showEmployees() {
        return "employees";
    }
}
package com.nbu.logistics.controllers;

import com.nbu.logistics.entities.OfficeEmployee;
import com.nbu.logistics.services.CouriersService;
import com.nbu.logistics.services.OfficeEmployeesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class EmployeesController {
    @Autowired
    private CouriersService couriersService;

    @Autowired
    private OfficeEmployeesService officeEmployeesService;

    private void getEmployeesPage(Model model) {
        model.addAttribute("allEmployees", officeEmployeesService.getAllOfficeEmployees());
        model.addAttribute("allCouriers", couriersService.getAllCouriers());
    }

    @RequestMapping("/employees")
    public String showAllEmployees(Model model, OfficeEmployee employee) {
        this.getEmployeesPage(model);

        return "employees";
    }
}
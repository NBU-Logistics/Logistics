package com.nbu.logistics.controllers;

import javax.validation.Valid;

import com.nbu.logistics.entities.User;
import com.nbu.logistics.services.CouriersService;
import com.nbu.logistics.services.OfficeEmployeesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/employees")
public class EmployeesController {
    @Autowired
    private CouriersService couriersService;

    @Autowired
    private OfficeEmployeesService officeEmployeesService;

    @Autowired
    private AuthController authController;

    @GetMapping("/")
    public String showEmployees() {
        return "employees";
    }

    @GetMapping("/couriers/register")
    public String showCreateCourierEmployee(User user) {
        return "create-courier-employee";
    }

    @GetMapping("/office/register")
    public String showCreateOfficeEmployee(User user) {
        return "create-office-employee";
    }

    @PostMapping("/couriers/register")
    public String createCourier(Model model, @Valid User user, BindingResult bindingResult) {
        return this.authController.registerUser(model, user, bindingResult, "create-courier-employee",
                "Successfully created a courier employee!", "COURIER", () -> {
                    this.couriersService.createCourier(user);
                });
    }

    @PostMapping("/office/register")
    public String createOfficeEmployee(Model model, @Valid User user, BindingResult bindingResult) {
        return this.authController.registerUser(model, user, bindingResult, "create-office-employee",
                "Successfully created an office employee!", "OFFICE_EMPLOYEE", () -> {
                    this.officeEmployeesService.createOfficeEmployee(user);
                });
    }
}
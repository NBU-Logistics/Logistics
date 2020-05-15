package com.nbu.logistics.controllers;

import com.nbu.logistics.exceptions.InvalidDataException;
import com.nbu.logistics.services.AuthService;
import com.nbu.logistics.services.CouriersService;
import com.nbu.logistics.services.OfficeEmployeesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EmployeesController {
    @Autowired
    private CouriersService couriersService;

    @Autowired
    private AuthService authService;

    @Autowired
    private OfficeEmployeesService officeEmployeesService;

    @GetMapping("/employees")
    public String showEmployees() {
        return "employees";
    }

    @PostMapping("/employees/delete")
    public String delete(@RequestParam("employeeId") long employeeId, Model model) {
        try {
            officeEmployeesService.deleteEmployee(employeeId);
        } catch (InvalidDataException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/employees";

        }
        model.addAttribute("success", "Successfully deleted employee!");

        return "employees";
    }
}


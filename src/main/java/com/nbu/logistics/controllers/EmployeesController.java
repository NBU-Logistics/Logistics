package com.nbu.logistics.controllers;

import com.nbu.logistics.entities.OfficeEmployee;
import com.nbu.logistics.exceptions.InvalidDataException;
import com.nbu.logistics.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class EmployeesController {
    @Autowired
    private CouriersService couriersService;

    @Autowired
    private OfficeEmployeesService officeEmployeesService;

    private String getEmployeesPage(Model model) {
        model.addAttribute("allEmployees", officeEmployeesService.getAllOfficeEmployees());
        model.addAttribute("allCouriers", couriersService.getAllCouriers());

        return "employees";
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @RequestMapping("/employees")
    public String showAllEmployees(Model model, OfficeEmployee employee) {
        return this.getEmployeesPage(model);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PostMapping("/employees/delete-employee")
    public String deleteEmployee(@RequestParam("id") long employeeId, Model model) {
        try {
            officeEmployeesService.deleteEmployee(employeeId);
        } catch (InvalidDataException e) {
            model.addAttribute("error", e.getMessage());

            return this.getEmployeesPage(model);
        }

        model.addAttribute("success", "Successfully deleted employee!");

        return this.getEmployeesPage(model);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PostMapping("/employees/delete-courier")
    public String deleteCourier(@RequestParam("id") long employeeId, Model model) {
        try {
            couriersService.deleteCourier(employeeId);
        } catch (InvalidDataException e) {
            model.addAttribute("error", e.getMessage());

            return this.getEmployeesPage(model);
        }

        model.addAttribute("success", "Successfully deleted employee!");

        return this.getEmployeesPage(model);
    }
}

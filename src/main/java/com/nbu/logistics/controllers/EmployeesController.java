package com.nbu.logistics.controllers;

import com.nbu.logistics.entities.OfficeEmployee;
import com.nbu.logistics.exceptions.InvalidDataException;
import com.nbu.logistics.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * This is the employees controller
 */
@Controller
@RequestMapping("/employees")
public class EmployeesController {
    @Autowired
    private CouriersService couriersService;

    @Autowired
    private OfficeEmployeesService officeEmployeesService;

    /**
     * It returns the employees page.
     * 
     * @param model the controller model
     * @return the employees page
     */
    private String getEmployeesPage(Model model) {
        model.addAttribute("allEmployees", officeEmployeesService.getAllOfficeEmployees());
        model.addAttribute("allCouriers", couriersService.getAllCouriers());

        return "employees";
    }

    /**
     * /employees get request handler
     * 
     * @param model    the controller model
     * @param employee the employee model
     * @return the employees page
     */
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @GetMapping()
    public String showAllEmployees(Model model, OfficeEmployee employee) {
        return this.getEmployeesPage(model);
    }

    /**
     * /employees/delete-employee post request handler
     * 
     * @param employeeId the employee id
     * @param model      the controller model
     * @return the employees page
     */
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PostMapping("/delete-employee")
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

    /**
     * /employees/delete-courier post request handler
     * 
     * @param employeeId the employee id
     * @param model      the controller model
     * @return the employees page
     */
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PostMapping("/delete-courier")
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

package com.nbu.logistics.controllers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.validation.Valid;

import com.nbu.logistics.entities.Company;
import com.nbu.logistics.exceptions.InvalidDataException;
import com.nbu.logistics.services.CompanyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * The company controller.
 */
@Controller
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    /**
     * /company get request handler
     * 
     * @param model the controller model
     * @return the comapny page
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping()
    public String showCompany(Model model) {
        try {
            model.addAttribute("company", this.companyService.getCompany());
        } catch (InvalidDataException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "company";
    }

    /**
     * /company/create get request handler
     * 
     * @param company the comapny model
     * @return the create-company page
     */
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @GetMapping("/create")
    public String getCreateCompany(Company company) {
        return "create-company";
    }

    /**
     * /company/create post request handler
     * 
     * @param model         the controller model
     * @param company       the comapny model
     * @param bindingResult the controller BindingResult
     * @return the create-company page
     */
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public String createCompany(Model model, @Valid Company company, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "create-company";
        }

        try {
            this.companyService.createCompany(company);
        } catch (InvalidDataException e) {
            model.addAttribute("error", e.getMessage());

            return "create-company";
        }

        model.addAttribute("success", "Successfully created!");

        return "create-company";
    }

    /**
     * /company/update get request handler
     * 
     * @param model the controller model
     * @return the update-company page
     */
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @GetMapping("/update")
    public String getUpdateCompany(Model model) {
        Company company = new Company();

        try {
            company = this.companyService.getCompany();
        } catch (InvalidDataException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("company", company);

            return "update-company";
        }

        model.addAttribute("company", company);

        return "update-company";
    }

    /**
     * /company/update post request handler
     * 
     * @param model         the controller model
     * @param company       the comany model
     * @param bindingResult the controller BindingResult
     * @return the update-company page
     */
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PostMapping("/update")
    public String updateCompany(Model model, @Valid Company company, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "update-company";
        }

        try {
            this.companyService.updateCompany(company);
        } catch (InvalidDataException e) {
            model.addAttribute("error", e.getMessage());

            return "update-company";
        }

        model.addAttribute("success", "Successfully updated!");

        return "update-company";
    }

    /**
     * /company/delete post request handler
     * 
     * @param model the controller model
     * @return the company page
     */
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PostMapping("/delete")
    public String deleteCompany(Model model) {
        try {
            this.companyService.deleteCompany();
        } catch (InvalidDataException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "company";
    }

    /**
     * /company/income get request handler
     * 
     * @param model the controller model
     * @return the income page
     */
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @GetMapping("/income")
    public String getShowCompanyIncome(Model model) {
        return "income";
    }

    /**
     * /company/income post request handler
     * 
     * @param model    the controller model
     * @param fromDate the date from which to create the report
     * @param toDate   the date to which to create the report
     * @return the income page
     */
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PostMapping("/income")
    public String getCompanyIncome(Model model, @ModelAttribute("fromDate") String fromDate,
            @ModelAttribute("toDate") String toDate) {
        try {
            LocalDate fromDateConverted = LocalDate.parse(fromDate);
            LocalDate toDateConverted = LocalDate.parse(toDate);

            int dateComparison = fromDateConverted.compareTo(toDateConverted);
            if (dateComparison == 0 || dateComparison > 0) {
                model.addAttribute("error", "Invalid date!");

                return "income";
            }

            BigDecimal income = this.companyService.calculateIncome(fromDateConverted, toDateConverted);
            model.addAttribute("income", income);
        } catch (DateTimeParseException e) {
            model.addAttribute("error", "Invalid date!");
        }

        return "income";
    }
}
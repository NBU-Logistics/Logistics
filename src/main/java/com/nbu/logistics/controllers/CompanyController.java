package com.nbu.logistics.controllers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.validation.Valid;

import com.nbu.logistics.entities.Company;
import com.nbu.logistics.exceptions.InvalidDataException;
import com.nbu.logistics.services.CompanyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @GetMapping()
    public String showCompany(Model model) {
        try {
            model.addAttribute("company", this.companyService.getCompany());
        } catch (InvalidDataException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "company";
    }

    @GetMapping("/create")
    public String getCreateCompany(Company company) {
        return "create-company";
    }

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

    @GetMapping("/update")
    public String getUpdateCompany(Company company) {
        return "update-company";
    }

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

    @PostMapping("/delete")
    public String deleteCompany(Model model) {
        try {
            this.companyService.deleteCompany();
        } catch (InvalidDataException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "company";
    }

    @GetMapping("/income")
    public String getShowCompanyIncome(Model model) {
        return "income";
    }

    @PostMapping("/income")
    public String getCompanyIncome(Model model, @ModelAttribute("fromDate") String fromDate,
            @ModelAttribute("toDate") String toDate) {
        try {
            LocalDate fromDateConverted = LocalDate.parse(fromDate);
            LocalDate toDateConverted = LocalDate.parse(toDate);

            BigDecimal income = this.companyService.calculateIncome(fromDateConverted, toDateConverted);
            model.addAttribute("income", income);
        } catch (DateTimeParseException e) {
            model.addAttribute("error", "Invalid date!");
        }

        return "income";
    }
}
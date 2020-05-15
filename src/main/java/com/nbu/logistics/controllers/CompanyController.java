package com.nbu.logistics.controllers;

import javax.validation.Valid;

import com.nbu.logistics.entities.Company;
import com.nbu.logistics.exceptions.InvalidDataException;
import com.nbu.logistics.services.CompanyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @RequestMapping("/company")
    public String showCompany(Model model) {
        try {
            model.addAttribute("company", this.companyService.getCompany());
        } catch (InvalidDataException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "company";
    }

    @RequestMapping("/company/create")
    public String getCreateCompany(Company company) {
        return "create-company";
    }

    @PostMapping("/company/create")
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

    @RequestMapping("/company/update")
    public String getUpdateCompany(Company company) {
        return "update-company";
    }

    @PostMapping("/company/update")
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

    @PostMapping("/company/delete")
    public String deleteCompany(Model model) {
        try {
            this.companyService.deleteCompany();
        } catch (InvalidDataException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "company";
    }
}
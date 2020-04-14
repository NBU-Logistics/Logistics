package com.nbu.logistics.controllers;

import com.nbu.logistics.entities.Office;
import com.nbu.logistics.exceptions.InvalidDataException;
import com.nbu.logistics.services.OfficesService;

import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class OfficesController {
    @Autowired
    private OfficesService officesService;

    @GetMapping("/offices")
    public String showOffices(Office office, Model model) {
        List<Office> theOffices = officesService.getAllOffices();
        model.addAttribute("offices", theOffices);

        return "offices";
    }

    @GetMapping("/offices/create")
    public String getCreateOffice(Model model) {
        Office office = new Office();
        model.addAttribute("office", office);

        return "create-office";
    }

    @PostMapping("/offices/create")
    public String saveOffice(Office office, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "offices";
        }

        officesService.save(office);

        return "redirect:/offices";
    }

    @PostMapping("/offices/update")
    public String update(Model model, Office office) {
        try {
            this.officesService.modifyOffice(office);
        } catch (InvalidDataException e) {
            model.addAttribute("error", e.getMessage());

            return "create-office";
        }

        model.addAttribute("success", "Office edited successfully!");
        model.addAttribute("offices", this.officesService.getAllOffices());

        return "offices";
    }

    @PostMapping("/offices/delete")
    public String delete(@RequestParam("officeId") String officeId) {
        officesService.deleteOffice(Long.parseLong(officeId));

        return "redirect:/offices";
    }
}
package com.nbu.logistics.controllers;

import com.nbu.logistics.entities.Office;
import com.nbu.logistics.exceptions.InvalidDataException;
import com.nbu.logistics.services.OfficesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.validation.Valid;

@Controller
@RequestMapping("/offices")
public class OfficesController {
    @Autowired
    private OfficesService officesService;

    @GetMapping()
    public String showOffices(Model model, Office office) {
        List<Office> theOffices = officesService.getAllOffices();
        model.addAttribute("offices", theOffices);

        return "offices";
    }

    @GetMapping("/create")
    public String getCreateOffice(Model model, Office office) {
        return "create-office";
    }

    @PostMapping("/create")
    public String saveOffice(Model model, @Valid @ModelAttribute("office") Office office, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "create-office";
        }

        officesService.save(office);
        model.addAttribute("success", "Office created!");

        return "create-office";
    }

    @GetMapping("/{id}")
    public String getUpdateOffice(Model model, @PathVariable("id") long id) {
        try {
            model.addAttribute("office", this.officesService.getOffice(id));
        } catch (InvalidDataException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "edit-office";
    }

    @PostMapping("/update")
    public String update(Model model, @Valid @ModelAttribute("office") Office office, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edit-office";
        }

        try {
            this.officesService.modifyOffice(office);
        } catch (InvalidDataException e) {
            model.addAttribute("error", e.getMessage());

            return "edit-office";
        }

        model.addAttribute("success", "Office eddited successfully!");

        return "edit-office";
    }

    @PostMapping("/delete")
    public String delete(Model model, @RequestParam("id") long id) {
        try {
            officesService.deleteOffice(id);
        } catch (InvalidDataException e) {
            model.addAttribute("error", e.getMessage());
        }

        return this.showOffices(model, new Office());
    }
}
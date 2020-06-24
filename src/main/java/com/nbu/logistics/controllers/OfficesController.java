package com.nbu.logistics.controllers;

import com.nbu.logistics.entities.Office;
import com.nbu.logistics.exceptions.InvalidDataException;
import com.nbu.logistics.services.OfficesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.validation.Valid;

/**
 * The offices controller.
 */
@Controller
@RequestMapping("/offices")
public class OfficesController {
    @Autowired
    private OfficesService officesService;

    /**
     * /offices get request handler
     * 
     * @param model  the controller model
     * @param office the office model
     * @return the offices page
     */
    @GetMapping()
    public String showOffices(Model model, Office office) {
        List<Office> theOffices = officesService.getAllOffices();
        model.addAttribute("offices", theOffices);

        return "offices";
    }

    /**
     * /offices/create get request handler
     * 
     * @param model  the controller model
     * @param office the office model
     * @return the create-office page
     */
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @GetMapping("/create")
    public String getCreateOffice(Model model, Office office) {
        return "create-office";
    }

    /**
     * /offices/create post request handler
     * 
     * @param model         the controller model
     * @param office        the office model
     * @param bindingResult the controller BindingResult
     * @return the create-office page
     */
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public String saveOffice(Model model, @Valid @ModelAttribute("office") Office office, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "create-office";
        }

        officesService.save(office);
        model.addAttribute("success", "Office created!");

        return "create-office";
    }

    /**
     * /offices/{id} get request handler
     * 
     * @param model the controller model
     * @param id    the office id
     * @return the edit-office page
     */
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public String getUpdateOffice(Model model, @PathVariable("id") long id) {
        try {
            model.addAttribute("office", this.officesService.getOffice(id));
        } catch (InvalidDataException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "edit-office";
    }

    /**
     * /offices/update post request handler
     * 
     * @param model         the controller model
     * @param office        the office model
     * @param bindingResult the controller BindingResult
     * @return the edit-office page
     */
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
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

        model.addAttribute("success", "Office edited successfully!");

        return "edit-office";
    }

    /**
     * /offices/delete post request handler
     * 
     * @param model the controller model
     * @param id    the office id
     * @return the offices page
     */
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PostMapping("/delete")
    public String delete(Model model, @RequestParam("id") long id) {
        try {
            officesService.deleteOffice(id);
        } catch (InvalidDataException e) {
            model.addAttribute("error", e.getMessage());
        }

        model.addAttribute("success", "Successfully deleted office!");

        return this.showOffices(model, new Office());
    }
}
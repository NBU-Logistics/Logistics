package com.nbu.logistics.controllers;

import com.nbu.logistics.entities.Office;
import com.nbu.logistics.services.OfficesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class OfficesController {
    @Autowired
    private OfficesService officesService;

    @RequestMapping("/about/offices")
    @ResponseBody
//    @ModelAttribute("allOffices")
    public List<Office> showOffices(Model model) {
        model.addAttribute("showOffices", officesService.getAllOffices());
        return officesService.getAllOffices();
    }


    @RequestMapping("/about/offices/{id}")
    @ResponseBody
    public Office getOffice(Model model, @PathVariable("id") int id) {
        model.addAttribute("addOffice", officesService.getOffice(id));
        return officesService.getOffice(id);
    }

//    @RequestMapping("/offices/create")
//    @ResponseBody
//    public String showCreateOffice(Model model) {
//        model.addAttribute("showOffice", "An office was created");
//        return "create-office";
//    }

    @PostMapping("/about/offices/")
    @ResponseBody
    public void addOffice(Model model, @RequestBody Office office) {
        model.addAttribute("addOffice", office);
        officesService.addOffice(office);
    }

    @DeleteMapping("/about/offices/{id}")
    @ResponseBody
    public void deleteOffice(Model model, @PathVariable("id") int id) {
        model.addAttribute("deleteOffice", officesService.getOffice(id));
        officesService.deleteOffice(id);
    }

    @ModelAttribute
    public void addAttribute(Model model) {
        model.addAttribute("msg", "Offices");
    }

}
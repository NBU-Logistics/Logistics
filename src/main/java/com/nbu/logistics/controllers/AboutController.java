package com.nbu.logistics.controllers;

import com.nbu.logistics.services.OfficesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * This is the about page controller.
 */
@Controller
public class AboutController {
    @Autowired
    private OfficesService officesService;

    /**
     * /about page get request handler
     * 
     * @param model the controller model
     * @return the about page
     */
    @RequestMapping("/about")
    private String getOfficesPage(Model model) {

        model.addAttribute("allOffices", officesService.getAllOffices());

        return "about";
    }
}
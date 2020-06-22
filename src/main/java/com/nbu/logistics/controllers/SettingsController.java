package com.nbu.logistics.controllers;

import javax.validation.Valid;

import com.nbu.logistics.entities.Settings;
import com.nbu.logistics.services.SettingsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * The settings controller.
 */
@RequestMapping("/settings")
@Controller
public class SettingsController {
    @Autowired
    private SettingsService settingsService;

    /**
     * /settings get request handler
     * 
     * @param model the controller model
     * @return the settings page
     */
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @GetMapping()
    public String getSettingsPage(Model model) {
        model.addAttribute("settings", this.settingsService.getSettings());

        return "settings";
    }

    /**
     * /settings post request handler
     * 
     * @param model         the controller model
     * @param settings      the settings model
     * @param bindingResult the controller BindingResult
     * @return the settings page
     */
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PostMapping()
    public String setSettings(Model model, @Valid @ModelAttribute("settings") Settings settings,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "settings";
        }

        this.settingsService.setSettings(settings);
        model.addAttribute("success", "Settings updated!");

        return this.getSettingsPage(model);
    }
}
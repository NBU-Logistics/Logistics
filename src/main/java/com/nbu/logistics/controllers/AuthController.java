package com.nbu.logistics.controllers;

import java.util.Arrays;

import javax.validation.Valid;

import com.nbu.logistics.config.MyUserPrincipal;
import com.nbu.logistics.controllers.models.UpdateUserViewModel;
import com.nbu.logistics.entities.*;
import com.nbu.logistics.exceptions.InvalidDataException;
import com.nbu.logistics.services.AuthService;
import com.nbu.logistics.services.ClientsService;
import com.nbu.logistics.services.CouriersService;
import com.nbu.logistics.services.OfficeEmployeesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    public interface RegisterAction {
        public void execute();
    }

    @Autowired
    private ClientsService clientsService;

    @Autowired
    private CouriersService couriersService;

    @Autowired
    private OfficeEmployeesService officeEmployeesService;

    @Autowired
    private AuthService authService;

    private String registerUser(Model model, @Valid User user, BindingResult bindingResult, String template,
            String successMessage, String role, RegisterAction registerAction) {
        if (bindingResult.hasErrors()) {
            return template;
        }

        try {
            this.authService.registerUser(user, Arrays.asList(role));
            registerAction.execute();
        } catch (InvalidDataException e) {
            model.addAttribute("error", e.getMessage());

            return template;
        }

        model.addAttribute("success", successMessage);

        return template;
    }

    private MyUserPrincipal getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserPrincipal loggedInUser = (MyUserPrincipal) authentication.getPrincipal();

        return loggedInUser;
    }

    @PreAuthorize("!isAuthenticated()")
    @GetMapping("/login")
    public String showLogin(@RequestParam(required = false) String error, Model model,
            @ModelAttribute("user") User user) {
        if (error != null) {
            model.addAttribute("error", "Wrong login information!");
        }

        return "login";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String showProfile(Model model, @ModelAttribute("user") UpdateUserViewModel user) {
        model.addAttribute("loggedInUser", this.getLoggedInUser());

        return "profile";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/profile")
    public String changeProfile(Model model, @Valid @ModelAttribute("user") UpdateUserViewModel user) {
        MyUserPrincipal loggedInUser = this.getLoggedInUser();
        model.addAttribute("loggedInUser", loggedInUser);

        try {
            this.authService.modifyUser(loggedInUser.getEmail(), user);
        } catch (InvalidDataException e) {
            model.addAttribute("error", e.getMessage());
        }

        model.addAttribute("success", "Successfully changed profile data!");

        return "profile";
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_CLIENT')")
    @PostMapping("/clients/delete")
    public String deleteClient(Model model, @ModelAttribute("user") User user) {
        MyUserPrincipal loggedInUser = this.getLoggedInUser();
        model.addAttribute("loggedInUser", loggedInUser);

        try {
            this.authService.deleteClient(loggedInUser.getEmail());
        } catch (InvalidDataException e) {
            model.addAttribute("error", e.getMessage());

            return "profile";
        }

        return "redirect:/logout";
    }

    @PreAuthorize("!isAuthenticated()")
    @GetMapping("/clients/register")
    public String showRegister(User user) {
        return "register";
    }

    @PostMapping("/clients/register")
    public String registerClient(Model model, @Valid @ModelAttribute("user") User user, BindingResult bindingResult) {
        return this.registerUser(model, user, bindingResult, "register", "Successfully registered!", "ROLE_CLIENT",
                () -> {
                    this.clientsService.createClient(user);
                });
    }

    @GetMapping("/employees/couriers/register")
    public String showCreateCourierEmployee(@ModelAttribute("user") User user) {
        return "create-courier-employee";
    }

    @GetMapping("/employees/office/register")
    public String showCreateOfficeEmployee(@ModelAttribute("user") User user) {
        return "create-office-employee";
    }

    @PostMapping("/employees/couriers/register")
    public String createCourier(Model model, @Valid @ModelAttribute("user") User user, BindingResult bindingResult) {
        return this.registerUser(model, user, bindingResult, "create-courier-employee",
                "Successfully created a courier employee!", "ROLE_COURIER", () -> {
                    this.couriersService.createCourier(user);
                });
    }

    @PostMapping("/employees/office/register")
    public String createOfficeEmployee(Model model, @Valid @ModelAttribute("user") User user,
            BindingResult bindingResult) {
        return this.registerUser(model, user, bindingResult, "create-office-employee",
                "Successfully created an office employee!", "ROLE_OFFICE_EMPLOYEE", () -> {
                    this.officeEmployeesService.createOfficeEmployee(user);
                });
    }

    @PostMapping("/admins/register")
    public String registerAdmin(Model model, @Valid @ModelAttribute("user") User admin, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register-admin";
        }

        try {
            this.authService.createAdmin(admin);
        } catch (InvalidDataException e) {
            model.addAttribute("error", e.getMessage());

            return "register-admin";
        }

        model.addAttribute("success", "Admin created!");

        return "register-admin";
    }
}
package com.nbu.logistics.controllers;

import java.util.Arrays;

import javax.validation.Valid;

import com.nbu.logistics.config.MyUserPrincipal;
import com.nbu.logistics.controllers.models.UpdateUserViewModel;
import com.nbu.logistics.entities.*;
import com.nbu.logistics.exceptions.InvalidDataException;
import com.nbu.logistics.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * This is the authentication controller.
 */
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

    /**
     * Registers a user.
     * 
     * @param model          the controller model
     * @param user           the user entity
     * @param bindingResult  the controller BindingResult
     * @param template       the template to return
     * @param successMessage the success message to display
     * @param role           the user role
     * @param registerAction the register action to execute after the user has been
     *                       registered
     * @return the template
     */
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

    /**
     * /login get request responder
     * 
     * @param error error request parameter
     * @param model the login controller model
     * @param user  the user model attribute
     * @return the login template
     */
    @PreAuthorize("!isAuthenticated()")
    @GetMapping("/login")
    public String showLogin(@RequestParam(required = false) String error, Model model,
            @ModelAttribute("user") User user) {
        if (error != null) {
            model.addAttribute("error", "Wrong login information!");
        }

        return "login";
    }

    /**
     * /profile get request handler
     * 
     * @param model the controller model
     * @param user  the user view model
     * @return the profile page
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String showProfile(Model model, @ModelAttribute("user") UpdateUserViewModel user) {
        model.addAttribute("loggedInUser", this.authService.getLoggedInUser());

        return "profile";
    }

    /**
     * /profile post request handler
     * 
     * @param model the controller model
     * @param user  the user view model
     * @return the profile page
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/profile")
    public String changeProfile(Model model, @Valid @ModelAttribute("user") UpdateUserViewModel user) {
        MyUserPrincipal loggedInUser = this.authService.getLoggedInUser();
        model.addAttribute("loggedInUser", loggedInUser);

        try {
            this.authService.modifyUser(loggedInUser.getEmail(), user);
        } catch (InvalidDataException e) {
            model.addAttribute("error", e.getMessage());
        }

        model.addAttribute("success", "Successfully changed profile data!");

        return "profile";
    }

    /**
     * /clients/delete post request handler
     * 
     * @param model the controller model
     * @param user  the user view model
     * @return the profile page or logout user
     */
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_CLIENT')")
    @PostMapping("/clients/delete")
    public String deleteClient(Model model, @ModelAttribute("user") User user) {
        MyUserPrincipal loggedInUser = this.authService.getLoggedInUser();
        model.addAttribute("loggedInUser", loggedInUser);

        try {
            this.authService.deleteClient(loggedInUser.getEmail());
        } catch (InvalidDataException e) {
            model.addAttribute("error", e.getMessage());

            return "profile";
        }

        return "redirect:/logout";
    }

    /**
     * /clients/register get request handler
     * 
     * @param user the user model
     * @return the register page
     */
    @PreAuthorize("!isAuthenticated()")
    @GetMapping("/clients/register")
    public String showRegister(User user) {
        return "register";
    }

    /**
     * /clients/register post request handler
     * 
     * @param model         the controller model
     * @param user          the user model
     * @param bindingResult the controller BindingResult
     * @return the register page
     */
    @PreAuthorize("!isAuthenticated()")
    @PostMapping("/clients/register")
    public String registerClient(Model model, @Valid @ModelAttribute("user") User user, BindingResult bindingResult) {
        return this.registerUser(model, user, bindingResult, "register", "Successfully registered!", "ROLE_CLIENT",
                () -> {
                    this.clientsService.createClient(user);
                });
    }

    /**
     * /employees/couriers/register get request handler
     * 
     * @param user the user model
     * @return the create-courier-employee page
     */
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @GetMapping("/employees/couriers/register")
    public String showCreateCourierEmployee(@ModelAttribute("user") User user) {
        return "create-courier-employee";
    }

    /**
     * /employees/office/register get request handler
     * 
     * @param user the user model
     * @return the create-office-employee page
     */
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @GetMapping("/employees/office/register")
    public String showCreateOfficeEmployee(@ModelAttribute("user") User user) {
        return "create-office-employee";
    }

    /**
     * /employees/couriers/register post request handler
     * 
     * @param model         the controller model
     * @param user          the user model
     * @param bindingResult the controller BindingResult
     * @return the create-courier-employee page
     */
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PostMapping("/employees/couriers/register")
    public String createCourier(Model model, @Valid @ModelAttribute("user") User user, BindingResult bindingResult) {
        return this.registerUser(model, user, bindingResult, "create-courier-employee",
                "Successfully created a courier employee!", "ROLE_COURIER", () -> {
                    this.couriersService.createCourier(user);
                });
    }

    /**
     * /employees/office/register post request handler
     * 
     * @param model         the controller model
     * @param user          the user model
     * @param bindingResult the controller BindingResult
     * @return the create-office-employee page
     */
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PostMapping("/employees/office/register")
    public String createOfficeEmployee(Model model, @Valid @ModelAttribute("user") User user,
            BindingResult bindingResult) {
        return this.registerUser(model, user, bindingResult, "create-office-employee",
                "Successfully created an office employee!", "ROLE_OFFICE_EMPLOYEE", () -> {
                    this.officeEmployeesService.createOfficeEmployee(user);
                });
    }

    /**
     * /admins/register post request handler
     * 
     * @param model         the controller model
     * @param admin         the user model
     * @param bindingResult the controller BindingResult
     * @return the register-admin page
     */
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
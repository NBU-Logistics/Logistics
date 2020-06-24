package com.nbu.logistics.services;

import java.util.*;

import com.nbu.logistics.config.MyUserPrincipal;
import com.nbu.logistics.controllers.models.UpdateUserViewModel;
import com.nbu.logistics.entities.*;
import com.nbu.logistics.exceptions.InvalidDataException;
import com.nbu.logistics.repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This is the authorization and authentication service.
 */
@Service
@Transactional
public class AuthService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private ClientsRepository clientsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ValidationService validator;

    /**
     * Registering a user and assigning one or more roles to the user.
     * 
     * @param user  the user model
     * @param roles a collection with the requested roles
     * @throws InvalidDataException throws if no user or no roles are provided. Also
     *                              throws when user already exists
     */
    public void registerUser(User user, Collection<String> roles) throws InvalidDataException {
        if (user == null || roles == null) {
            throw new InvalidDataException("Invalid input!");
        }

        if (this.usersRepository.existsByEmail(user.getEmail())) {
            throw new InvalidDataException("User already exists!");
        }

        List<UserRole> userRoles = new ArrayList<>();
        for (String role : roles) {
            UserRole currentRole = new UserRole(role);

            if (!this.rolesRepository.existsByName(role)) {
                this.rolesRepository.save(currentRole);
            } else {
                currentRole = this.rolesRepository.findByName(role);
            }

            userRoles.add(currentRole);
        }

        user.setRoles(userRoles);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        this.usersRepository.save(user);
    }

    /**
     * Changes a given user's profile.
     * 
     * @param email   the user's e-mail
     * @param newUser the user viewmodel with the changed data
     * @throws InvalidDataException throws when no e-mail or no user data is
     *                              provided. Also throws when the user does not
     *                              exist.
     */
    public void modifyUser(String email, UpdateUserViewModel newUser) throws InvalidDataException {
        if (email == null || newUser == null) {
            throw new InvalidDataException("Invalid input!");
        }

        User existingUser = this.usersRepository.findByEmail(email);
        if (existingUser == null) {
            throw new InvalidDataException("User does not exist!");
        }

        MyUserPrincipal userPrincipal = this.getLoggedInUser();
        if (newUser.getEmail() != null && !newUser.getEmail().isBlank()) {
            existingUser.setEmail(newUser.getEmail());
            this.validator.validate(existingUser);
            userPrincipal.setEmail(newUser.getEmail());
        }

        if (newUser.getFirstName() != null && !newUser.getFirstName().isBlank()) {
            existingUser.setFirstName(newUser.getFirstName());
            this.validator.validate(existingUser);
            userPrincipal.setFirstName(newUser.getFirstName());
        }

        if (newUser.getLastName() != null && !newUser.getLastName().isBlank()) {
            existingUser.setLastName(newUser.getLastName());
            this.validator.validate(existingUser);
            userPrincipal.setLastName(newUser.getLastName());
        }

        if (newUser.getPassword() != null && !newUser.getPassword().isBlank()) {
            existingUser.setPassword(newUser.getPassword());
            this.validator.validate(existingUser);
            existingUser.setPassword(this.passwordEncoder.encode(newUser.getPassword()));
        }

        this.usersRepository.save(existingUser);
    }

    /**
     * Deletes a user by given e-mail.
     * 
     * @param email the user's e-mail
     * @throws InvalidDataException throws when no e-mail is provided. Also throws
     *                              when the user does not exist.
     */
    public void deleteUser(String email) throws InvalidDataException {
        if (email == null) {
            throw new InvalidDataException("Invalid data!");
        }

        User existingUser = this.usersRepository.findByEmail(email);
        if (existingUser == null) {
            throw new InvalidDataException("User does not exist!");
        }

        existingUser.setDeleted(true);
        this.usersRepository.save(existingUser);
    }

    /**
     * Deletes a client by given e-mail.
     * 
     * @param email the client's e-mail
     * @throws InvalidDataException throws when no e-mail is provided. Also throws
     *                              when the user does not exist.
     */
    public void deleteClient(String email) throws InvalidDataException {
        if (email == null) {
            throw new InvalidDataException("Invalid data!");
        }

        Client existingClient = this.clientsRepository.findByUserEmail(email);
        if (existingClient == null) {
            throw new InvalidDataException("Client does not exist!");
        }

        existingClient.getReceivedDeliveries().forEach((delivery) -> delivery.setRecipient(null));
        existingClient.getSentDeliveries().forEach((delivery) -> delivery.setSender(null));
        existingClient.setDeleted(true);
        this.clientsRepository.save(existingClient);

        this.deleteUser(email);
    }

    /**
     * Returns the currently logged in user.
     * 
     * @return the logged in user's information.
     */
    public MyUserPrincipal getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (MyUserPrincipal) authentication.getPrincipal();
    }

    /**
     * Checks if the currently logged in user is in the given role.
     * 
     * @param role the role to check
     * @return true if the user is in this role and false if not
     */
    public boolean isInRole(String role) {
        Collection<? extends GrantedAuthority> authorities = this.getLoggedInUser().getAuthorities();
        boolean authorized = authorities.contains(new SimpleGrantedAuthority(role));

        return authorized;
    }

    /**
     * Checks if the database has an administrator registered.
     * 
     * @return true if there is an administrator and false if not
     */
    public boolean adminExists() {
        UserRole adminRole = this.rolesRepository.findFirstByName("ROLE_ADMIN");
        if (adminRole == null) {
            return false;
        }

        return this.usersRepository.existsByRoles(adminRole);
    }

    /**
     * Creates an administrator.
     * 
     * @param admin the admin entity
     * @throws InvalidDataException throws if no entity is provided. Also throws
     *                              when the administrator is already registered.
     */
    public void createAdmin(User admin) throws InvalidDataException {
        if (admin == null) {
            throw new InvalidDataException("No user provided!");
        }

        if (this.adminExists()) {
            throw new InvalidDataException("Admin already exists!");
        }

        this.registerUser(admin, Arrays.asList("ROLE_ADMIN"));
    }
}
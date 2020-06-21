package com.nbu.logistics.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.nbu.logistics.config.MyUserPrincipal;
import com.nbu.logistics.controllers.models.UpdateUserViewModel;
import com.nbu.logistics.entities.Client;
import com.nbu.logistics.entities.User;
import com.nbu.logistics.entities.UserRole;
import com.nbu.logistics.exceptions.InvalidDataException;
import com.nbu.logistics.repositories.ClientsRepository;
import com.nbu.logistics.repositories.RolesRepository;
import com.nbu.logistics.repositories.UsersRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public MyUserPrincipal getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (MyUserPrincipal) authentication.getPrincipal();
    }

    public boolean isInRole(String role) {
        Collection<? extends GrantedAuthority> authorities = this.getLoggedInUser().getAuthorities();
        boolean authorized = authorities.contains(new SimpleGrantedAuthority(role));

        return authorized;
    }

    public boolean adminExists() {
        UserRole adminRole = this.rolesRepository.findFirstByName("ROLE_ADMIN");
        if (adminRole == null) {
            return false;
        }

        return this.usersRepository.existsByRoles(adminRole);
    }

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
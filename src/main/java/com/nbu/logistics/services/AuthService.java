package com.nbu.logistics.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.nbu.logistics.entities.User;
import com.nbu.logistics.entities.UserRole;
import com.nbu.logistics.exceptions.InvalidDataException;
import com.nbu.logistics.repositories.RolesRepository;
import com.nbu.logistics.repositories.UsersRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private RolesRepository rolesRepository;

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

        this.usersRepository.save(user);
    }
}
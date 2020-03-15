package com.nbu.logistics.services;

import com.nbu.logistics.entities.User;
import com.nbu.logistics.entities.UserRole;
import com.nbu.logistics.exceptions.InvalidDataException;
import com.nbu.logistics.repositories.RolesRepository;
import com.nbu.logistics.repositories.UsersRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private RolesRepository rolesRepository;

    public void registerUser(User user, String role) throws InvalidDataException {
        if (user == null || role == null) {
            throw new InvalidDataException("Invalid input!");
        }

        if (this.usersRepository.existsByEmail(user.getEmail())) {
            throw new InvalidDataException("User already exists!");
        }

        UserRole currentRole = new UserRole(role);
        user.setRole(currentRole);

        if (!this.rolesRepository.existsByName(role)) {
            this.rolesRepository.save(currentRole);
        }

        this.usersRepository.save(user);
    }
}
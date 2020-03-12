package com.nbu.logistics.services;

import com.nbu.logistics.entities.User;
import com.nbu.logistics.exceptions.InvalidDataException;
import com.nbu.logistics.repositories.UsersRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UsersRepository usersRepository;

    public void registerUser(User user) throws InvalidDataException {
        if (this.usersRepository.existsByEmail(user.getEmail())) {
            throw new InvalidDataException("User already exists!");
        }

        this.usersRepository.save(user);
    }

    public void loginUser(User user) throws InvalidDataException {
        User foundUser = this.usersRepository.findByEmail(user.getEmail());

        if (foundUser == null) {
            throw new InvalidDataException("User does not exist!");
        }

        if (!foundUser.getPassword().equals(user.getPassword())) {
            throw new InvalidDataException("Invalid password");
        }
    }
}
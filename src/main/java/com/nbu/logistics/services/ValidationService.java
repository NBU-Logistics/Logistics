package com.nbu.logistics.services;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.nbu.logistics.exceptions.InvalidDataException;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ValidationService {
    private final Validator validator;

    public <T> void validate(T entity) throws InvalidDataException {
        Set<ConstraintViolation<T>> violations = this.validator.validate(entity);
        if (violations.size() > 0) {
            throw new InvalidDataException(violations.iterator().next().getMessage());
        }
    }

    public <T> boolean isValid(T entity) {
        try {
            this.validate(entity);
        } catch (InvalidDataException e) {
            return false;
        }

        return true;
    }
}

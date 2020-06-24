package com.nbu.logistics.services;

import java.util.Set;

import javax.validation.*;

import com.nbu.logistics.exceptions.InvalidDataException;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

/**
 * This is the validation service. It manually activates entity validation.
 */
@Service
@AllArgsConstructor
public class ValidationService {
    private final Validator validator;

    /**
     * This validates a single entity.
     * 
     * @param <T>    the entity's concrete type
     * @param entity the entity
     * @throws InvalidDataException throws when the entity is invalid
     */
    public <T> void validate(T entity) throws InvalidDataException {
        Set<ConstraintViolation<T>> violations = this.validator.validate(entity);
        if (violations.size() > 0) {
            throw new InvalidDataException(violations.iterator().next().getMessage());
        }
    }

    /**
     * Checks if a given entity is valid.
     * 
     * @param <T>    the entity's concrete type
     * @param entity the entity
     * @return true if the entity is valid and false if not
     */
    public <T> boolean isValid(T entity) {
        try {
            this.validate(entity);
        } catch (InvalidDataException e) {
            return false;
        }

        return true;
    }
}

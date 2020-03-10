package com.nbu.logistics.repositories;

public interface UsersRepository {
    boolean existsByEmail(String email);
}
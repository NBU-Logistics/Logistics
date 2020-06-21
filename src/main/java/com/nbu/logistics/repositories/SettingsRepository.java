package com.nbu.logistics.repositories;

import com.nbu.logistics.entities.Settings;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingsRepository extends JpaRepository<Settings, Long> {

}
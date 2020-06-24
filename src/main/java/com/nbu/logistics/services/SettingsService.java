package com.nbu.logistics.services;

import java.util.List;

import com.nbu.logistics.entities.Settings;
import com.nbu.logistics.repositories.SettingsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This is the settings service. It saves and loads the data about the prices.
 */
@Service
public class SettingsService {
    @Autowired
    private SettingsRepository settingsRepository;

    /**
     * Returns the settings from the database.
     * 
     * @return the settings
     */
    public Settings getSettings() {
        List<Settings> settings = this.settingsRepository.findAll();
        if (settings.size() == 0) {
            return new Settings(0, 0);
        }

        return settings.get(0);
    }

    /**
     * This saves the settings to the database.
     * 
     * @param settings the settings
     */
    public void setSettings(Settings settings) {
        Settings existingSettings = this.getSettings();
        existingSettings.setPricePerKilogram(settings.getPricePerKilogram());
        existingSettings.setSentToOfficeDiscount(settings.getSentToOfficeDiscount());

        this.settingsRepository.save(existingSettings);
    }
}
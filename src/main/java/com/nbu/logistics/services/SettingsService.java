package com.nbu.logistics.services;

import java.util.List;

import com.nbu.logistics.entities.Settings;
import com.nbu.logistics.repositories.SettingsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {
    @Autowired
    private SettingsRepository settingsRepository;

    public Settings getSettings() {
        List<Settings> settings = this.settingsRepository.findAll();
        if (settings.size() == 0) {
            return new Settings(0, 0);
        }

        return settings.get(0);
    }

    public void setSettings(Settings settings) {
        Settings existingSettings = this.getSettings();
        existingSettings.setPricePerKilogram(settings.getPricePerKilogram());
        existingSettings.setSentToOfficeDiscount(settings.getSentToOfficeDiscount());

        this.settingsRepository.save(existingSettings);
    }
}
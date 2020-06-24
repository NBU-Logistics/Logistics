package com.nbu.logistics.services;

import com.nbu.logistics.entities.*;
import com.nbu.logistics.exceptions.InvalidDataException;
import com.nbu.logistics.repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This is the company service.
 */
@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private DeliveriesRepository deliveriesRepository;

    /**
     * Creates the company.
     * 
     * @param companyData the company entity.
     * @throws InvalidDataException when either company data fields are empty. Also
     *                              throws when the company has already been
     *                              created.
     */
    public void createCompany(Company companyData) throws InvalidDataException {
        if (companyRepository.findAll().size() == 0) {
            if (companyData.getEmail().isBlank()) {
                throw new InvalidDataException("Fields can not be empty!");
            }

            if (companyData.getAddress().isBlank()) {
                throw new InvalidDataException("Fields can not be empty!");
            }

            if (companyData.getName().isBlank()) {
                throw new InvalidDataException("Fields can not be empty!");
            }

            if (companyData.getOwnerName().isBlank()) {
                throw new InvalidDataException("Fields can not be empty!");
            }

            if (companyData.getPhoneNumber().isBlank()) {
                throw new InvalidDataException("Fields can not be empty!");

            }

            this.companyRepository.save(companyData);
        } else {
            throw new InvalidDataException("A company already exists!");
        }
    }

    /**
     * Updates the company's data.
     * 
     * @param companyData the company entity
     * @throws InvalidDataException when the company does not exist
     */
    public void updateCompany(Company companyData) throws InvalidDataException {
        if (companyRepository.findAll().size() != 0) {
            Company company = companyRepository.findAll().get(0);
            if (company == null) {
                throw new InvalidDataException("Company does not exist!");
            }

            if (!companyData.getAddress().isBlank()) {
                company.setAddress(companyData.getAddress());
            }

            if (!companyData.getEmail().isBlank()) {
                company.setEmail(companyData.getEmail());
            }

            if (!companyData.getName().isBlank()) {
                company.setName(companyData.getName());
            }

            if (!companyData.getOwnerName().isBlank()) {
                company.setOwnerName(companyData.getOwnerName());
            }

            if (!companyData.getPhoneNumber().isBlank()) {
                company.setPhoneNumber(companyData.getPhoneNumber());
            }

            this.companyRepository.save(company);
        } else {
            throw new InvalidDataException("Company does not exist!");
        }
    }

    /**
     * Deletes the company.
     * 
     * @throws InvalidDataException when the company does not exist
     */
    public void deleteCompany() throws InvalidDataException {
        if (companyRepository.findAll().size() != 0) {
            Company company = companyRepository.findAll().get(0);
            company.setDeleted(true);
            this.companyRepository.save(company);
        } else {
            throw new InvalidDataException("Company does not exist!");
        }
    }

    /**
     * Returns the company data.
     * 
     * @return the company
     * @throws InvalidDataException when the company does not exist
     */
    public Company getCompany() throws InvalidDataException {
        List<Company> companies = companyRepository.findAll();
        if (companies.size() == 0) {
            throw new InvalidDataException("Company does not exist!");
        }

        return this.companyRepository.findAll().get(0);
    }

    /**
     * Calculates the comapny's income for given period of time.
     * 
     * @param fromDate the date from which the period begins
     * @param toDate   the date from which the period ends
     * @return the calculated income
     */
    public BigDecimal calculateIncome(LocalDate fromDate, LocalDate toDate) {
        List<Delivery> deliveries = this.deliveriesRepository.findByStatus(DeliveryStatus.DELIVERED).stream()
                .filter((delivery) -> fromDate.compareTo(delivery.getCreatedOn()) < 1
                        && toDate.compareTo(delivery.getCreatedOn()) > -1)
                .collect(Collectors.toList());
        BigDecimal income = new BigDecimal(0);
        for (Delivery delivery : deliveries) {
            income = income.add(new BigDecimal(delivery.getPrice()));
        }

        return income;
    }
}
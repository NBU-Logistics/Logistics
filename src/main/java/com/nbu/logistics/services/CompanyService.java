package com.nbu.logistics.services;

import com.nbu.logistics.entities.Company;
import com.nbu.logistics.exceptions.InvalidDataException;
import com.nbu.logistics.repositories.CompanyRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

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

    public void deleteCompany() throws InvalidDataException {
        if (companyRepository.findAll().size() != 0) {
            Company company = companyRepository.findAll().get(0);
            company.setDeleted(true);
            this.companyRepository.save(company);
        } else {
            throw new InvalidDataException("Company does not exist!");
        }
    }

    public Company getCompany() throws InvalidDataException {
        List<Company> companies = companyRepository.findAll();
        if (companies.size() == 1) {
            return this.companyRepository.findAll().get(0);
        } else if (companies.size() > 1) {
            throw new InvalidDataException("More than one company!");
        } else {
            throw new InvalidDataException("Company does not exist!");
        }
    }
}
package com.nbu.logistics.services;

import com.nbu.logistics.entities.Company;
import com.nbu.logistics.exceptions.InvalidDataException;
import com.nbu.logistics.repositories.CompanyRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    public void createCompany(Company companyData) throws InvalidDataException{
        if(companyRepository.findAll().size() == 0) {
            Company company = new Company();

            if (companyData.getEmail() != null) {
                if ("".equals(companyData.getEmail().trim())) {
                    throw new InvalidDataException("Fields can not be empty!");
                } else {
                    company.setEmail(companyData.getEmail());
                }
            }
            if (companyData.getAddress() != null) {
                if ("".equals(companyData.getAddress().trim())) {
                    throw new InvalidDataException("Fields can not be empty!");
                } else {
                    company.setAddress(companyData.getAddress());
                }
            }
            if (companyData.getName() != null) {
                if ("".equals(companyData.getName().trim())) {
                    throw new InvalidDataException("Fields can not be empty!");
                } else {
                    company.setName(companyData.getName());
                }
            }
            if (companyData.getOwnerName() != null) {
                if ("".equals(companyData.getOwnerName().trim())) {
                    throw new InvalidDataException("Fields can not be empty!");
                } else {
                    company.setOwnerName(companyData.getOwnerName());
                }
            }
            if (companyData.getPhoneNumber() != null) {
                if ("".equals(companyData.getPhoneNumber().trim())) {
                    throw new InvalidDataException("Fields can not be empty!");
                } else {
                    company.setPhoneNumber(companyData.getPhoneNumber());
                }
            }
            company.setDtype("Company");
            this.companyRepository.save(company);
        }
        else{
            throw new InvalidDataException("A company already exists!");
        }
    }

    public void updateCompany(Company companyData) throws InvalidDataException{
        if(companyRepository.findAll().size() != 0){
            Company company = companyRepository.findAll().get(0);
            if (company == null) {
                throw new InvalidDataException("Company does not exist!");
            }

            if(companyData.getAddress() != null){
                if (!"".equals(companyData.getAddress().trim())) {
                    company.setAddress(companyData.getAddress());
                }
            }
            if(companyData.getEmail() != null){
                if (!"".equals(companyData.getEmail().trim())) {
                    company.setEmail(companyData.getEmail());
                }
            }
            if(companyData.getName() != null){
                if (!"".equals(companyData.getName().trim())) {
                    company.setName(companyData.getName());
                }
            }
            if(companyData.getOwnerName() != null){
                if (!"".equals(companyData.getOwnerName().trim())) {
                    company.setOwnerName(companyData.getOwnerName());
                }
            }
            if(companyData.getPhoneNumber() != null){
                if (!"".equals(companyData.getPhoneNumber().trim())) {
                    company.setPhoneNumber(companyData.getPhoneNumber());
                }
            }
            this.companyRepository.save(company);
        }
        else{
            throw new InvalidDataException("No companies exist!");
        }
    }

    public void deleteCompany() throws InvalidDataException{
        if(companyRepository.findAll().size() != 0){
            Company company = companyRepository.findAll().get(0);
            company.setDeleted(true);
            this.companyRepository.save(company);
        }
        else{
            throw new InvalidDataException("No companies exist!");
        }
    }

    public Company getCompany() throws InvalidDataException{
        List<Company> companies = companyRepository.findAll();
        if(companies.size() == 1){
            return this.companyRepository.findAll().get(0);
        }
        else if(companies.size() > 1){
            throw new InvalidDataException("More than one company!");
        }
        else{
            throw new InvalidDataException("No companies exist!");
        }
    }

}
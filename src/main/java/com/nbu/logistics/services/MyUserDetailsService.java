package com.nbu.logistics.services;

import com.nbu.logistics.repositories.ClientsRepository;
import com.nbu.logistics.repositories.CouriersRepository;
import com.nbu.logistics.repositories.OfficeEmployeesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private ClientsRepository clientsRepository;

    @Autowired
    private CouriersRepository couriersRepository;

    @Autowired
    private OfficeEmployeesRepository officeEmployeesRepository;

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }
}
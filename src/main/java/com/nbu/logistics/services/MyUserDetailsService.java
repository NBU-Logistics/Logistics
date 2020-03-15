package com.nbu.logistics.services;

import java.util.Collection;

import com.nbu.logistics.entities.User;
import com.nbu.logistics.repositories.UsersRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UsersRepository usersRepository;

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = this.usersRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("No user found with username: " + email);
        }

        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        String[] userRoles = { user.getRole().getName() };
        Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(userRoles);

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), enabled,
                accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }
}
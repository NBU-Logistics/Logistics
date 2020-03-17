package com.nbu.logistics.config;

import java.util.Collection;

import com.nbu.logistics.entities.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

public class MyUserPrincipal implements UserDetails {
    private User user;
    private Collection<GrantedAuthority> authorities;

    public MyUserPrincipal(User user) {
        this.user = user;

        String[] userRoles = user.getRoles().stream().map((role) -> role.getName()).toArray(String[]::new);
        this.authorities = AuthorityUtils.createAuthorityList(userRoles);
    }

    public String getFirstName() {
        return this.user.getFirstName();
    }

    public String getLastName() {
        return this.user.getLastName();
    }

    public String getEmail() {
        return this.user.getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getFirstName() + " " + this.user.getLastName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
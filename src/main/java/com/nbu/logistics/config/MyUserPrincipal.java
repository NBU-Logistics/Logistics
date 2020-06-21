package com.nbu.logistics.config;

import java.util.Collection;

import com.nbu.logistics.entities.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * MyUserPrincipal is a class that represents the currently logged in user. It
 * is being used by spring session.
 */
public class MyUserPrincipal implements UserDetails {
    private static final long serialVersionUID = 1L;
    private User user;
    private Collection<GrantedAuthority> authorities;

    /**
     * MyUserPrincipal's constructor
     * 
     * @param user is the user entity from the database
     */
    public MyUserPrincipal(User user) {
        this.user = user;

        String[] userRoles = user.getRoles().stream().map((role) -> role.getName()).toArray(String[]::new);
        this.authorities = AuthorityUtils.createAuthorityList(userRoles);
    }

    /**
     * Returns the logged in user's id as it is in the database.
     * 
     * @return the user's id
     */
    public long getUserId() {
        return this.user.getId();
    }

    /**
     * Returns the logged in user's first name.
     * 
     * @return the user's first name
     */
    public String getFirstName() {
        return this.user.getFirstName();
    }

    /**
     * It sets the logged in user's first name.
     * 
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.user.setFirstName(firstName);
    }

    /**
     * Returns the logged in user's last name.
     * 
     * @return the user's last name.
     */
    public String getLastName() {
        return this.user.getLastName();
    }

    /**
     * It sets the logged in user's last name.
     * 
     * @param lastName the user's last name to set.
     */
    public void setLastName(String lastName) {
        this.user.setLastName(lastName);
    }

    /**
     * Returns the logged in user's e-mail.
     * 
     * @return the user's e-mail
     */
    public String getEmail() {
        return this.user.getEmail();
    }

    /**
     * It sets the logged in user's e-mail.
     * 
     * @param email the user's e-mail to set
     */
    public void setEmail(String email) {
        this.user.setEmail(email);
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

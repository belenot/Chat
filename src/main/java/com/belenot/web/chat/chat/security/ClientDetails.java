package com.belenot.web.chat.chat.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.belenot.web.chat.chat.domain.Admin;
import com.belenot.web.chat.chat.domain.Client;
import com.belenot.web.chat.chat.domain.Moderator;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class ClientDetails implements UserDetails {

    public ClientDetails(Client client) {
        this.client = client;
    }

    private Client client;
    public enum ClientAuthorities {CLIENT, MODERATOR, ADMIN};

    public Client getClient() {
        return client;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (client instanceof Client) authorities.add(new SimpleGrantedAuthority("ROLE_CLIENT"));
        if (client instanceof Admin) {
            authorities.add(new SimpleGrantedAuthority("ROLE_CLIENT"));
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return client.getPassword();
    }

    @Override
    public String getUsername() {
        return client.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
}
package com.register.wowlibre.domain.shared;

import com.register.wowlibre.domain.model.RolModel;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    @Getter
    private final Long userId;
    private final Collection<? extends GrantedAuthority> authorities;
    private final String password;
    private final String username;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;
    @Getter
    private final String avatarUrl;
    @Getter
    private final String language;
    @Getter
    private final boolean pendingValidation;

    public CustomUserDetails(Collection<? extends GrantedAuthority> authorities, String password, String username,
                             boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired,
                             boolean enabled, Long accountWebId, String avatarUrl, String language,
                             boolean pendingValidation) {
        this.authorities = authorities;
        this.password = password;
        this.username = username;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
        this.userId = accountWebId;
        this.avatarUrl = avatarUrl;
        this.language = language;
        this.pendingValidation = pendingValidation;
    }

    public CustomUserDetails(List<RolModel> authorities, String password, String username,
                             boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired,
                             boolean enabled, Long userId, String avatarUrl, String language) {

        this.authorities = authorities.stream()
                .map(rolModel -> new SimpleGrantedAuthority(rolModel.name()))
                .collect(Collectors.toList());
        this.password = password;
        this.username = username;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
        this.userId = userId;
        this.avatarUrl = avatarUrl;
        this.language = language;
        this.pendingValidation = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}

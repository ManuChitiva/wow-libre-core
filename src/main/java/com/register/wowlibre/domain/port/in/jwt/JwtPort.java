package com.register.wowlibre.domain.port.in.jwt;


import com.register.wowlibre.domain.shared.CustomUserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Date;

public interface JwtPort {
    String generateToken(CustomUserDetails userDetails);

    String extractUsername(String token);

    Long extractUserId(String token);

    boolean isTokenValid(String token);

    String generateRefreshToken(CustomUserDetails userDetails);

    Date extractExpiration(String token);

    Collection<GrantedAuthority> extractRoles(String token);

}

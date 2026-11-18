package com.securityblueprint.appsec.identity.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {
    /**
     * Retrieve the currentyl authenticated user's identity
     */
    public CurrentUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof JwtAuthenticationToken jwtauth) || !authentication.isAuthenticated()){
            throw new IllegalStateException(
                    "No authenticated JWT user found"
            );
        }

        var jwt = jwtauth.getToken();

        return new CurrentUser(
                jwt.getSubject(),
                jwt.getClaimAsString("preferred_username"),
                jwt.getClaimAsString("email"),
                jwt.getClaimAsString("name")
        );
    }

    /**
     * Retrieve the authenticated user's unique subject identifier.
     */
    public String getCurrentUserId(){
        return getCurrentUser().subject();
    }

    /**
     * Represents the identity extracted from a validated JWT.
     */
    public record CurrentUser(
            String subject,
            String username,
            String email,
            String name
    ){}
}

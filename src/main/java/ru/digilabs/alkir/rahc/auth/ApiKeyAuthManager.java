package ru.digilabs.alkir.rahc.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import ru.digilabs.alkir.rahc.configuration.AuthConfigurationProperties;

@Component
@RequiredArgsConstructor
public class ApiKeyAuthManager implements AuthenticationManager {

    private final AuthConfigurationProperties authProperties;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var token = authProperties.getToken();

        String principal = (String) authentication.getPrincipal();

        if (!principal.equals(token)) {
            throw new BadCredentialsException("The API key was not found or not the expected value.");
        }
        authentication.setAuthenticated(true);
        return authentication;
    }

}

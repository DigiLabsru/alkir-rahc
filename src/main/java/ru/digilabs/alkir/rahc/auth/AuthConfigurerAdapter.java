package ru.digilabs.alkir.rahc.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import ru.digilabs.alkir.rahc.configuration.AuthConfigurationProperties;

@Configuration
@RequiredArgsConstructor
@Profile("web")
@EnableWebSecurity
@Order(1)
public class AuthConfigurerAdapter {

    private final ApiKeyAuthManager apiKeyAuthManager;
    private final AuthConfigurationProperties authProperties;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(httpSecuritySessionManagementConfigurer ->
                httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        if (authProperties.isEnabled()) {
            var bearerTokenAuthenticationFilter = new BearerTokenAuthenticationFilter(apiKeyAuthManager);

            http
                .addFilter(bearerTokenAuthenticationFilter)
                .authorizeHttpRequests(authorizeRequests ->
                    authorizeRequests
                        .anyRequest()
                        .authenticated()
                );
        }

        return http.build();
    }
}

package ru.digilabs.alkir.rahc.auth;

import ru.digilabs.alkir.rahc.configuration.AuthConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@Order(1)
public class AuthConfigurerAdapter extends WebSecurityConfigurerAdapter {

  private final ApiKeyAuthManager apiKeyAuthManager;
  private final AuthConfigurationProperties authProperties;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    var filter = new BearerTokenAuthenticationFilter(apiKeyAuthManager);

    var httpSecuritySessionManagementConfigurer = http.antMatcher("/api/**")
      .csrf()
      .disable()
      .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    if (authProperties.isEnabled()) {
      httpSecuritySessionManagementConfigurer.and()
        .addFilter(filter)
        .authorizeRequests()
        .anyRequest()
        .authenticated();
    }
  }
}

package ru.digilabs.alkir.rahc.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;

@ConfigurationProperties("auth")
@Profile("web")
@Data
public class AuthConfigurationProperties {
    String token = "";

    public boolean isEnabled() {
        return !token.isBlank();
    }
}

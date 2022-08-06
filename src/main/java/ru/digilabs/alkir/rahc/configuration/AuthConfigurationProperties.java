package ru.digilabs.alkir.rahc.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("auth")
@Data
public class AuthConfigurationProperties {
  String token = "";

  public boolean isEnabled() {
    return !token.isBlank();
  }
}

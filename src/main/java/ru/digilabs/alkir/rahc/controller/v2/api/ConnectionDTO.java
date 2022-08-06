package ru.digilabs.alkir.rahc.controller.v2.api;

import lombok.Data;
import ru.digilabs.alkir.rahc.configuration.RasConfigurationProperties;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Optional;

@Data
public class ConnectionDTO {
  @NotBlank
  String address;

  @Positive
  @NotNull
  Integer port;

  Optional<String> clusterAdminUsername = Optional.empty();
  Optional<String> clusterAdminPassword = Optional.empty();
  Optional<String> centralServerAdminUsername = Optional.empty();
  Optional<String> centralServerAdminPassword = Optional.empty();

  Optional<String> clusterId = Optional.empty();
  Optional<String> clusterName = Optional.empty();

  Optional<String> ibId = Optional.empty();
  Optional<String> ibName = Optional.empty();
  Optional<String> ibUsername = Optional.empty();
  Optional<String> ibPassword = Optional.empty();

  public RasConfigurationProperties toConfigurationProperties() {
    return new RasConfigurationProperties()
      .setAddress(address)
      .setPort(port)
      .setClusterAdminUsername(clusterAdminUsername.orElse(""))
      .setClusterAdminPassword(clusterAdminPassword.orElse(""))
      .setCentralServerAdminUsername(centralServerAdminUsername.orElse(""))
      .setCentralServerAdminPassword(centralServerAdminPassword.orElse(""));
  }
}

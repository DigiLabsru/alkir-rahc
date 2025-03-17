package ru.digilabs.alkir.rahc.configuration;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("ras")
@Data
@Accessors(chain = true)
public class RasConfigurationProperties {
    int port = 1545;
    String address = "localhost";
    int timeout = 10000;
    String clusterAdminUsername = "";
    String clusterAdminPassword = "";
    String centralServerAdminUsername = "";
    String centralServerAdminPassword = "";
}

package ru.digilabs.alkir.rahc;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableAdminServer
public class RahcApplication {

    public static void main(String[] args) {
        SpringApplication.run(RahcApplication.class, args);
    }

}

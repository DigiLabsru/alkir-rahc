package ru.digilabs.alkir.rahc.configuration;

import org.aspectj.lang.Aspects;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import ru.digilabs.alkir.rahc.aop.CliErrorHandlerAspect;
import ru.digilabs.alkir.rahc.aop.RetryableRacMethodAspect;

@Configuration
public class AspectJConfiguration {

    @Bean
    @Lazy(false)
    public RetryableRacMethodAspect retryableRacMethodAspect() {
        return Aspects.aspectOf(RetryableRacMethodAspect.class);
    }

    @Bean
    @Lazy(false)
    @Profile("cli")
    public CliErrorHandlerAspect cliErrorHandlerAspect() {
        return Aspects.aspectOf(CliErrorHandlerAspect.class);
    }

}

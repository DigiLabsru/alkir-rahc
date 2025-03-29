package ru.digilabs.alkir.rahc.configuration;

import com._1c.v8.ibis.V8Exception;
import com._1c.v8.ibis.admin.AgentAdminException;
import com._1c.v8.ibis.admin.client.AgentAdminConnectorFactory;
import com._1c.v8.ibis.admin.client.IAgentAdminConnectorFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.support.RetryTemplate;
import ru.digilabs.alkir.rahc.service.RacService;

import java.util.List;

import static org.springframework.web.context.WebApplicationContext.SCOPE_SESSION;

@Configuration
@EnableRetry
public class AppConfiguration {

    @Bean
    IAgentAdminConnectorFactory adminConnectorFactory() {
        return new AgentAdminConnectorFactory();
    }

    @Bean
    public RetryTemplate racRetryTemplate() {
        return RetryTemplate.builder()
            .retryOn(List.of(V8Exception.class, AgentAdminException.class))
            .fixedBackoff(1000)
            .maxAttempts(3)
            .build();
    }

    @Bean
    @Scope(value = SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
    RacService sessionScopedRacService(RacService racService) {
        return racService;
    }

}

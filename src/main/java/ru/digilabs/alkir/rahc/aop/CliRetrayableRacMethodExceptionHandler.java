package ru.digilabs.alkir.rahc.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("cli")
@Slf4j
public class CliRetrayableRacMethodExceptionHandler implements RetrayableRacMethodExceptionHandler {
    @Override
    public void handle(Throwable throwable) {
        LOGGER.debug("Handling exception from retryable RAC method", throwable);
    }
}

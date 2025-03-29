package ru.digilabs.alkir.rahc.aop;

import lombok.SneakyThrows;

public class WebRetrayableRacMethodExceptionHandler implements RetrayableRacMethodExceptionHandler {
    @Override
    @SneakyThrows
    public void handle(Throwable throwable) {
        throw throwable;
    }
}

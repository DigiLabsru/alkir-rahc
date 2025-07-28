package ru.digilabs.alkir.rahc.command.cli;

import lombok.SneakyThrows;

public interface CliPrintWriter {
    @SneakyThrows
    void printSuccess(Object object);

    @SneakyThrows
    void printError(String errorMessage, Throwable throwable);

    @SneakyThrows
    void println(Object object);
}

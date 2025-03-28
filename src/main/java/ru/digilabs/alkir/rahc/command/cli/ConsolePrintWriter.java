package ru.digilabs.alkir.rahc.command.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;

@Component
public class ConsolePrintWriter extends PrintWriter implements CliPrintWriter {
    private final ObjectMapper objectMapper;

    public ConsolePrintWriter(ObjectMapper objectMapper) {
        super(System.out);
        this.objectMapper = objectMapper;
    }

    @Override
    public void printSuccess(Object object) {
        var result = CliResult.success(object);
        println(result);
    }

    @Override
    public void printError(String errorMessage, Throwable throwable) {
        var result = CliResult.error(errorMessage, throwable);
        println(result);
    }

    @SneakyThrows
    @Override
    public void println(Object object) {
        super.println(objectMapper.writeValueAsString(object));
    }
}

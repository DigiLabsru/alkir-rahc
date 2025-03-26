package ru.digilabs.alkir.rahc.command.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;

import java.io.PrintWriter;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Command(
    usageHelpAutoWidth = true,
    mixinStandardHelpOptions = true,
    sortOptions = false
)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface RacCliSubCommand {
    @Component
    class ConsolePrintWriter extends PrintWriter {
        private final ObjectMapper objectMapper;

        public ConsolePrintWriter(ObjectMapper objectMapper) {
            super(System.out);
            this.objectMapper = objectMapper;
        }

        @Override
        @SneakyThrows
        public void println(Object object) {
            super.println(objectMapper.writeValueAsString(object));
        }
    }
}

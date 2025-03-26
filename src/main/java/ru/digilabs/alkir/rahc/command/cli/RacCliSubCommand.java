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

        /**
         * Constructs a ConsolePrintWriter that directs output to the standard output stream and uses the provided ObjectMapper to serialize objects into JSON.
         *
         * @param objectMapper the ObjectMapper used for converting objects to their JSON representation
         */
        public ConsolePrintWriter(ObjectMapper objectMapper) {
            super(System.out);
            this.objectMapper = objectMapper;
        }

        /**
         * Serializes the specified object to JSON format and prints it to the standard output.
         *
         * <p>This method overrides {@code println} from {@link PrintWriter} and leverages an {@code ObjectMapper} to
         * convert the object into its JSON representation before printing.</p>
         *
         * @param object the object to serialize and print as JSON
         */
        @Override
        @SneakyThrows
        public void println(Object object) {
            super.println(objectMapper.writeValueAsString(object));
        }
    }
}

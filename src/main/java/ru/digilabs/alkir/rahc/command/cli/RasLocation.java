package ru.digilabs.alkir.rahc.command.cli;

import lombok.Getter;
import picocli.CommandLine;
import picocli.CommandLine.Option;

import java.util.Optional;

import static picocli.CommandLine.Help.Visibility.ALWAYS;

public class RasLocation {

    @Option(
        names = {"--address"},
        defaultValue = "localhost",
        showDefaultValue = ALWAYS
    )
    @Getter
    private String address;

    @Option(
        names = {"--port"},
        defaultValue = "1545",
        showDefaultValue = ALWAYS
    )
    @Getter
    private int port;

    @Option(
        names = {"--central-server-usr"}
    )
    @Getter
    private Optional<String> centralServerAdminUsername;

    @Option(
        names = {"--central-server-pwd"}
    )
    @Getter
    private Optional<String> centralServerAdminPassword;

}

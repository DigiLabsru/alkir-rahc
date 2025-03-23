package ru.digilabs.alkir.rahc.command.cli;

import lombok.Getter;
import picocli.CommandLine.Option;

import java.util.Optional;

import static picocli.CommandLine.Help.Visibility.ALWAYS;

public class RasLocation {

    @Option(
        names = {"--address"},
        order = 0,
        showDefaultValue = ALWAYS
    )
    @Getter
    private static String address = "localhost";

    @Option(
        names = {"--port"},
        order = 1,
        showDefaultValue = ALWAYS
    )
    @Getter
    private static int port = 1545;

    @Option(
        names = {"--central-server-usr"},
        order = 10
    )
    @Getter
    private static Optional<String> centralServerAdminUsername;

    @Option(
        names = {"--central-server-pwd"},
        order = 20
    )
    @Getter
    private static Optional<String> centralServerAdminPassword;

}

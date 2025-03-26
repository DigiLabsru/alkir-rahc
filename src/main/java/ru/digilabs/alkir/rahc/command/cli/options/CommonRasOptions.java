package ru.digilabs.alkir.rahc.command.cli.options;

import picocli.CommandLine;
import picocli.CommandLine.Option;
import ru.digilabs.alkir.rahc.dto.ConnectionDTO;

import java.util.Optional;

import static picocli.CommandLine.Help.Visibility.ALWAYS;

public class CommonRasOptions {

    @Option(
        names = {"--address"},
        order = -110,
        showDefaultValue = ALWAYS,
        scope = CommandLine.ScopeType.INHERIT
    )
    private static String address = "localhost";

    @Option(
        names = {"--port"},
        order = -109,
        showDefaultValue = ALWAYS,
        scope = CommandLine.ScopeType.INHERIT
    )
    private static int port = 1545;

    @Option(
        names = {"--central-server-usr"},
        order = -100,
        scope = CommandLine.ScopeType.INHERIT
    )
    private static Optional<String> centralServerAdminUsername = Optional.empty();

    @Option(
        names = {"--central-server-pwd"},
        order = -99,
        scope = CommandLine.ScopeType.INHERIT
    )
    private static Optional<String> centralServerAdminPassword = Optional.empty();

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public Optional<String> getCentralServerAdminUsername() {
        return centralServerAdminUsername;
    }

    public Optional<String> getCentralServerAdminPassword() {
        return centralServerAdminPassword;
    }

    public ConnectionDTO toConnectionDTO() {
        var connectionDTO = new ConnectionDTO();
        connectionDTO
            .setAddress(getAddress())
            .setPort(getPort())
            .setCentralServerAdminUsername(getCentralServerAdminUsername())
            .setCentralServerAdminPassword(getCentralServerAdminPassword());

        return connectionDTO;
    }

    public ConnectionDTO toConnectionDTO(ClusterAdminOptions clusterAdminOptions) {
        var connectionDTO = toConnectionDTO();
        connectionDTO
            .setClusterAdminUsername(clusterAdminOptions.getClusterAdminUsername())
            .setClusterAdminPassword(clusterAdminOptions.getClusterAdminPassword());

        return connectionDTO;
    }
}

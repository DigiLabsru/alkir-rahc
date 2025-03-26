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

    /**
     * Retrieves the server address used for establishing connections.
     *
     * @return the server address, which defaults to "localhost" if not explicitly set.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Returns the server port.
     *
     * @return the port number for server connections
     */
    public int getPort() {
        return port;
    }

    /**
     * Retrieves the optional username for the central server administrator.
     *
     * @return an Optional containing the admin username if set; otherwise, an empty Optional.
     */
    public Optional<String> getCentralServerAdminUsername() {
        return centralServerAdminUsername;
    }

    /**
     * Returns the optional central server administrator's password.
     *
     * @return an Optional containing the password if present, or an empty Optional otherwise.
     */
    public Optional<String> getCentralServerAdminPassword() {
        return centralServerAdminPassword;
    }

    /**
     * Creates a ConnectionDTO instance populated with the configured server address, port, and
     * central server administrator credentials.
     *
     * @return a ConnectionDTO instance with its fields set from the current command-line options.
     */
    public ConnectionDTO toConnectionDTO() {
        var connectionDTO = new ConnectionDTO();
        connectionDTO
            .setAddress(getAddress())
            .setPort(getPort())
            .setCentralServerAdminUsername(getCentralServerAdminUsername())
            .setCentralServerAdminPassword(getCentralServerAdminPassword());

        return connectionDTO;
    }

    /**
     * Creates a ConnectionDTO with cluster admin credentials.
     *
     * <p>This method first constructs a base ConnectionDTO using the default command-line options,
     * then integrates cluster admin credentials from the provided ClusterAdminOptions.
     *
     * @param clusterAdminOptions an object containing cluster admin username and password
     * @return a ConnectionDTO populated with both the default and cluster admin credentials
     */
    public ConnectionDTO toConnectionDTO(ClusterAdminOptions clusterAdminOptions) {
        var connectionDTO = toConnectionDTO();
        connectionDTO
            .setClusterAdminUsername(clusterAdminOptions.getClusterAdminUsername())
            .setClusterAdminPassword(clusterAdminOptions.getClusterAdminPassword());

        return connectionDTO;
    }
}

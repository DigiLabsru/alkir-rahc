package ru.digilabs.alkir.rahc.command.cli.options;

import picocli.CommandLine;
import picocli.CommandLine.Option;

import java.util.Optional;
import java.util.UUID;

public class ClusterAdminOptions {

    @Option(
        names = "--cluster-id",
        description = "server cluster identifier (UUID)",
        // TODO: required = true does not work with @MixIn
        required = true,
        order = -90,
        scope = CommandLine.ScopeType.INHERIT
    )
    private static UUID clusterId;

    @Option(
        names = {"--cluster-usr"},
        order = -89,
        scope = CommandLine.ScopeType.INHERIT
    )
    private static Optional<String> clusterAdminUsername = Optional.empty();

    @Option(
        names = {"--cluster-pwd"},
        order = -88,
        scope = CommandLine.ScopeType.INHERIT
    )
    private static Optional<String> clusterAdminPassword = Optional.empty();

    /**
     * Returns the UUID representing the server cluster identifier.
     *
     * <p>This value is provided through the {@code --cluster-id} command-line option.</p>
     *
     * @return the UUID of the server cluster
     */
    public UUID getClusterId() {
        return clusterId;
    }

    /**
     * Retrieves the cluster administrator's username.
     *
     * @return an Optional containing the username if provided, or an empty Optional otherwise.
     */
    public Optional<String> getClusterAdminUsername() {
        return clusterAdminUsername;
    }

    /**
     * Returns the cluster administrator's password.
     *
     * <p>This method retrieves the password provided via the '--cluster-pwd' command-line option.
     * The result is wrapped in an Optional that is empty if no password was specified.</p>
     *
     * @return an Optional containing the cluster administrator's password if provided, otherwise empty
     */
    public Optional<String> getClusterAdminPassword() {
        return clusterAdminPassword;
    }
}

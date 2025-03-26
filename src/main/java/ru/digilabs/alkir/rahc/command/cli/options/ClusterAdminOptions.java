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

    public UUID getClusterId() {
        return clusterId;
    }

    public Optional<String> getClusterAdminUsername() {
        return clusterAdminUsername;
    }

    public Optional<String> getClusterAdminPassword() {
        return clusterAdminPassword;
    }
}

package ru.digilabs.alkir.rahc.command.cli.options;

import picocli.CommandLine;
import picocli.CommandLine.Mixin;
import picocli.CommandLine.Option;

import java.util.Optional;
import java.util.UUID;

public class ClusterAdminOptions {

    @Mixin
    private static ClusterIdOption clusterIdOption;

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
        return clusterIdOption.getOptionValue();
    }

    public Optional<String> getClusterAdminUsername() {
        return clusterAdminUsername;
    }

    public Optional<String> getClusterAdminPassword() {
        return clusterAdminPassword;
    }
}

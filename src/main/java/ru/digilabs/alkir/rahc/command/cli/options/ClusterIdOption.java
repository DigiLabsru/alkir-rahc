package ru.digilabs.alkir.rahc.command.cli.options;

import picocli.CommandLine;
import picocli.CommandLine.Option;

import java.util.UUID;

public class ClusterIdOption {
    @Option(
        names = "--cluster-id",
        description = "cluster identifier (UUID)",
        required = true,
        order = -90,
        scope = CommandLine.ScopeType.INHERIT
    )
    private static UUID clusterId;

    public UUID getOptionValue() {
        return clusterId;
    }
}

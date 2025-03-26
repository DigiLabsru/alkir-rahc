package ru.digilabs.alkir.rahc.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;
import ru.digilabs.alkir.rahc.command.cli.ClusterManagerSubcommand;
import ru.digilabs.alkir.rahc.command.cli.ClusterSubcommand;
import ru.digilabs.alkir.rahc.command.cli.SessionSubcommand;
import ru.digilabs.alkir.rahc.command.cli.options.CommonRasOptions;

@Command(
    name = "cli",
    subcommands = {
        ClusterSubcommand.class,
        ClusterManagerSubcommand.class,
        SessionSubcommand.class,
    },
    description = "Run RAHC CLI",
    usageHelpAutoWidth = true,
    mixinStandardHelpOptions = true,
    sortOptions = false,
    footer = "@|green Copyright(c) 2022-2025|@"
)
@Component
@RequiredArgsConstructor
public class CliCommand {
    @Mixin
    @Getter
    private CommonRasOptions commonRasOptions;
}

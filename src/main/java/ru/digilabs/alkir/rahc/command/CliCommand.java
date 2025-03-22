package ru.digilabs.alkir.rahc.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ScopeType;
import ru.digilabs.alkir.rahc.command.cli.ClusterSubcommand;
import ru.digilabs.alkir.rahc.command.cli.RasLocation;
import ru.digilabs.alkir.rahc.dto.ConnectionDTO;

import java.util.concurrent.Callable;

import static picocli.CommandLine.Help.Visibility.ALWAYS;

@Command(
    name = "cli",
    subcommands = {
        ClusterSubcommand.class,
    },
    description = "Run RAHC CLI",
    usageHelpAutoWidth = true,
    footer = "@|green Copyright(c) 2022-2025|@"
)
@Component
@RequiredArgsConstructor
public class CliCommand {
    @Mixin
    private RasLocation rasLocation;
}

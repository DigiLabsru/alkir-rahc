package ru.digilabs.alkir.rahc.command.cli;

import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;
import ru.digilabs.alkir.rahc.command.CliCommand;
import ru.digilabs.alkir.rahc.command.cli.options.CommonRasOptions;

@Command(
    usageHelpAutoWidth = true,
    mixinStandardHelpOptions = true,
    sortOptions = false,
    footer = "@|green Copyright(c) 2022-2025|@"
)
public abstract class RasCliCommand {

    @ParentCommand
    CliCommand parentCommand;

    protected CommonRasOptions getCommonRasOptions() {
        return parentCommand.getCommonRasOptions();
    }
}

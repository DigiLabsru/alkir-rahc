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

    /**
     * Retrieves the common RAS options from the parent CLI command.
     *
     * <p>This method delegates to the parent command to obtain shared RAS configuration options
     * that are used to standardize command-line behavior across multiple commands.</p>
     *
     * @return the common RAS options from the parent command
     */
    protected CommonRasOptions getCommonRasOptions() {
        return parentCommand.getCommonRasOptions();
    }
}

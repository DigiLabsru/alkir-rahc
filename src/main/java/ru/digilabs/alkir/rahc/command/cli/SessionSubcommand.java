package ru.digilabs.alkir.rahc.command.cli;

import com._1c.v8.ibis.admin.ISessionInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;
import ru.digilabs.alkir.rahc.command.cli.options.ClusterAdminOptions;
import ru.digilabs.alkir.rahc.service.RacServiceProvider;

import java.io.PrintWriter;
import java.util.List;

@Command(
    name = "session",
    description = "Infobase session administration"
)
@Component
@RequiredArgsConstructor
public class SessionSubcommand extends RasCliCommand {

    private @Mixin ClusterAdminOptions clusterAdminOptions;

    private final RacServiceProvider racServiceProvider;
    private final PrintWriter printWriter;

    @Command(
        description = "get the session list",
        usageHelpAutoWidth = true,
        mixinStandardHelpOptions = true,
        sortOptions = false
    )
    public void list() {
        var connection = getCommonRasOptions().toConnectionDTO(clusterAdminOptions);

        try (var racService = racServiceProvider.getRacService(connection)) {
            List<ISessionInfo> clusterManagers = racService.getSessions(clusterAdminOptions.getClusterId());
            printWriter.println(clusterManagers);
        }
    }

}

package ru.digilabs.alkir.rahc.command.cli;

import com._1c.v8.ibis.admin.ISessionInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;
import picocli.CommandLine.Option;
import ru.digilabs.alkir.rahc.command.cli.options.ClusterAdminOptions;
import ru.digilabs.alkir.rahc.service.RacServiceProvider;

import java.util.List;
import java.util.UUID;

@Command(
    name = "session",
    description = "Infobase session administration"
)
@Component
@RequiredArgsConstructor
public class SessionSubcommand extends RasCliCommand {

    private @Mixin ClusterAdminOptions clusterAdminOptions;

    private final RacServiceProvider racServiceProvider;
    private final CliPrintWriter printWriter;

    @Command(
        description = "get the session list",
        usageHelpAutoWidth = true,
        mixinStandardHelpOptions = true,
        sortOptions = false
    )
    public void list(
        @Option(
            names = {"--ibId"},
            description = "infobase identifier (UUID)"
        )
        UUID ibId
    ) {
        var connection = getCommonRasOptions().toConnectionDTO(clusterAdminOptions);
        var clusterId = clusterAdminOptions.getClusterId();

        try (var racService = racServiceProvider.getRacService(connection)) {
            List<ISessionInfo> sessions;
            if (ibId != null) {
                sessions = racService.getSessions(clusterId, ibId);
            } else {
                sessions = racService.getSessions(clusterId);
            }
            printWriter.printSuccess(sessions);
        }
    }
}

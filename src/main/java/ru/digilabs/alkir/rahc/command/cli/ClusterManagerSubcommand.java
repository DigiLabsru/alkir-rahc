package ru.digilabs.alkir.rahc.command.cli;

import com._1c.v8.ibis.admin.IClusterManagerInfo;
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
    name = "cluster-manager",
    description = "Cluster manager administration"
)
@Component
@RequiredArgsConstructor
public class ClusterManagerSubcommand extends RasCliCommand {

    private @Mixin ClusterAdminOptions clusterAdminOptions;

    private final RacServiceProvider racServiceProvider;
    private final CliPrintWriter printWriter;

    @Command(
        description = "get the cluster manager information list",
        usageHelpAutoWidth = true,
        mixinStandardHelpOptions = true,
        sortOptions = false
    )
    public void list() {
        var connection = getCommonRasOptions().toConnectionDTO(clusterAdminOptions);

        try (var racService = racServiceProvider.getRacService(connection)) {
            List<IClusterManagerInfo> clusterManagers = racService.getClusterManagers(clusterAdminOptions.getClusterId());
            printWriter.println(clusterManagers);
        }
    }

    @Command(
        description = "get the cluster manager information",
        usageHelpAutoWidth = true,
        mixinStandardHelpOptions = true,
        sortOptions = false
    )
    public void info(
        @Option(
            names = "--cluster-manager-id",
            description = "server cluster manager identifier (UUID)",
            required = true
        ) UUID clusterManagerId
    ) {
        var connection = getCommonRasOptions().toConnectionDTO(clusterAdminOptions);

        try (var racService = racServiceProvider.getRacService(connection)) {
            IClusterManagerInfo clusterManagerInfo = racService.getClusterManagerInfo(clusterAdminOptions.getClusterId(), clusterManagerId);
            printWriter.printSuccess(clusterManagerInfo);
        }
    }

}

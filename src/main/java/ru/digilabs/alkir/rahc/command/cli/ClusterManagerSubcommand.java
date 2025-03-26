package ru.digilabs.alkir.rahc.command.cli;

import com._1c.v8.ibis.admin.IClusterManagerInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;
import picocli.CommandLine.Option;
import ru.digilabs.alkir.rahc.command.cli.options.ClusterAdminOptions;
import ru.digilabs.alkir.rahc.service.RacServiceProvider;

import java.io.PrintWriter;
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
    private final PrintWriter printWriter;

    /**
     * Retrieves and prints a list of cluster manager information.
     *
     * <p>
     * Establishes a connection using common Ras options combined with cluster administration options,
     * obtains a RAC service to fetch details for the configured cluster, and prints the resulting list.
     * </p>
     */
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

    /**
     * Retrieves and prints detailed information for a specified cluster manager.
     *
     * <p>This method creates a connection using common RAS options and cluster admin settings,
     * obtains the corresponding cluster manager information via the RAC service, and outputs the details.
     *
     * @param clusterManagerId the unique identifier of the cluster manager to query
     */
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
            printWriter.println(clusterManagerInfo);
        }
    }

}

package ru.digilabs.alkir.rahc.command.cli;

import com._1c.v8.ibis.admin.IClusterInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import ru.digilabs.alkir.rahc.service.RacServiceProvider;

import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

@Command(
    name = "cluster",
    description = "Cluster administration"
)
@Component
@RequiredArgsConstructor
public class ClusterSubcommand extends RasCliCommand {

    private final RacServiceProvider racServiceProvider;
    private final PrintWriter printWriter;

    /**
     * Retrieves and prints the list of cluster information.
     * 
     * <p>This method converts common connection options to a DTO,
     * acquires a RAC service instance to fetch the cluster list, and then
     * prints the list using the configured PrintWriter. The RAC service
     * is automatically closed using a try-with-resources block.
     */
    @Command(
        description = "get the cluster information list",
        usageHelpAutoWidth = true,
        mixinStandardHelpOptions = true,
        sortOptions = false
    )
    public void list() {
        var connection = getCommonRasOptions().toConnectionDTO();

        try (var racService = racServiceProvider.getRacService(connection)) {
            List<IClusterInfo> clusters = racService.getClusters();
            printWriter.println(clusters);
        }
    }

    /**
     * Retrieves and prints detailed information for a specific cluster.
     *
     * <p>This command uses common RAC options to establish a connection, obtains a RAC service,
     * and prints the cluster details for the provided cluster UUID.</p>
     *
     * @param clusterId the unique identifier of the cluster to retrieve information for
     */
    @Command(
        description = "get the cluster information",
        usageHelpAutoWidth = true,
        mixinStandardHelpOptions = true,
        sortOptions = false
    )
    public void info(
        @Option(
            names = "--cluster-id",
            description = "server cluster identifier (UUID)",
            required = true
        ) UUID clusterId
    ) {
        var connection = getCommonRasOptions().toConnectionDTO();

        try (var racService = racServiceProvider.getRacService(connection)) {
            IClusterInfo clusterInfo = racService.getClusterInfo(clusterId);
            printWriter.println(clusterInfo);
        }
    }

}

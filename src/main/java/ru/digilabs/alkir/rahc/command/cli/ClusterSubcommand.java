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

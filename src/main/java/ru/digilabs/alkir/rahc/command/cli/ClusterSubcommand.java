package ru.digilabs.alkir.rahc.command.cli;

import com._1c.v8.ibis.admin.IClusterInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;
import ru.digilabs.alkir.rahc.command.cli.options.ClusterAdminOptions;
import ru.digilabs.alkir.rahc.command.cli.options.ClusterIdOption;
import ru.digilabs.alkir.rahc.service.RacServiceProvider;

import java.util.List;

@Command(
    name = "cluster",
    description = "Cluster administration"
)
@Component
@RequiredArgsConstructor
public class ClusterSubcommand extends RasCliCommand {

    private final RacServiceProvider racServiceProvider;
    private final CliPrintWriter printWriter;

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
            printWriter.printSuccess(clusters);
        }
    }

    @Command(
        description = "get the cluster information",
        usageHelpAutoWidth = true,
        mixinStandardHelpOptions = true,
        sortOptions = false
    )
    public void info(@Mixin ClusterIdOption clusterIdOption) {
        var connection = getCommonRasOptions().toConnectionDTO();

        try (var racService = racServiceProvider.getRacService(connection)) {
            IClusterInfo clusterInfo = racService.getClusterInfo(clusterIdOption.getOptionValue());
            printWriter.printSuccess(clusterInfo);
        }
    }

    @Command(
        description = "delete the cluster",
        usageHelpAutoWidth = true,
        mixinStandardHelpOptions = true,
        sortOptions = false
    )
    public void delete(@Mixin ClusterAdminOptions clusterAdminOptions) {
        var connection = getCommonRasOptions().toConnectionDTO(clusterAdminOptions);

        try (var racService = racServiceProvider.getRacService(connection)) {
            racService.deleteCluster(clusterAdminOptions.getClusterId());
        }
    }

}

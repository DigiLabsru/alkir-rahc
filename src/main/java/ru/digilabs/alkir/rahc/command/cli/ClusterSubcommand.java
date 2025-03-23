package ru.digilabs.alkir.rahc.command.cli;

import com._1c.v8.ibis.admin.IClusterInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;
import ru.digilabs.alkir.rahc.dto.ConnectionDTO;
import ru.digilabs.alkir.rahc.service.RacServiceProvider;

import java.util.List;

@Command(
    name = "cluster",
    description = "cluster command",
    usageHelpAutoWidth = true,
    mixinStandardHelpOptions = true,
    sortOptions = false,
    footer = "@|green Copyright(c) 2022-2025|@"
)
@Component
@RequiredArgsConstructor
public class ClusterSubcommand {

    @Mixin
    private RasLocation rasLocation;

    private final RacServiceProvider racServiceProvider;
    private final ObjectMapper objectMapper;

    @Command(
        usageHelpAutoWidth = true,
        mixinStandardHelpOptions = true,
        sortOptions = false
    )
    @SneakyThrows
    public void list(@Mixin RasLocation rasLocation) {
        var connection = new ConnectionDTO();
        connection.setAddress(rasLocation.getAddress());
        connection.setPort(rasLocation.getPort());
        connection.setCentralServerAdminUsername(rasLocation.getCentralServerAdminUsername());
        connection.setCentralServerAdminPassword(rasLocation.getCentralServerAdminPassword());

        try (var racService = racServiceProvider.getRacService(connection)) {
            List<IClusterInfo> clusters = racService.getClusters();
            System.out.println(objectMapper.writeValueAsString(clusters));
        }
    }

}

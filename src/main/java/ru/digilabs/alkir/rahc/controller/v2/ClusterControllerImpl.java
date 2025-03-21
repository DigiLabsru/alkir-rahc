package ru.digilabs.alkir.rahc.controller.v2;

import com._1c.v8.ibis.admin.IClusterInfo;
import com._1c.v8.ibis.admin.IClusterServiceInfo;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.digilabs.alkir.rahc.controller.v2.api.ClusterController;
import ru.digilabs.alkir.rahc.controller.v2.api.ConnectionDTO;
import ru.digilabs.alkir.rahc.service.RacServiceProvider;

import java.util.List;
import java.util.UUID;

@RestController("clusterControllerV2Impl")
@AutoJsonRpcServiceImpl
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer")
@RequestMapping("api/rest/v2/cluster")
@Validated
@Slf4j
public class ClusterControllerImpl implements ClusterController {

    private final RacServiceProvider racServiceProvider;

    @Override
    @GetMapping("all")
    public List<IClusterInfo> list(
        @Valid ConnectionDTO connection
    ) {
        try (var racService = racServiceProvider.getRacService(connection)) {
            return racService.getClusters();
        }
    }

    @Override
    @PutMapping
    public UUID edit(
        @Valid ConnectionDTO connection,
        @RequestBody IClusterInfo clusterInfo
    ) {
        try (var racService = racServiceProvider.getRacService(connection)) {
            return racService.editCluster(clusterInfo);
        }
    }

    @Override
    @GetMapping("services")
    public List<IClusterServiceInfo> getClusterServices(ConnectionDTO connection) {
        try (var racService = racServiceProvider.getRacService(connection)) {
            var clusterId = racService.getClusterId(connection);
            return racService.getClusterServices(clusterId);
        }
    }

}

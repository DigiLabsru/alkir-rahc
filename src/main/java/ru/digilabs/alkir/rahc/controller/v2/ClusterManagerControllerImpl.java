package ru.digilabs.alkir.rahc.controller.v2;

import com._1c.v8.ibis.admin.IClusterManagerInfo;
import com._1c.v8.ibis.admin.IClusterServiceInfo;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.digilabs.alkir.rahc.controller.v2.api.ClusterManagerController;
import ru.digilabs.alkir.rahc.controller.v2.api.ConnectionDTO;
import ru.digilabs.alkir.rahc.controller.v2.validation.ClusterIdIsNotEmpty;
import ru.digilabs.alkir.rahc.service.RacServiceProvider;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController("clusterManagerControllerV2Impl")
@AutoJsonRpcServiceImpl
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer")
@RequestMapping("api/rest/v2/clusterManager")
@Validated
public class ClusterManagerControllerImpl implements ClusterManagerController {

  private final RacServiceProvider racServiceProvider;

  @Override
  @GetMapping("all")
  public List<IClusterManagerInfo> list(
    @Valid @ClusterIdIsNotEmpty ConnectionDTO connection
  ) {
    try (var racService = racServiceProvider.getRacService(connection)) {
      var clusterId = racService.getClusterId(connection);
      return racService.getClusterManagers(clusterId);
    }
  }

  @Override
  @GetMapping
  public IClusterManagerInfo getClusterManagerInfo(
    @Valid @ClusterIdIsNotEmpty ConnectionDTO connection,
    UUID managerId
  ) {
    try (var racService = racServiceProvider.getRacService(connection)) {
      var clusterId = racService.getClusterId(connection);
      return racService.getClusterManagerInfo(clusterId, managerId);
    }
  }

  @Override
  @GetMapping("services")
  public List<IClusterServiceInfo> getClusterServices(
    @Valid @ClusterIdIsNotEmpty ConnectionDTO connection,
    UUID managerId
  ) {
    try (var racService = racServiceProvider.getRacService(connection)) {
      var clusterId = racService.getClusterId(connection);
      return racService.getClusterServices(clusterId, managerId);
    }
  }
}

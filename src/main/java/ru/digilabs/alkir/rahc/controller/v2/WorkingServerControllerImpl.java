package ru.digilabs.alkir.rahc.controller.v2;

import com._1c.v8.ibis.admin.IClusterManagerInfo;
import com._1c.v8.ibis.admin.IWorkingProcessInfo;
import com._1c.v8.ibis.admin.IWorkingServerInfo;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.digilabs.alkir.rahc.controller.v2.api.ConnectionDTO;
import ru.digilabs.alkir.rahc.controller.v2.api.WorkingServerController;
import ru.digilabs.alkir.rahc.controller.v2.validation.ClusterIdIsNotEmpty;
import ru.digilabs.alkir.rahc.service.RacServiceProvider;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController("workingServerControllerV2Impl")
@AutoJsonRpcServiceImpl
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer")
@RequestMapping("api/rest/v2/workingServer")
@Validated
public class WorkingServerControllerImpl implements WorkingServerController {

  private final RacServiceProvider racServiceProvider;

  @Override
  @GetMapping("all")
  public List<IWorkingServerInfo> list(
    @Valid @ClusterIdIsNotEmpty ConnectionDTO connection
  ) {
    try (var racService = racServiceProvider.getRacService(connection)) {
      var clusterId = racService.getClusterId(connection);
      return racService.getWorkingServers(clusterId);
    }
  }

  @Override
  @GetMapping("workingProcesses")
  public List<IWorkingProcessInfo> workingProcesses(
    @Valid @ClusterIdIsNotEmpty ConnectionDTO connection,
    @RequestParam UUID serverId
  ) {
    try (var racService = racServiceProvider.getRacService(connection)) {
      var clusterId = racService.getClusterId(connection);
      return racService.getServerWorkingProcesses(clusterId, serverId);
    }
  }

  @Override
  @GetMapping("clusterManagers")
  public List<IClusterManagerInfo> clusterManagers(
    @Valid @ClusterIdIsNotEmpty ConnectionDTO connection,
    @RequestParam UUID serverId
  ) {
    try (var racService = racServiceProvider.getRacService(connection)) {
      var clusterId = racService.getClusterId(connection);
      return racService.getServerClusterManagers(clusterId, serverId);
    }
  }

  @Override
  @GetMapping
  public IWorkingServerInfo info(
    @Valid @ClusterIdIsNotEmpty ConnectionDTO connection,
    @RequestParam UUID serverId
  ) {
    try (var racService = racServiceProvider.getRacService(connection)) {
      var clusterId = racService.getClusterId(connection);
      return racService.getWorkingServer(clusterId, serverId);
    }
  }

  @Override
  @PutMapping
  public UUID edit(
    @Valid @ClusterIdIsNotEmpty ConnectionDTO connection,
    @RequestBody IWorkingServerInfo serverInfo
  ) {
    try (var racService = racServiceProvider.getRacService(connection)) {
      var clusterId = racService.getClusterId(connection);
      return racService.editWorkingServer(clusterId, serverInfo);
    }
  }

  @Override
  @DeleteMapping
  public void delete(
    @Valid @ClusterIdIsNotEmpty ConnectionDTO connection,
    @RequestParam UUID serverId
  ) {
    try (var racService = racServiceProvider.getRacService(connection)) {
      var clusterId = racService.getClusterId(connection);
      racService.deleteWorkingServer(clusterId, serverId);
    }
  }
}

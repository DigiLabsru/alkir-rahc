package ru.digilabs.alkir.rahc.controller.v2;

import com._1c.v8.ibis.admin.ISessionInfo;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.digilabs.alkir.rahc.controller.v2.api.ConnectionDTO;
import ru.digilabs.alkir.rahc.controller.v2.api.SessionController;
import ru.digilabs.alkir.rahc.controller.v2.validation.ClusterIdIsNotEmpty;
import ru.digilabs.alkir.rahc.service.RacServiceProvider;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController("sessionControllerV2Impl")
@AutoJsonRpcServiceImpl
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer")
@RequestMapping("api/rest/v2/session")
@Validated
public class SessionControllerImpl implements SessionController {

  private final RacServiceProvider racServiceProvider;

  @Override
  @GetMapping("all")
  public List<ISessionInfo> list(
    @Valid @ClusterIdIsNotEmpty ConnectionDTO connection
  ) {
    try (var racService = racServiceProvider.getRacService(connection)) {
      var clusterId = racService.getClusterId(connection);
      return racService.getSessions(clusterId);
    }
  }

  @Override
  @GetMapping
  public ISessionInfo info(
    @Valid @ClusterIdIsNotEmpty ConnectionDTO connection,
    @RequestParam UUID sid
  ) {
    try (var racService = racServiceProvider.getRacService(connection)) {
      var clusterId = racService.getClusterId(connection);
      return racService.getSessionInfo(clusterId, sid);
    }
  }

  @Override
  @DeleteMapping
  public void delete(
    @Valid @ClusterIdIsNotEmpty ConnectionDTO connection,
    @RequestParam UUID sid,
    @RequestParam Optional<String> message
  ) {
    try (var racService = racServiceProvider.getRacService(connection)) {
      var clusterId = racService.getClusterId(connection);
      racService.terminateSession(clusterId, sid, message);
    }
  }

}

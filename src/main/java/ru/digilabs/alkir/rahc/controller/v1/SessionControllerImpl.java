package ru.digilabs.alkir.rahc.controller.v1;

import com._1c.v8.ibis.admin.ISessionInfo;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.digilabs.alkir.rahc.controller.v1.api.SessionController;
import ru.digilabs.alkir.rahc.service.RacService;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController("sessionControllerV1Impl")
@AutoJsonRpcServiceImpl
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer")
@RequestMapping("api/rest/v1/session")
public class SessionControllerImpl implements SessionController {

  @Qualifier("sessionScopedRacService")
  private final RacService racService;

  @Override
  @GetMapping("all")
  public List<ISessionInfo> list(@RequestParam UUID clusterId) {
    return racService.getSessions(clusterId);
  }

  @Override
  @GetMapping
  public ISessionInfo info(@RequestParam UUID clusterId, @RequestParam UUID sid) {
    return racService.getSessionInfo(clusterId, sid);
  }

  @Override
  @DeleteMapping
  public void delete(
    @RequestParam UUID clusterId,
    @RequestParam UUID sid,
    @RequestParam Optional<String> message
  ) {
    racService.terminateSession(clusterId, sid, message);
  }

}

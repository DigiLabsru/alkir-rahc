package ru.digilabs.alkir.rahc.controller.v1;

import com._1c.v8.ibis.admin.IClusterManagerInfo;
import com._1c.v8.ibis.admin.IWorkingProcessInfo;
import com._1c.v8.ibis.admin.IWorkingServerInfo;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.digilabs.alkir.rahc.controller.v1.api.WorkingServerController;
import ru.digilabs.alkir.rahc.service.RacService;

import java.util.List;
import java.util.UUID;

@RestController("workingServerControllerV1Impl")
@Profile("web")
@AutoJsonRpcServiceImpl
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer")
@RequestMapping("api/rest/v1/workingServer")
public class WorkingServerControllerImpl implements WorkingServerController {

    @Qualifier("sessionScopedRacService")
    private final RacService racService;

    @Override
    @GetMapping("all")
    public List<IWorkingServerInfo> list(@RequestParam UUID clusterId) {
        return racService.getWorkingServers(clusterId);
    }

    @Override
    @GetMapping("workingProcesses")
    public List<IWorkingProcessInfo> workingProcesses(@RequestParam UUID clusterId, @RequestParam UUID serverId) {
        return racService.getServerWorkingProcesses(clusterId, serverId);
    }

    @Override
    @GetMapping("clusterManagers")
    public List<IClusterManagerInfo> clusterManagers(@RequestParam UUID clusterId, @RequestParam UUID serverId) {
        return racService.getServerClusterManagers(clusterId, serverId);
    }

    @Override
    @GetMapping
    public IWorkingServerInfo info(@RequestParam UUID clusterId, @RequestParam UUID serverId) {
        return racService.getWorkingServer(clusterId, serverId);
    }

    @Override
    @PutMapping
    public UUID edit(@RequestParam UUID clusterId, @RequestBody IWorkingServerInfo serverInfo) {
        return racService.editWorkingServer(clusterId, serverInfo);
    }

    @Override
    @DeleteMapping
    public void delete(@RequestParam UUID clusterId, @RequestParam UUID serverId) {
        racService.deleteWorkingServer(clusterId, serverId);
    }
}

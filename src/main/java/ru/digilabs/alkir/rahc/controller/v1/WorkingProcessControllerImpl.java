package ru.digilabs.alkir.rahc.controller.v1;

import com._1c.v8.ibis.admin.IWorkingProcessInfo;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.digilabs.alkir.rahc.controller.v1.api.WorkingProcessController;
import ru.digilabs.alkir.rahc.service.RacService;

import java.util.List;
import java.util.UUID;

@RestController("workingProcessControllerV1Impl")
@Profile("web")
@AutoJsonRpcServiceImpl
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer")
@RequestMapping("api/rest/v1/workingProcess")
public class WorkingProcessControllerImpl implements WorkingProcessController {

    @Qualifier("sessionScopedRacService")
    private final RacService racService;

    @Override
    @GetMapping("all")
    public List<IWorkingProcessInfo> list(@RequestParam UUID clusterId) {
        return racService.getWorkingProcesses(clusterId);
    }

    @Override
    @GetMapping
    public IWorkingProcessInfo info(@RequestParam UUID clusterId, @RequestParam UUID processId) {
        return racService.getWorkingProcess(clusterId, processId);
    }

}

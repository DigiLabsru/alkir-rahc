package ru.digilabs.alkir.rahc.controller.v2;

import com._1c.v8.ibis.admin.IWorkingProcessInfo;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.digilabs.alkir.rahc.dto.ConnectionDTO;
import ru.digilabs.alkir.rahc.controller.v2.api.WorkingProcessController;
import ru.digilabs.alkir.rahc.controller.v2.validation.ClusterIdIsNotEmpty;
import ru.digilabs.alkir.rahc.service.RacServiceProvider;

import java.util.List;
import java.util.UUID;

@RestController("workingProcessControllerV2Impl")
@AutoJsonRpcServiceImpl
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer")
@RequestMapping("api/rest/v2/workingProcess")
@Validated
public class WorkingProcessControllerImpl implements WorkingProcessController {

    private final RacServiceProvider racServiceProvider;

    @Override
    @GetMapping("all")
    public List<IWorkingProcessInfo> list(
        @Valid @ClusterIdIsNotEmpty ConnectionDTO connection
    ) {
        try (var racService = racServiceProvider.getRacService(connection)) {
            var clusterId = racService.getClusterId(connection);
            return racService.getWorkingProcesses(clusterId);
        }
    }

    @Override
    @GetMapping
    public IWorkingProcessInfo info(
        @Valid @ClusterIdIsNotEmpty ConnectionDTO connection,
        @RequestParam UUID processId
    ) {
        try (var racService = racServiceProvider.getRacService(connection)) {
            var clusterId = racService.getClusterId(connection);
            return racService.getWorkingProcess(clusterId, processId);
        }
    }

}

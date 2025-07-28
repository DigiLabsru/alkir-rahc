package ru.digilabs.alkir.rahc.controller.v1;

import com._1c.v8.ibis.admin.IClusterInfo;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.digilabs.alkir.rahc.controller.v1.api.ClusterController;
import ru.digilabs.alkir.rahc.service.RacService;

import java.util.List;
import java.util.UUID;

@RestController("clusterControllerV1Impl")
@Profile("web")
@AutoJsonRpcServiceImpl
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer")
@RequestMapping("api/rest/v1/cluster")
public class ClusterControllerImpl implements ClusterController {

    @Qualifier("sessionScopedRacService")
    private final RacService racService;

    @Override
    @GetMapping("all")
    public List<IClusterInfo> list() {
        return racService.getClusters();
    }

    @Override
    @PutMapping
    public UUID edit(@RequestBody IClusterInfo clusterInfo) {
        return racService.editCluster(clusterInfo);
    }

}

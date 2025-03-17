package ru.digilabs.alkir.rahc.controller.v2;

import com._1c.v8.ibis.admin.IInfoBaseInfo;
import com._1c.v8.ibis.admin.IInfoBaseInfoShort;
import com._1c.v8.ibis.admin.ISessionInfo;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.digilabs.alkir.rahc.controller.v2.api.ConnectionDTO;
import ru.digilabs.alkir.rahc.controller.v2.api.InfoBaseController;
import ru.digilabs.alkir.rahc.controller.v2.validation.ClusterIdIsNotEmpty;
import ru.digilabs.alkir.rahc.controller.v2.validation.InfoBaseIdIsNotEmpty;
import ru.digilabs.alkir.rahc.service.RacServiceProvider;

import java.util.List;
import java.util.UUID;

@RestController("infoBaseControllerV2Impl")
@AutoJsonRpcServiceImpl
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer")
@RequestMapping("api/rest/v2/infobase")
@Validated
public class InfoBaseControllerImpl implements InfoBaseController {

    private final RacServiceProvider racServiceProvider;

    @Override
    @GetMapping("all")
    public List<IInfoBaseInfoShort> list(
        @Valid @ClusterIdIsNotEmpty ConnectionDTO connection
    ) {
        try (var racService = racServiceProvider.getRacService(connection)) {
            var clusterId = racService.getClusterId(connection);

            return racService.getInfoBases(clusterId);
        }
    }

    @Override
    @GetMapping("sessions")
    public List<ISessionInfo> sessions(
        @Valid @ClusterIdIsNotEmpty @InfoBaseIdIsNotEmpty ConnectionDTO connection
    ) {
        try (var racService = racServiceProvider.getRacService(connection)) {
            var clusterId = racService.getClusterId(connection);
            var ibId = racService.getIbId(connection);

            return racService.getSessions(clusterId, ibId);
        }
    }

    @Override
    @GetMapping("all/full")
    public List<IInfoBaseInfo> fullList(
        @Valid @ClusterIdIsNotEmpty ConnectionDTO connection
    ) {
        try (var racService = racServiceProvider.getRacService(connection)) {
            var clusterId = racService.getClusterId(connection);

            return racService.getInfoBasesFull(clusterId);
        }
    }

    @Override
    @GetMapping
    public IInfoBaseInfoShort getInfoBase(
        @Valid @ClusterIdIsNotEmpty @InfoBaseIdIsNotEmpty ConnectionDTO connection
    ) {
        try (var racService = racServiceProvider.getRacService(connection)) {
            var clusterId = racService.getClusterId(connection);
            var ibId = racService.getIbId(connection);

            return racService.getInfoBase(clusterId, ibId);
        }
    }

    @Override
    @GetMapping("full")
    public IInfoBaseInfo getInfoBaseFull(
        @Valid @ClusterIdIsNotEmpty @InfoBaseIdIsNotEmpty ConnectionDTO connection
    ) {
        try (var racService = racServiceProvider.getRacService(connection)) {
            var clusterId = racService.getClusterId(connection);
            var ibId = racService.getIbId(connection);
            var ibUsername = connection.getIbUsername();
            var ibPassword = connection.getIbPassword();

            return racService.getInfoBaseFull(clusterId, ibId, ibUsername, ibPassword);
        }
    }

    @Override
    @PutMapping
    public void update(
        @Valid @ClusterIdIsNotEmpty ConnectionDTO connection,
        @RequestBody IInfoBaseInfo ibInfo
    ) {
        try (var racService = racServiceProvider.getRacService(connection)) {
            var clusterId = racService.getClusterId(connection);
            var ibUsername = connection.getIbUsername();
            var ibPassword = connection.getIbPassword();

            racService.updateInfoBase(clusterId, ibInfo, ibUsername, ibPassword);
        }
    }

    @Override
    @PostMapping
    public UUID create(
        @Valid @ClusterIdIsNotEmpty ConnectionDTO connection,
        @RequestBody IInfoBaseInfo ibInfo,
        @RequestParam int mode
    ) {
        try (var racService = racServiceProvider.getRacService(connection)) {
            var clusterId = racService.getClusterId(connection);

            return racService.createInfoBase(clusterId, ibInfo, mode);
        }
    }

}
